package caruana.silverbirch.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Item;
import caruana.silverbirch.SilverBirchException.SilverBirchTransactionException;
import caruana.silverbirch.Transaction;
import caruana.silverbirch.server.items.GetDrive;
import caruana.silverbirch.server.items.ListDrives;
import caruana.silverbirch.server.items.ItemsImpl;
import datomic.Peer;


public class TransactionalDriveTest {
    
    private static Logger logger = LoggerFactory.getLogger(TransactionalDriveTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private TransactionImpl transaction;
    private TransactionalItems transactionalItems;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("TransactionalDriveTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        Peer.createDatabase(repo);
        conn = Peer.connect(repo);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.bootstrap(conn);
        ItemsImpl items = new ItemsImpl();
        items.setGetDrive(new GetDrive());
        items.setListDrives(new ListDrives());
        transaction = new TransactionImpl(conn);
        transactionalItems = new TransactionalItems(items, transaction);
    }

    @Test
    public void createDrive()
    {
        profiler.start("createDrive");
        Item drive = transactionalItems.createDrive("test");
        assertNotNull(drive);
        profiler.start("hasChanges");
        assertTrue(transaction.hasChanges());
        profiler.start("apply");
        transaction.applyChanges();
        profiler.stop().log();
    }

    @Test
    public void uniqueAcrossTransaction()
    {
        profiler.start("createDrive");
        Item drive1 = transactionalItems.createDrive("test");
        assertNotNull(drive1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateDrive");
        Item drive2 = transactionalItems.createDrive("test");
        assertNotNull(drive2);
        profiler.start("apply");
        try
        {
            transaction.applyChanges();
            fail("Failed to catch duplicate drive");
        }
        catch(SilverBirchTransactionException e)
        {
        }
        profiler.stop().log();
    }

    @Ignore("Datomic bug: see datomic.UniqueValueWithinTransaction") @Test
    public void uniqueWithinTransaction()
    {
        profiler.start("createDrive");
        Item drive1 = transactionalItems.createDrive("test");
        assertNotNull(drive1);
        profiler.start("createDuplicateDrive");
        Item drive2 = transactionalItems.createDrive("test");
        assertNotNull(drive2);
        profiler.start("apply");
        try
        {
            transaction.applyChanges();
            fail("Failed to catch duplicate drive");
        }
        catch(SilverBirchTransactionException e)
        {
        }
        profiler.stop().log();
    }

    @Test
    public void uniqueWithinAndAcrossTransaction()
    {
        profiler.start("createDrive");
        Item drive1 = transactionalItems.createDrive("test");
        assertNotNull(drive1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateDrives");
        Item drive2 = transactionalItems.createDrive("test");
        assertNotNull(drive2);
        Item drive3 = transactionalItems.createDrive("test");
        assertNotNull(drive3);
        profiler.start("apply");
        try
        {
            transaction.applyChanges();
            fail("Failed to catch duplicate drive");
        }
        catch(SilverBirchTransactionException e)
        {
        }
        profiler.stop().log();
    }

    @Test
    public void getDrive()
    {
        profiler.start("getDrive");
        Item drive1 = transactionalItems.getDrive("test");
        assertNull(drive1);
        profiler.start("createDrive");
        Item drive2 = transactionalItems.createDrive("test");
        assertNotNull(drive2);
        Item drive3 = transactionalItems.getDrive("test");
        assertNull(drive3);
        Transaction.Result r = transaction.applyChanges();
        Object drive2id = r.resolveId(drive2.getId());
        assertNotNull(drive2id);
        profiler.start("getDrive");
        Item drive4 = transactionalItems.getDrive("test");
        assertNotNull(drive4);
        assertEquals(drive2id, drive4.getId());
        assertEquals(drive2.getName(), drive4.getName());
        assertEquals(drive4.getDriveId(), drive4.getId());
        assertEquals(drive4.getRootId(), drive4.getId());
        profiler.stop().log();
    }

    @Test
    public void listDrives()
    {
        profiler.start("listDrives");
        List<Item> drives1 = transactionalItems.listDrives();
        assertNotNull(drives1);
        assertTrue(drives1.isEmpty());
        profiler.start("createDrives");
        Item drive1 = transactionalItems.createDrive("test1");
        assertNotNull(drive1);
        Item drive2 = transactionalItems.createDrive("test2");
        assertNotNull(drive2);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("listDrives");
        List<Item> drives2 = transactionalItems.listDrives();
        assertNotNull(drives2);
        assertEquals(2, drives2.size());
        profiler.stop().log();
    }

}
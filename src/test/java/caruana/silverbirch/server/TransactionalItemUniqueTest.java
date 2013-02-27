package caruana.silverbirch.server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Item;
import caruana.silverbirch.SilverBirchException.SilverBirchTransactionException;
import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.TransactionImpl;
import caruana.silverbirch.server.TransactionalItems;
import caruana.silverbirch.server.items.GetDrive;
import caruana.silverbirch.server.items.ItemsImpl;

import datomic.Peer;


public class TransactionalItemUniqueTest {
    
    private static Logger logger = LoggerFactory.getLogger(TransactionalItemUniqueTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private TransactionImpl transaction;
    private TransactionalItems transactionalItems;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("TransactionalItemUniqueTest");
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
        transaction = new TransactionImpl(conn);
        transactionalItems = new TransactionalItems(items, transaction);
    }

    @Test
    public void uniqueWithinParentAcrossTransaction()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive1");
        assertNotNull(drive1);
        Item item1 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateItem");
        Item item2 = transactionalItems.createDrive("item2");
        assertNotNull(item2);
        transaction.applyChanges();
        profiler.stop().log();
    }
 
    @Test
    public void uniqueWithinParentWithinTransaction()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive1");
        assertNotNull(drive1);
        Item item1 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item1);
        profiler.start("createDuplicateItem");
        Item item2 = transactionalItems.createDrive("item2");
        assertNotNull(item2);
        transaction.applyChanges();
        profiler.stop().log();
    }

    @Test
    public void uniqueWithinParentWithinAndAcrossTransaction()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive1");
        assertNotNull(drive1);
        Item item1 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateItems");
        Item item2 = transactionalItems.createDrive("item2");
        assertNotNull(item2);
        Item item3 = transactionalItems.createDrive("item3");
        assertNotNull(item3);
        transaction.applyChanges();
        profiler.stop().log();
    }

    @Test
    public void uniqueAcrossParentAcrossTransaction()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive1");
        assertNotNull(drive1);
        Item item1 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateItem");
        Item drive2 = transactionalItems.createDrive("drive2");
        assertNotNull(drive2);
        Item item2 = transactionalItems.createItem(drive2, "item1");
        assertNotNull(item2);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.stop().log();
    }

    @Test
    public void uniqueAcrossParentWithinTransaction()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive1");
        assertNotNull(drive1);
        Item item1 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item1);
        profiler.start("createDuplicateItem");
        Item drive2 = transactionalItems.createDrive("drive2");
        assertNotNull(drive2);
        Item item2 = transactionalItems.createItem(drive2, "item1");
        assertNotNull(item2);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.stop().log();
    }

    @Test
    public void uniqueAcrossParentWithinAndAcrossTransaction()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive1");
        assertNotNull(drive1);
        Item item1 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateItem");
        Item drive2 = transactionalItems.createDrive("drive2");
        assertNotNull(drive2);
        Item item2 = transactionalItems.createItem(drive2, "item1");
        assertNotNull(item2);
        Item drive3 = transactionalItems.createDrive("drive3");
        assertNotNull(drive3);
        Item item3 = transactionalItems.createItem(drive3, "item1");
        assertNotNull(item3);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.stop().log();
    }
    
    @Test
    public void duplicateWithParentAcrossTransaction()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive1");
        assertNotNull(drive1);
        Item item1 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateItem");
        // TODO: think about resolving ids on apply changes
        drive1 = transactionalItems.getDrive("drive1");
        assertNotNull(drive1);
        Item item2 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item2);
        profiler.start("apply");
        try
        {
            transaction.applyChanges();
            fail("Failed to catch duplicate items within parent");
        }
        catch(SilverBirchTransactionException e)
        {
        }
        profiler.stop().log();
    }

    @Ignore("Datomic bug: see datomic.UniqueValueWithinTransaction") @Test
    public void duplicateWithParentWithinTransaction()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive1");
        assertNotNull(drive1);
        Item item1 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item1);
        profiler.start("createDuplicateItem");
        Item item2 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item2);
        profiler.start("apply");
        try
        {
            transaction.applyChanges();
            fail("Failed to catch duplicate items within parent");
        }
        catch(SilverBirchTransactionException e)
        {
        }
        profiler.stop().log();
    }

    @Test
    public void duplicateWithParentWithinAndAcrossTransaction()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive1");
        assertNotNull(drive1);
        Item item1 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateItem");
        // TODO: think about resolving ids on apply changes
        drive1 = transactionalItems.getDrive("drive1");
        assertNotNull(drive1);
        Item item2 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item2);
        Item item3 = transactionalItems.createItem(drive1, "item1");
        assertNotNull(item3);
        profiler.start("apply");
        try
        {
            transaction.applyChanges();
            fail("Failed to catch duplicate items within parent");
        }
        catch(SilverBirchTransactionException e)
        {
        }
        profiler.stop().log();
    }

}
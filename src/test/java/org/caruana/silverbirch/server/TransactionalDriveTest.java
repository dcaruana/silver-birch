package org.caruana.silverbirch.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.Transaction;
import org.caruana.silverbirch.SilverBirchException.SilverBirchTransactionException;
import org.caruana.silverbirch.server.storage.GetDrive;
import org.caruana.silverbirch.server.storage.StorageImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import datomic.Peer;


public class TransactionalDriveTest {
    
    private static Logger logger = LoggerFactory.getLogger(TransactionalDriveTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private TransactionImpl transaction;
    private TransactionalStorage transactionalStorage;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("TransactionalStorageTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        Peer.createDatabase(repo);
        conn = Peer.connect(repo);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.bootstrap(conn);
        StorageImpl storage = new StorageImpl();
        storage.setGetDrive(new GetDrive());
        transaction = new TransactionImpl(conn);
        transactionalStorage = new TransactionalStorage(storage, transaction);
    }

    @Test
    public void createDrive()
    {
        profiler.start("createDrive");
        Node drive = transactionalStorage.createDrive("test");
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
        Node drive1 = transactionalStorage.createDrive("test");
        assertNotNull(drive1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateDrive");
        Node drive2 = transactionalStorage.createDrive("test");
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
        Node drive1 = transactionalStorage.createDrive("test");
        assertNotNull(drive1);
        profiler.start("createDuplicateDrive");
        Node drive2 = transactionalStorage.createDrive("test");
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
        Node drive1 = transactionalStorage.createDrive("test");
        assertNotNull(drive1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateDrives");
        Node drive2 = transactionalStorage.createDrive("test");
        assertNotNull(drive2);
        Node drive3 = transactionalStorage.createDrive("test");
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
        Node drive1 = transactionalStorage.getDrive("test");
        assertNull(drive1);
        profiler.start("createDrive");
        Node drive2 = transactionalStorage.createDrive("test");
        assertNotNull(drive2);
        Node drive3 = transactionalStorage.getDrive("test");
        assertNull(drive3);
        Transaction.Result r = transaction.applyChanges();
        Object drive2id = r.resolveId(drive2.getId());
        assertNotNull(drive2id);
        profiler.start("getDrive");
        Node drive4 = transactionalStorage.getDrive("test");
        assertNotNull(drive4);
        assertEquals(drive2id, drive4.getId());
        assertEquals(drive2.getName(), drive4.getName());
        assertEquals(drive4.getDriveId(), drive4.getId());
        assertEquals(drive4.getRootId(), drive4.getId());
        profiler.stop().log();
    }

}
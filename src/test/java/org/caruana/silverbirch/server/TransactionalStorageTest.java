package org.caruana.silverbirch.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.Transaction;
import org.caruana.silverbirch.server.storage.GetDrive;
import org.caruana.silverbirch.server.storage.StorageImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import datomic.Peer;


public class TransactionalStorageTest {
    
    private static Logger logger = LoggerFactory.getLogger(TransactionalStorageTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private TransactionImpl transaction;
    private TransactionalStorage transactionalStorage;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("StorageImplTest");
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

    @Test
    public void createNode()
    {
        profiler.start("createDrive");
        Node drive = transactionalStorage.createDrive("test");
        assertNotNull(drive);
        profiler.start("createNode");
        Node node = transactionalStorage.createNode(drive, "node1");
        assertNotNull(node);
        assertNotNull(node.getId());
        assertEquals(drive.getDriveId(), node.getDriveId());
        assertEquals(drive.getRootId(), node.getRootId());
        assertEquals("node1", node.getName());
        profiler.start("hasChanges");
        assertTrue(transaction.hasChanges());
        profiler.start("apply");
        transaction.applyChanges();
        profiler.stop().log();
    }
    
}
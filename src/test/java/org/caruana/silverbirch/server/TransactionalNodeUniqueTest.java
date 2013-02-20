package org.caruana.silverbirch.server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.caruana.silverbirch.Node;
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


public class TransactionalNodeUniqueTest {
    
    private static Logger logger = LoggerFactory.getLogger(TransactionalNodeUniqueTest.class);

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

    // TODO: unique node name tests
    //       unique with parent
    //       same name in two parents
    //       across transaction vs within transaction
    
    @Test
    public void uniqueWithinParentAcrossTransaction()
    {
        profiler.start("createNode");
        Node drive1 = transactionalStorage.createDrive("drive1");
        assertNotNull(drive1);
        Node node1 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateNode");
        Node node2 = transactionalStorage.createDrive("node2");
        assertNotNull(node2);
        transaction.applyChanges();
        profiler.stop().log();
    }
 
    @Test
    public void uniqueWithinParentWithinTransaction()
    {
        profiler.start("createNode");
        Node drive1 = transactionalStorage.createDrive("drive1");
        assertNotNull(drive1);
        Node node1 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node1);
        profiler.start("createDuplicateNode");
        Node node2 = transactionalStorage.createDrive("node2");
        assertNotNull(node2);
        transaction.applyChanges();
        profiler.stop().log();
    }

    @Test
    public void uniqueWithinParentWithinAndAcrossTransaction()
    {
        profiler.start("createNode");
        Node drive1 = transactionalStorage.createDrive("drive1");
        assertNotNull(drive1);
        Node node1 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateNodes");
        Node node2 = transactionalStorage.createDrive("node2");
        assertNotNull(node2);
        Node node3 = transactionalStorage.createDrive("node3");
        assertNotNull(node3);
        transaction.applyChanges();
        profiler.stop().log();
    }

    @Test
    public void uniqueAcrossParentAcrossTransaction()
    {
        profiler.start("createNode");
        Node drive1 = transactionalStorage.createDrive("drive1");
        assertNotNull(drive1);
        Node node1 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateNode");
        Node drive2 = transactionalStorage.createDrive("drive2");
        assertNotNull(drive2);
        Node node2 = transactionalStorage.createNode(drive2, "node1");
        assertNotNull(node2);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.stop().log();
    }

    @Test
    public void uniqueAcrossParentWithinTransaction()
    {
        profiler.start("createNode");
        Node drive1 = transactionalStorage.createDrive("drive1");
        assertNotNull(drive1);
        Node node1 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node1);
        profiler.start("createDuplicateNode");
        Node drive2 = transactionalStorage.createDrive("drive2");
        assertNotNull(drive2);
        Node node2 = transactionalStorage.createNode(drive2, "node1");
        assertNotNull(node2);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.stop().log();
    }

    @Test
    public void uniqueAcrossParentWithinAndAcrossTransaction()
    {
        profiler.start("createNode");
        Node drive1 = transactionalStorage.createDrive("drive1");
        assertNotNull(drive1);
        Node node1 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateNode");
        Node drive2 = transactionalStorage.createDrive("drive2");
        assertNotNull(drive2);
        Node node2 = transactionalStorage.createNode(drive2, "node1");
        assertNotNull(node2);
        Node drive3 = transactionalStorage.createDrive("drive3");
        assertNotNull(drive3);
        Node node3 = transactionalStorage.createNode(drive3, "node1");
        assertNotNull(node3);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.stop().log();
    }
    
    @Test
    public void duplicateWithParentAcrossTransaction()
    {
        profiler.start("createNode");
        Node drive1 = transactionalStorage.createDrive("drive1");
        assertNotNull(drive1);
        Node node1 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateNode");
        // TODO: think about resolving ids on apply changes
        drive1 = transactionalStorage.getDrive("drive1");
        assertNotNull(drive1);
        Node node2 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node2);
        profiler.start("apply");
        try
        {
            transaction.applyChanges();
            fail("Failed to catch duplicate nodes within parent");
        }
        catch(SilverBirchTransactionException e)
        {
        }
        profiler.stop().log();
    }

    @Ignore("Datomic bug: see datomic.UniqueValueWithinTransaction") @Test
    public void duplicateWithParentWithinTransaction()
    {
        profiler.start("createNode");
        Node drive1 = transactionalStorage.createDrive("drive1");
        assertNotNull(drive1);
        Node node1 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node1);
        profiler.start("createDuplicateNode");
        Node node2 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node2);
        profiler.start("apply");
        try
        {
            transaction.applyChanges();
            fail("Failed to catch duplicate nodes within parent");
        }
        catch(SilverBirchTransactionException e)
        {
        }
        profiler.stop().log();
    }

    @Test
    public void duplicateWithParentWithinAndAcrossTransaction()
    {
        profiler.start("createNode");
        Node drive1 = transactionalStorage.createDrive("drive1");
        assertNotNull(drive1);
        Node node1 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node1);
        profiler.start("apply");
        transaction.applyChanges();
        profiler.start("createDuplicateNode");
        // TODO: think about resolving ids on apply changes
        drive1 = transactionalStorage.getDrive("drive1");
        assertNotNull(drive1);
        Node node2 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node2);
        Node node3 = transactionalStorage.createNode(drive1, "node1");
        assertNotNull(node3);
        profiler.start("apply");
        try
        {
            transaction.applyChanges();
            fail("Failed to catch duplicate nodes within parent");
        }
        catch(SilverBirchTransactionException e)
        {
        }
        profiler.stop().log();
    }

}
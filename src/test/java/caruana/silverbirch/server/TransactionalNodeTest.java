package caruana.silverbirch.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Node;
import caruana.silverbirch.server.storage.GetDrive;
import caruana.silverbirch.server.storage.ListNodeChildren;
import caruana.silverbirch.server.storage.StorageImpl;
import datomic.Peer;


public class TransactionalNodeTest {
    
    private static Logger logger = LoggerFactory.getLogger(TransactionalNodeTest.class);

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
        storage.setListNodeChildren(new ListNodeChildren());
        transaction = new TransactionImpl(conn);
        transactionalStorage = new TransactionalStorage(storage, transaction);
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
    
    @Test
    public void listChildren()
    {
        profiler.start("createDrive");
        Node drive1 = transactionalStorage.createDrive("test");
        assertNotNull(drive1);
        transaction.applyChanges();
        profiler.start("listChildren");
        Node drive2 = transactionalStorage.getDrive("test");
        assertNotNull(drive2);
        List<Node> nodes1 = transactionalStorage.listChildren(drive2);
        assertNotNull(nodes1);
        assertTrue(nodes1.isEmpty());
        profiler.start("createNodes");
        Node node1 = transactionalStorage.createNode(drive2, "node1");
        assertNotNull(node1);
        Node node2 = transactionalStorage.createNode(drive2, "node2");
        assertNotNull(node2);
        transaction.applyChanges();
        profiler.start("listChildren");
        List<Node> nodes2 = transactionalStorage.listChildren(drive2);
        assertNotNull(nodes2);
        assertEquals(2, nodes2.size());
        profiler.stop().log();
    }
}
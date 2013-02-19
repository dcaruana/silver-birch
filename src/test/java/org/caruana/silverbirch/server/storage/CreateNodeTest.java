package org.caruana.silverbirch.server.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.SilverBirchException.SilverBirchValidatorException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import datomic.Peer;

public class CreateNodeTest {

    private static Logger logger = LoggerFactory.getLogger(StorageImplTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("CreateNodeTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        Peer.createDatabase(repo);
        conn = Peer.connect(repo);
    }

    @Test
    public void invalidNodeName()
    {
        profiler.start("createDrive");
        CreateDrive driveStatement = new CreateDrive(conn, "test");
        profiler.start("createInvalidNode");
        try
        {
            new CreateNode(conn, driveStatement.getDrive(), "/");
            fail("Failed to catch invalid node name");
        }
        catch(SilverBirchValidatorException e)
        {
        }
        profiler.stop().log();
    }
    
    @Test
    public void createNode()
    {
        profiler.start("createDrive");
        CreateDrive driveStatement = new CreateDrive(conn, "test");
        Node drive = driveStatement.getDrive();
        assertNotNull(drive);
        profiler.start("createNode");
        CreateNode nodeStatement = new CreateNode(conn, driveStatement.getDrive(), "node1");
        assertNotNull(nodeStatement);
        Node node = nodeStatement.getNode();
        assertNotNull(node);
        assertNotNull(node.getId());
        assertEquals(drive.getDriveId(), node.getDriveId());
        assertEquals(drive.getRootId(), node.getRootId());
        assertEquals("node1", node.getName());
        profiler.stop().log();
    }
 
}

package org.caruana.silverbirch.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.Storage;
import org.caruana.silverbirch.Transaction;
import org.caruana.silverbirch.SilverBirchException.SilverBirchValidatorException;
import org.caruana.silverbirch.server.connection.ConnectionImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class StorageImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(StorageImplTest.class);

    private String repo = "mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private ConnectionImpl conn;
    private Storage storage;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("StorageImplTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void initStorage()
    {
        Injector injector = Guice.createInjector(new SilverBirchModule());
        SilverBirchImpl silverbirch = injector.getInstance(SilverBirchImpl.class);
        boolean created = silverbirch.createRepo(repo);
        assertTrue(created);
        conn = silverbirch.internalConnect(repo);
        assertNotNull(conn);
        storage = conn.storage();
        assertNotNull(storage);
    }

    @Test
    public void invalidDriveName()
    {
        profiler.start("createDrive");
        storage.createDrive("a");
        profiler.start("createInvalidDrive");
        try
        {
            storage.createDrive("/");
            fail("Failed to catch invalid drive name");
        }
        catch(SilverBirchValidatorException e)
        {
        }
        profiler.stop().log();
    }
    
    @Test
    public void createDrive()
    {
        profiler.start("createDrive");
        Node drive = storage.createDrive("test");
        assertNotNull(drive);
        assertNotNull(drive.getDriveId());
        assertNotNull(drive.getId());
        assertEquals(drive.getDriveId(), drive.getId());
        assertEquals("test", drive.getName());
        profiler.start("hasChanges");
        assertTrue(conn.transaction().hasChanges());
        profiler.start("apply");
        conn.transaction().applyChanges();
        profiler.stop().log();
    }

    @Test
    public void getDrive()
    {
        profiler.start("getDrive");
        Node drive1 = storage.getDrive("test");
        assertNull(drive1);
        profiler.start("createDrive");
        Node drive2 = storage.createDrive("test");
        assertNotNull(drive2);
        Node drive3 = storage.getDrive("test");
        assertNull(drive3);
        Transaction.Result r = conn.transaction().applyChanges();
        Object drive2id = r.resolveId(drive2.getId());
        assertNotNull(drive2id);
        profiler.start("getDrive");
        Node drive4 = storage.getDrive("test");
        assertNotNull(drive4);
        assertEquals(drive2id, drive4.getId());
        assertEquals(drive2.getName(), drive4.getName());
        assertEquals(drive4.getDriveId(), drive4.getId());
        assertEquals(drive4.getRootId(), drive4.getId());
        profiler.stop().log();
    }

    @Test
    public void invalidNodeName()
    {
        profiler.start("createDrive");
        Node drive = storage.createDrive("test");
        profiler.start("createNode");
        storage.createNode(drive, "a");
        profiler.start("createInvalidNode");
        try
        {
            storage.createNode(drive, "/");
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
        Node drive = storage.createDrive("test");
        assertNotNull(drive);
        profiler.start("createNode");
        Node node = storage.createNode(drive, "node1");
        assertNotNull(node);
        assertNotNull(node.getId());
        assertEquals(drive.getDriveId(), node.getDriveId());
        assertEquals(drive.getRootId(), node.getRootId());
        assertEquals("node1", node.getName());
        profiler.start("hasChanges");
        assertTrue(conn.transaction().hasChanges());
        profiler.start("apply");
        conn.transaction().applyChanges();
        profiler.stop().log();
    }
    
}
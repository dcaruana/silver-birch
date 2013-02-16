package org.caruana.silverbirch.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.caruana.silverbirch.Connection;
import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.SilverBirch;
import org.caruana.silverbirch.Storage;
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
    private Connection conn;
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
        SilverBirch silverbirch = injector.getInstance(SilverBirch.class);
        boolean created = silverbirch.createRepo(repo);
        assertTrue(created);
        conn = silverbirch.connect(repo);
        assertNotNull(conn);
        storage = conn.storage();
        assertNotNull(storage);
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
        Node node = storage.getDrive("fred");
        assertEquals(node.getName(), "fred");
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
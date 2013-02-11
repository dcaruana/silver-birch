package org.caruana.silverbirch.server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.caruana.silverbirch.Connection;
import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.SilverBirch;
import org.caruana.silverbirch.Storage;
import org.caruana.silverbirch.server.SilverBirchImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;


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
        SilverBirch silverbirch = new SilverBirchImpl();
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
        assertNotNull(drive.getId());
        assertEquals("test", drive.getName());
        profiler.start("hasChanges");
        assertTrue(conn.hasChanges());
        profiler.start("apply");
        conn.applyChanges();
        profiler.stop().log();
    }
    
}
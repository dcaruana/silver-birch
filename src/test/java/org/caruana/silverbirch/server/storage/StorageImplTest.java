package org.caruana.silverbirch.server.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.SilverBirch;
import org.caruana.silverbirch.server.Bootstrap;
import org.caruana.silverbirch.server.SilverBirchModule;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import com.google.inject.Guice;
import com.google.inject.Injector;

import datomic.Peer;


public class StorageImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(StorageImplTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private StorageImpl storage;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("StorageImplTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void initStorage()
    {
        Peer.createDatabase(repo);
        conn = Peer.connect(repo);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.bootstrap(conn);
        
        Injector injector = Guice.createInjector(new SilverBirchModule());
        storage = injector.getInstance(StorageImpl.class);
    }

    @Test
    public void createDrive()
    {
        CreateDrive statement = storage.createDrive(conn, "test");
        assertNotNull(statement);
    }
    
    @Test
    public void createNode()
    {
        CreateDrive driveStatement = storage.createDrive(conn, "test");
        assertNotNull(driveStatement);
        CreateNode statement = storage.createNode(conn, driveStatement.getDrive(), "test");
        assertNotNull(statement);
    }
    
    @Test
    public void getDrive()
    {
        Node drive = storage.getDrive(conn, "test");
        assertNull(drive);
    }
   
}
package org.caruana.silverbirch.server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.caruana.silverbirch.server.connection.ConnectionImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class ConnectionImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(ConnectionImplTest.class);

    private String repo = "mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private ConnectionImpl conn;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("ConnectionImplTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void initConnection()
    {
        Injector injector = Guice.createInjector(new SilverBirchModule());
        SilverBirchImpl silverbirch = injector.getInstance(SilverBirchImpl.class);
        boolean created = silverbirch.createRepo(repo);
        assertTrue(created);
        conn = silverbirch.internalConnect(repo);
        assertNotNull(conn);
    }
    
    @Test
    public void services()
    {
        profiler.start("transaction()");
        assertNotNull(conn.transaction());
        profiler.start("storage()");
        assertNotNull(conn.storage());
        profiler.stop().log();
    }

}
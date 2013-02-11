package org.caruana.silverbirch.server;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.caruana.silverbirch.util.DatomicImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import datomic.Peer;
import datomic.Util;


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
        SilverBirchImpl silverbirch = new SilverBirchImpl();
        boolean created = silverbirch.createRepo(repo);
        assertTrue(created);
        conn = silverbirch.internalConnect(repo);
        assertNotNull(conn);
    }
    
    @Test
    public void noCommands()
    {
        profiler.start("hasChanges");
        assertFalse(conn.hasChanges());
        profiler.start("applyChanges");
        conn.applyChanges();
        profiler.start("hasChanges");
        assertFalse(conn.hasChanges());
        profiler.stop().log();
    }

    @Test
    public void applyCommands()
    {
        profiler.start("hasChanges");
        assertFalse(conn.hasChanges());
        profiler.start("addCommand");
        conn.addCommand(new TestCommand());
        profiler.start("hasChanges");
        assertTrue(conn.hasChanges());
        profiler.start("applyChanges");
        conn.applyChanges();
        profiler.start("hasChanges");
        assertFalse(conn.hasChanges());
        profiler.stop().log();
    }

    private static class TestCommand implements Command
    {
        @Override
        public List data()
        {
            Map m = Util.map(
                        DatomicImpl.DB_ID, Peer.tempid(DatomicImpl.DB_PARTITION_USER),
                        StorageImpl.NODE_NAME, "test" + System.currentTimeMillis()
                    );
            return Util.list(m);
        }
    }

}
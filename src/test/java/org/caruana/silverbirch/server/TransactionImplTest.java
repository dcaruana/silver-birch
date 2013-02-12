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


public class TransactionImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(TransactionImplTest.class);

    private String repo = "mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private ConnectionImpl conn;
    private TransactionImpl transaction;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("TransactionImplTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void initTransaction()
    {
        SilverBirchImpl silverbirch = new SilverBirchImpl();
        boolean created = silverbirch.createRepo(repo);
        assertTrue(created);
        conn = silverbirch.internalConnect(repo);
        assertNotNull(conn);
        transaction = new TransactionImpl();
    }
    
    @Test
    public void noCommands()
    {
        profiler.start("hasChanges");
        assertFalse(transaction.hasChanges());
        profiler.start("applyChanges");
        transaction.applyChanges(conn);
        profiler.start("hasChanges");
        assertFalse(transaction.hasChanges());
        profiler.stop().log();
    }

    @Test
    public void applyCommands()
    {
        profiler.start("hasChanges");
        assertFalse(transaction.hasChanges());
        profiler.start("addCommand");
        transaction.addStatement(new TestCommand());
        profiler.start("hasChanges");
        assertTrue(transaction.hasChanges());
        profiler.start("applyChanges");
        transaction.applyChanges(conn);
        profiler.start("hasChanges");
        assertFalse(transaction.hasChanges());
        profiler.stop().log();
    }

    private static class TestCommand implements Statement
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
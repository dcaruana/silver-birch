package org.caruana.silverbirch.server;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.caruana.silverbirch.SilverBirchException.SilverBirchTransactionException;
import org.caruana.silverbirch.statements.Statement;
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

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
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
        Peer.createDatabase(repo);
        datomic.Connection datomic = Peer.connect(repo);
        conn = new ConnectionImpl(datomic);
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
    public void applyFailure()
    {
        profiler.start("hasChanges");
        assertFalse(transaction.hasChanges());
        profiler.start("addCommand");
        transaction.addStatement(new BadCommand());
        profiler.start("hasChanges");
        assertTrue(transaction.hasChanges());
        profiler.start("applyChanges");
        try
        {
            transaction.applyChanges(conn);
            fail("Failed to throw transaction exception");
        }
        catch(SilverBirchTransactionException e)
        {
        }
        profiler.start("hasChanges");
        assertTrue(transaction.hasChanges());
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
                        DatomicImpl.DB_ID, Peer.tempid(DatomicImpl.DB_PARTITION_USER)
                    );
            return Util.list(m);
        }
    }

    private static class BadCommand implements Statement
    {
        @Override
        public List data()
        {
            return Util.list(Util.map(":test/invalid", "test"));
        }
    }

}
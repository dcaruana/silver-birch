package org.caruana.silverbirch.server;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.caruana.silverbirch.SilverBirchException.SilverBirchTransactionException;
import org.caruana.silverbirch.Transaction;
import org.caruana.silverbirch.server.schema.Schema;
import org.caruana.silverbirch.server.schema.TestData;
import org.caruana.silverbirch.server.statement.Statement;
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
        datomic.Connection conn = Peer.connect(repo);
        TestData bootstrap = new TestData();
        bootstrap.bootstrap(conn);
        transaction = new TransactionImpl(conn);
    }
    
    @Test
    public void noCommands()
    {
        profiler.start("hasChanges");
        assertFalse(transaction.hasChanges());
        profiler.start("applyChanges");
        transaction.applyChanges();
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
            transaction.applyChanges();
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
        transaction.addStatement(new NewCommand());
        profiler.start("hasChanges");
        assertTrue(transaction.hasChanges());
        profiler.start("applyChanges");
        transaction.applyChanges();
        profiler.start("hasChanges");
        assertFalse(transaction.hasChanges());
        profiler.stop().log();
    }
    
    @Test
    public void resolveTempId()
    {
        profiler.start("newCommand");
        NewCommand cmd = new NewCommand();
        Object tempId = cmd.getId();
        transaction.addStatement(cmd);
        Transaction.Result r = transaction.applyChanges();
        profiler.start("resolveTempId");
        Object id1 = r.resolveId(Peer.tempid(Schema.DB_PARTITION_USER));
        assertNotNull(r);
        assertNull(id1);
        Object id2 = r.resolveId(tempId);
        assertNotNull(id2);
        profiler.stop().log();
    }

    private static class NewCommand implements Statement
    {
        private Object id;
        
        public NewCommand()
        {
            id = Peer.tempid(Schema.DB_PARTITION_USER);
        }
        
        public Object getId()
        {
            return id;
        }
        
        @Override
        public List data()
        {
            Map m = Util.map(Schema.DB_ID, id, TestData.TEST_NAME, "test");
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
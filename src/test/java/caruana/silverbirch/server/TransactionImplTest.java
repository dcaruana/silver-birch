package caruana.silverbirch.server;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.SilverBirchException.SilverBirchTransactionException;
import caruana.silverbirch.Transaction;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.Schema;
import caruana.silverbirch.server.schema.TestData;
import caruana.silverbirch.server.statement.Statement;
import datomic.Peer;
import datomic.Util;


public class TransactionImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(TransactionImplTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private TransactionImpl transaction;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(TransactionImplTest.class.getSimpleName());
        profiler.setLogger(logger);
    }
    
    @Before
    public void initTransaction()
    {
        InMemoryRepoStore repoStore = new InMemoryRepoStore();
        repoStore.create(repo);
        datomic.Connection conn = repoStore.connect(repo);
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
        transaction.addStatement(new BadCommandStatement());
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
        transaction.addStatement(new NewCommandStatement());
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
        NewCommandStatement cmd = new NewCommandStatement();
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

    @Test
    public void transactionId()
    {
        profiler.start("newCommand");
        NewCommandStatement cmd = new NewCommandStatement();
        transaction.addStatement(cmd);
        Transaction.Result r = transaction.applyChanges();
        profiler.start("transactionId");
        Object trxId1 = r.getTransactionId();
        assertNotNull(trxId1);
        assertTrue(Long.class.isInstance(trxId1));
        profiler.start("newCommand2");
        NewCommandStatement cmd2 = new NewCommandStatement();
        transaction.addStatement(cmd2);
        Transaction.Result r2 = transaction.applyChanges();
        profiler.start("transactionId2");
        Object trxId2 = r2.getTransactionId();
        assertNotNull(trxId2);
        assertTrue(Long.class.isInstance(trxId2));
        assertNotEquals(trxId1, trxId2);
        profiler.stop().log();
    }
    
    
    private static class NewCommandStatement implements Statement
    {
        private Object id;
        
        public NewCommandStatement()
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
        
        @Override
        public List log()
        {
            return null;
        }
    }

    private static class BadCommandStatement implements Statement
    {
        @Override
        public List data()
        {
            return Util.list(Util.map(":test/invalid", "test"));
        }
        
        @Override
        public List log()
        {
            return null;
        }
    }

}
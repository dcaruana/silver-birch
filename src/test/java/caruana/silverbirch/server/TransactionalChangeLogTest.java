package caruana.silverbirch.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Change;
import caruana.silverbirch.Transaction.Result;
import caruana.silverbirch.server.log.ChangeLogImpl;
import caruana.silverbirch.server.log.GetChangeLogQuery;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.TestData;


public class TransactionalChangeLogTest {
    
    private static Logger logger = LoggerFactory.getLogger(TransactionalChangeLogTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private TransactionImpl transaction;
    private TransactionalChangeLog transactionalLog;
    private Result t1;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(TransactionalChangeLogTest.class.getSimpleName());
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        InMemoryRepoStore repoStore = new InMemoryRepoStore();
        repoStore.create(repo);
        conn = repoStore.connect(repo);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.bootstrap(conn);
        TestData testData = new TestData();
        testData.bootstrap(conn);
        t1 = testData.data(conn, "get_transaction_change_log_data1.edn");
        ChangeLogImpl log = new ChangeLogImpl();
        log.setGetTransactionChangeLog(new GetChangeLogQuery());
        transaction = new TransactionImpl(conn);
        transactionalLog = new TransactionalChangeLog(log, transaction);
    }

    @Test
    public void getChangeLog()
    {
        profiler.start("getChangeLog");
        Collection<Change> changes = transactionalLog.getChanges(t1.getTransactionId());
        assertNotNull(changes);
        assertEquals(1, changes.size());
        profiler.stop().log();
    }
   
}
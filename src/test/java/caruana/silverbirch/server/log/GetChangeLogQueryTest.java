package caruana.silverbirch.server.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Transaction.Result;
import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.TestData;
import datomic.Util;

public class GetChangeLogQueryTest {

    private static Logger logger = LoggerFactory.getLogger(GetChangeLogQueryTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private GetChangeLogQuery getLog;
    private Result t1;
    private Result t2;
    private Result t3;
    private Result t4;
    

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(GetChangeLogQueryTest.class.getName());
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
        TestData data = new TestData();
        data.bootstrap(conn);
        t1 = data.data(conn, "get_transaction_change_log_data1.edn");
        t2 = data.data(conn, "get_transaction_change_log_data2.edn");
        t3 = data.data(conn, "get_transaction_change_log_data3.edn");
        t4 = data.data(conn, "get_transaction_change_log_data4.edn");
        getLog = new GetChangeLogQuery();
    }

    @Test
    public void getChangeLog1()
    {
        profiler.start("getChangeLog");
        Collection<List<Object>> results = getLog.execute(conn, t1.getTransactionId());
        assertNotNull(results);
        assertEquals(1, results.size());
        profiler.start("assertResults");
        Map<String, List> values = Util.map(":test/property", Util.list("value1"));
        Map<String, Boolean> added = Util.map("value1", true);
        assertResults(results, "test_statement1", values, added);
        profiler.stop().log();
    }

    
    @Test
    public void getChangeLog2()
    {
        profiler.start("getChangeLog");
        Collection<List<Object>> results = getLog.execute(conn, t2.getTransactionId());
        assertNotNull(results);
        assertEquals(2, results.size());
        profiler.start("assertResults");
        Map<String, List> values = Util.map(":test/property", Util.list("value2"), ":test/name", Util.list("name2"));
        Map<String, Boolean> added = Util.map("value2", true, "name2", true);
        assertResults(results, "test_statement2", values, added);
        profiler.stop().log();
    }

    @Test
    public void getChangeLog3()
    {
        profiler.start("getChangeLog");
        Collection<List<Object>> results = getLog.execute(conn, t3.getTransactionId());
        assertNotNull(results);
        assertEquals(2, results.size());
        profiler.start("assertResults");
        Map<String, List> values = Util.map(":test/property", Util.list("value3"), ":test.log/property", Util.list("customvalue3"));
        Map<String, Boolean> added = Util.map("value3", true, "customvalue3", true);
        assertResults(results, "test_statement3", values, added);
        profiler.stop().log();
    }

    @Test
    public void getChangeLog4()
    {
        profiler.start("getChangeLog");
        Collection<List<Object>> results = getLog.execute(conn, t4.getTransactionId());
        assertNotNull(results);
        assertEquals(2, results.size());
        profiler.start("assertResults");
        Map<String, List> values = Util.map(":test/property", Util.list("value1", "value1_updated"));
        Map<String, Boolean> added = Util.map("value1", false, "value1_updated", true);
        assertResults(results, "test_statement4", values, added);
        profiler.stop().log();
    }

    private void assertResults(Collection<List<Object>> results, String statement, Map<String, List> values, Map<String, Boolean> added)
    {
        Object prevSubject = null;
        for (List<Object> result : results)
        {
            assertEquals(statement, result.get(0));
            Object subject = result.get(1);
            assertNotNull(subject);
            if (prevSubject != null) assertEquals(prevSubject, subject);
            assertTrue(values.containsKey(result.get(2)));
            assertTrue(values.get(result.get(2)).contains(result.get(3)));
            assertEquals(added.get(result.get(3)), result.get(4));
            prevSubject = subject;
        }
    }
}

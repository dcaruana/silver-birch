package caruana.silverbirch.server.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Change;
import caruana.silverbirch.Change.Property;
import caruana.silverbirch.Transaction.Result;
import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.SilverBirchModule;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.TestData;

import com.google.inject.Guice;
import com.google.inject.Injector;

import datomic.Util;


public class ChangeLogImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(ChangeLogImplTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private ChangeLogImpl changeLog;
    private Result t1;
    private Result t2;
    private Result t3;
    private Result t4;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("ChangeLogImplTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void initItems()
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
        Injector injector = Guice.createInjector(new SilverBirchModule());
        changeLog = injector.getInstance(ChangeLogImpl.class);
    }

    @Test
    public void createEntry()
    {
        profiler.start("createEntry");
        List log = changeLog.createEntry("test_statement", new Object(), Util.list("one", "two"));
        assertNotNull(log);
        assertEquals(1, log.size());
        profiler.stop().log();
    }

    @Test
    public void createEntryCustomAttrs()
    {
        profiler.start("createEntry");
        Map customAttrs = Util.map("three", "threeVal", "four", "fourVal");
        List log = changeLog.createEntry("test_statement", new Object(), Util.list("one", "two"), customAttrs);
        assertNotNull(log);
        assertEquals(2, log.size());
        profiler.stop().log();
    }
   
    @Test
    public void getChangeLogTransactionNotExist()
    {
        profiler.start("getChangeLog");
        Collection<Change> changes = changeLog.getChangeLog(conn, 1);
        assertNotNull(changes);
        assertEquals(0, changes.size());
        profiler.stop().log();
    }
    
    @Test
    public void getChangeLog1()
    {
        profiler.start("getChangeLog");
        Collection<Change> changes = changeLog.getChangeLog(conn, t1.getTransactionId());
        assertNotNull(changes);
        assertEquals(1, changes.size());
        profiler.start("assertChange");
        Change change = changes.iterator().next();
        assertNotNull(change.getStatement());
        assertEquals("test_statement1", change.getStatement());
        assertNotNull(change.getSubject());
        Map<String, Property> props = change.getProperties();
        assertNotNull(props);
        assertEquals(1, props.size());
        Property prop = props.get(":test/property");
        assertNotNull(prop);
        assertEquals("value1", prop.getValue());
        assertNull(prop.getPrev());
        profiler.stop().log();
   }

    @Test
    public void getChangeLog2()
    {
        profiler.start("getChangeLog");
        Collection<Change> changes = changeLog.getChangeLog(conn, t2.getTransactionId());
        assertNotNull(changes);
        assertEquals(1, changes.size());
        profiler.start("assertChange");
        Change change = changes.iterator().next();
        assertNotNull(change.getStatement());
        assertEquals("test_statement2", change.getStatement());
        assertNotNull(change.getSubject());
        Map<String, Property> props = change.getProperties();
        assertNotNull(props);
        assertEquals(2, props.size());
        Property prop1 = props.get(":test/property");
        assertNotNull(prop1);
        assertEquals("value2", prop1.getValue());
        assertNull(prop1.getPrev());
        Property prop2 = props.get(":test/name");
        assertNotNull(prop2);
        assertEquals("name2", prop2.getValue());
        assertNull(prop2.getPrev());
        profiler.stop().log();
   }

    @Test
    public void getChangeLog3()
    {
        profiler.start("getChangeLog");
        Collection<Change> changes = changeLog.getChangeLog(conn, t3.getTransactionId());
        assertNotNull(changes);
        assertEquals(1, changes.size());
        profiler.start("assertChange");
        Change change = changes.iterator().next();
        assertNotNull(change.getStatement());
        assertEquals("test_statement3", change.getStatement());
        assertNotNull(change.getSubject());
        Map<String, Property> props = change.getProperties();
        assertNotNull(props);
        assertEquals(2, props.size());
        Property prop1 = props.get(":test/property");
        assertNotNull(prop1);
        assertEquals("value3", prop1.getValue());
        assertNull(prop1.getPrev());
        Property prop2 = props.get(":test.log/property");
        assertNotNull(prop2);
        assertEquals("customvalue3", prop2.getValue());
        assertNull(prop2.getPrev());
        profiler.stop().log();
   }

    @Test
    public void getChangeLog4()
    {
        profiler.start("getChangeLog");
        Collection<Change> changes = changeLog.getChangeLog(conn, t4.getTransactionId());
        assertNotNull(changes);
        assertEquals(1, changes.size());
        profiler.start("assertChange");
        Change change = changes.iterator().next();
        assertNotNull(change.getStatement());
        assertEquals("test_statement4", change.getStatement());
        assertNotNull(change.getSubject());
        Map<String, Property> props = change.getProperties();
        assertNotNull(props);
        assertEquals(1, props.size());
        Property prop1 = props.get(":test/property");
        assertNotNull(prop1);
        assertEquals("value1_updated", prop1.getValue());
        assertEquals("value1", prop1.getPrev());
        profiler.stop().log();
   }

}
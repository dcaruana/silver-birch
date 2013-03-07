package caruana.silverbirch.server.log;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Change;
import caruana.silverbirch.Change.Property;
import caruana.silverbirch.ChangeLog;
import caruana.silverbirch.Item;
import caruana.silverbirch.Transaction.Result;
import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.TransactionImpl;
import caruana.silverbirch.server.TransactionalChangeLog;
import caruana.silverbirch.server.TransactionalItems;
import caruana.silverbirch.server.items.CreateDriveStatement;
import caruana.silverbirch.server.items.GetDriveQuery;
import caruana.silverbirch.server.items.GetPropertiesQuery;
import caruana.silverbirch.server.items.ItemsImpl;
import caruana.silverbirch.server.items.ListItemChildrenQuery;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.Schema;
import caruana.silverbirch.server.schema.TestData;
import datomic.Util;


public class ItemsChangeLogTest {
    
    private static Logger logger = LoggerFactory.getLogger(ItemsChangeLogTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private TransactionImpl transaction;
    private TransactionalItems transactionalItems;
    private TransactionalChangeLog transactionalLog;
    private Result t1;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(ItemsChangeLogTest.class.getSimpleName());
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
        ItemsImpl items = new ItemsImpl();
        items.setCreateDrive(new CreateDriveStatement());
        items.setGetDrive(new GetDriveQuery());
        items.setListItemChildren(new ListItemChildrenQuery());
        items.setGetProperties(new GetPropertiesQuery());
        transaction = new TransactionImpl(conn);
        transactionalItems = new TransactionalItems(items, transaction);
        ChangeLogImpl log = new ChangeLogImpl();
        log.setGetTransactionChangeLog(new GetChangeLogQuery());
        transactionalLog = new TransactionalChangeLog(log, transaction);
    }

    @Test
    public void createDrive()
    {
        profiler.start("createDrive");
        Item drive = transactionalItems.createDrive("drive");
        assertNotNull(drive);
        Result t1 = transaction.applyChanges();
        assertNotNull(t1);
        profiler.start("getChangeLog");
        Collection<Change> changes = transactionalLog.getChanges(t1.getTransactionId());
        assertNotNull(changes);
        assertEquals(1, changes.size());
        Change change = changes.iterator().next();
        assertEquals(ChangeLog.CREATE_DRIVE, change.getStatement());
        assertEquals(t1.resolveId(drive.getId()), change.getSubject());
        Map<String, Property> props = change.getProperties();
        assertNotNull(props);
        assertEquals(2, props.size());
        Property prop1 = props.get(Schema.ITEM_NAME);
        assertNotNull(prop1);
        assertEquals("drive", prop1.getValue());
        assertNull(prop1.getPrev());
        Property prop2 = props.get(Schema.ITEM_ROOT);
        assertNotNull(prop2);
        assertEquals(t1.resolveId(drive.getRootId()), prop2.getValue());
        assertNull(prop2.getPrev());
        profiler.stop().log();
    }

    @Test
    public void createItem()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive");
        assertNotNull(drive1);
        Result t1 = transaction.applyChanges();
        assertNotNull(t1);
        Item drive2 = transactionalItems.getDrive("drive");
        assertNotNull(drive2);
        Item item = transactionalItems.createItem(drive2, "item");
        Result t2 = transaction.applyChanges();
        assertNotNull(t2);
        profiler.start("getChangeLog");
        Collection<Change> changes = transactionalLog.getChanges(t2.getTransactionId());
        assertNotNull(changes);
        assertEquals(1, changes.size());
        Change change = changes.iterator().next();
        assertEquals(ChangeLog.CREATE_ITEM, change.getStatement());
        assertEquals(t2.resolveId(item.getId()), change.getSubject());
        Map<String, Property> props = change.getProperties();
        assertNotNull(props);
        assertEquals(3, props.size());
        Property prop1 = props.get(Schema.ITEM_NAME);
        assertNotNull(prop1);
        assertEquals("item", prop1.getValue());
        assertNull(prop1.getPrev());
        Property prop2 = props.get(Schema.ITEM_ROOT);
        assertNotNull(prop2);
        assertEquals(t1.resolveId(drive1.getRootId()), prop2.getValue());
        assertNull(prop2.getPrev());
        Property prop3 = props.get(Schema.ITEM_PARENTS);
        assertNotNull(prop3);
        assertEquals(t1.resolveId(drive1.getId()), prop3.getValue());
        assertNull(prop3.getPrev());
        profiler.stop().log();
    }

    @Test
    public void setProperties()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive");
        assertNotNull(drive1);
        Result t1 = transaction.applyChanges();
        assertNotNull(t1);
        Item drive2 = transactionalItems.getDrive("drive");
        assertNotNull(drive2);
        transactionalItems.setProperties(drive2, Util.map(TestData.TEST_PROPERTY, "value1"));
        Result t2 = transaction.applyChanges();
        assertNotNull(t2);
        profiler.start("getChangeLog");
        Collection<Change> changes = transactionalLog.getChanges(t2.getTransactionId());
        assertNotNull(changes);
        assertEquals(1, changes.size());
        Change change = changes.iterator().next();
        assertEquals(ChangeLog.SET_PROPERTIES, change.getStatement());
        assertEquals(drive2.getId(), change.getSubject());
        Map<String, Property> props = change.getProperties();
        assertNotNull(props);
        assertEquals(1, props.size());
        Property prop1 = props.get(TestData.TEST_PROPERTY);
        assertNotNull(prop1);
        assertEquals("value1", prop1.getValue());
        assertNull(prop1.getPrev());
        profiler.stop().log();
    }

    @Test
    public void setPropertiesUpdate()
    {
        profiler.start("createItem");
        Item drive1 = transactionalItems.createDrive("drive");
        assertNotNull(drive1);
        Result t1 = transaction.applyChanges();
        assertNotNull(t1);
        Item drive2 = transactionalItems.getDrive("drive");
        assertNotNull(drive2);
        transactionalItems.setProperties(drive2, Util.map(TestData.TEST_PROPERTY, "value1"));
        Result t2 = transaction.applyChanges();
        assertNotNull(t2);
        transactionalItems.setProperties(drive2, Util.map(TestData.TEST_PROPERTY, "value1_updated"));
        Result t3 = transaction.applyChanges();
        assertNotNull(t3);
        profiler.start("getChangeLog");
        Collection<Change> changes = transactionalLog.getChanges(t3.getTransactionId());
        assertNotNull(changes);
        assertEquals(1, changes.size());
        Change change = changes.iterator().next();
        assertEquals(ChangeLog.SET_PROPERTIES, change.getStatement());
        assertEquals(drive2.getId(), change.getSubject());
        Map<String, Property> props = change.getProperties();
        assertNotNull(props);
        assertEquals(1, props.size());
        Property prop1 = props.get(TestData.TEST_PROPERTY);
        assertNotNull(prop1);
        assertEquals("value1_updated", prop1.getValue());
        assertEquals("value1", prop1.getPrev());
        profiler.stop().log();
    }

    
//    List attrs = Util.list(Schema.ITEM_NAME, Schema.ITEM_ROOT, Schema.ITEM_PARENTS);

}
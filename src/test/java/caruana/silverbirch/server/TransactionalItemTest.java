package caruana.silverbirch.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import com.google.inject.Guice;
import com.google.inject.Injector;

import caruana.silverbirch.Item;
import caruana.silverbirch.server.items.CreateDriveStatementFactory;
import caruana.silverbirch.server.items.CreateItemStatementFactory;
import caruana.silverbirch.server.items.GetDriveQuery;
import caruana.silverbirch.server.items.GetPropertiesQuery;
import caruana.silverbirch.server.items.ItemsImpl;
import caruana.silverbirch.server.items.ListItemChildrenQuery;
import caruana.silverbirch.server.items.SetPropertiesStatementFactory;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.TestData;
import datomic.Util;


public class TransactionalItemTest {
    
    private static Logger logger = LoggerFactory.getLogger(TransactionalItemTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private TransactionImpl transaction;
    private TransactionalItems transactionalItems;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(TransactionalItemTest.class.getSimpleName());
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
        Injector injector = Guice.createInjector(new SilverBirchModule());
        ItemsImpl items = injector.getInstance(ItemsImpl.class);
        transaction = new TransactionImpl(conn);
        transactionalItems = new TransactionalItems(items, transaction);
    }

    @Test
    public void createItem()
    {
        profiler.start("createDrive");
        Item drive = transactionalItems.createDrive("test");
        assertNotNull(drive);
        profiler.start("createItem");
        Item item = transactionalItems.createItem(drive, "item1");
        assertNotNull(item);
        assertNotNull(item.getId());
        assertEquals(drive.getDriveId(), item.getDriveId());
        assertEquals(drive.getRootId(), item.getRootId());
        assertEquals("item1", item.getName());
        profiler.start("hasChanges");
        assertTrue(transaction.hasChanges());
        profiler.start("apply");
        transaction.applyChanges();
        profiler.stop().log();
    }
    
    @Test
    public void listChildren()
    {
        profiler.start("createDrive");
        Item drive1 = transactionalItems.createDrive("test");
        assertNotNull(drive1);
        transaction.applyChanges();
        profiler.start("listChildren");
        Item drive2 = transactionalItems.getDrive("test");
        assertNotNull(drive2);
        List<Item> items1 = transactionalItems.listChildren(drive2);
        assertNotNull(items1);
        assertTrue(items1.isEmpty());
        profiler.start("createItems");
        Item item1 = transactionalItems.createItem(drive2, "item1");
        assertNotNull(item1);
        Item item2 = transactionalItems.createItem(drive2, "item2");
        assertNotNull(item2);
        transaction.applyChanges();
        profiler.start("listChildren");
        List<Item> items2 = transactionalItems.listChildren(drive2);
        assertNotNull(items2);
        assertEquals(2, items2.size());
        profiler.stop().log();
    }
    
    @Test
    public void setPropertiesWithinTransaction()
    {
        profiler.start("createDrive");
        Item drive1 = transactionalItems.createDrive("test");
        assertNotNull(drive1);
        profiler.start("setProperties");
        Map m = Util.map(TestData.TEST_PROPERTY, "value1");
        transactionalItems.setProperties(drive1, m);
        transaction.applyChanges();
        profiler.stop().log();
    }

    @Test
    public void setPropertiesAcrossTransaction()
    {
        profiler.start("createDrive");
        Item drive1 = transactionalItems.createDrive("test");
        assertNotNull(drive1);
        transaction.applyChanges();
        profiler.start("setProperties");
        Map m = Util.map(TestData.TEST_PROPERTY, "value1");
        transactionalItems.setProperties(drive1, m);
        assertTrue(transaction.hasChanges());
        transaction.applyChanges();
        profiler.stop().log();
    }
    
    @Test
    public void getProperties()
    {
        profiler.start("createDrive");
        Item drive1 = transactionalItems.createDrive("test");
        assertNotNull(drive1);
        profiler.start("setProperties");
        Map m = Util.map(TestData.TEST_PROPERTY, "value1");
        transactionalItems.setProperties(drive1, m);
        transaction.applyChanges();
        Item drive2 = transactionalItems.getDrive("test");
        assertNotNull(drive2);
        Map<String, Object> properties = transactionalItems.getProperties(drive2);
        assertNotNull(properties);
        String value = (String)properties.get(TestData.TEST_PROPERTY);
        assertNotNull(value);
        assertEquals("value1", value);
        profiler.stop().log();
    }
    
}
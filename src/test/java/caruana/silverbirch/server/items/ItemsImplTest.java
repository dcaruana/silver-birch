package caruana.silverbirch.server.items;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Item;
import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.SilverBirchModule;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.TestData;

import com.google.inject.Guice;
import com.google.inject.Injector;

import datomic.Util;


public class ItemsImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(ItemsImplTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private ItemsImpl items;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(ItemsImplTest.class.getSimpleName());
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
        
        Injector injector = Guice.createInjector(new SilverBirchModule());
        items = injector.getInstance(ItemsImpl.class);
    }

    @Test
    public void createDrive()
    {
        CreateDrive statement = items.createDrive(conn, "test");
        assertNotNull(statement);
    }

    @Test
    public void getDrive()
    {
        Item drive = items.getDrive(conn, "test");
        assertNull(drive);
    }

    @Test
    public void listDrives()
    {
        List<Item> drives = items.listDrives(conn);
        assertNotNull(drives);
        assertTrue(drives.isEmpty());
    }

    @Test
    public void createItem()
    {
        CreateDrive driveStatement = items.createDrive(conn, "test");
        assertNotNull(driveStatement);
        CreateItem statement = items.createItem(conn, driveStatement.getDrive(), "test");
        assertNotNull(statement);
    }
    
    @Test
    public void listItemChildren()
    {
        List<Item> children = items.listItemChildren(conn, new ItemData(null, null, null, 1234l, null));
        assertNotNull(children);
        assertTrue(children.isEmpty());
    }

    @Test
    public void setProperties()
    {
        CreateDrive createDrive = items.createDrive(conn, "test");
        assertNotNull(createDrive);
        Map props = Util.map(TestData.TEST_PROPERTY, "value1");
        SetProperties setProperties = items.setProperties(conn, createDrive.getDrive(), props);
        assertNotNull(setProperties);
    }
    
    @Test
    public void getProperties()
    {
        CreateDrive createDrive = items.createDrive(conn, "test");
        assertNotNull(createDrive);
        Map<String, Object> properties = items.getProperties(conn, createDrive.getDrive());
        assertNotNull(properties);
    }
    
}
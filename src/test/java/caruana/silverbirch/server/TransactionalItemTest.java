package caruana.silverbirch.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Item;
import caruana.silverbirch.server.items.GetDrive;
import caruana.silverbirch.server.items.ItemsImpl;
import caruana.silverbirch.server.items.ListItemChildren;
import caruana.silverbirch.server.repo.InMemoryRepoStore;


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
        profiler = new Profiler("TransactionalItemTest");
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
        ItemsImpl items = new ItemsImpl();
        items.setGetDrive(new GetDrive());
        items.setListItemChildren(new ListItemChildren());
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
}
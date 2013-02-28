package caruana.silverbirch.server.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.TestData;

public class ListItemChildrenTest {

    private static Logger logger = LoggerFactory.getLogger(ItemsImplTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private GetDrive getDrive;
    private ListItemChildren listItemChildren;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("ListItemChildrenTest");
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
        getDrive = new GetDrive();
        listItemChildren = new ListItemChildren();
    }

    @Test
    public void listParentNotExist()
    {
        profiler.start("listItemChildren");
        List<ItemData> children = listItemChildren.execute(conn, new ItemData(null, null, null, 1234l, null));
        assertNotNull(children);
        assertTrue(children.isEmpty());
        profiler.stop().log();
    }

    @Test
    public void listEmptyChildren()
    {
        profiler.start("createItems");
        TestData data = new TestData();
        data.data(conn, "list_item_children_data.edn");
        profiler.start("listItems");
        ItemData drive1 = getDrive.execute(conn, "drive2");
        assertNotNull(drive1);
        List<ItemData> items1 = listItemChildren.execute(conn, drive1);
        assertNotNull(items1);
        assertTrue(items1.isEmpty());
        profiler.stop().log();
    }

    @Test
    public void listChildren()
    {
        profiler.start("createItems");
        TestData data = new TestData();
        data.data(conn, "list_item_children_data.edn");
        profiler.start("listItems");
        ItemData drive1 = getDrive.execute(conn, "drive1");
        assertNotNull(drive1);
        List<ItemData> items1 = listItemChildren.execute(conn, drive1);
        assertNotNull(items1);
        assertEquals(2, items1.size());
        ItemData drive3 = getDrive.execute(conn, "drive3");
        assertNotNull(drive3);
        List<ItemData> items3 = listItemChildren.execute(conn, drive3);
        assertNotNull(items3);
        assertEquals(1, items3.size());
        profiler.stop().log();
    }
    
}

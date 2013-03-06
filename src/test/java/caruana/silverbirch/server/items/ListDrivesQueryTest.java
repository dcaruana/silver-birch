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

public class ListDrivesQueryTest {

    private static Logger logger = LoggerFactory.getLogger(ListDrivesQueryTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private ListDrivesQuery listDrives;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("ListDrivesTest");
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
        listDrives = new ListDrivesQuery();
    }

    @Test
    public void listEmptyDrives()
    {
        profiler.start("listDrives");
        List<ItemData> drives = listDrives.execute(conn);
        assertNotNull(drives);
        assertTrue(drives.isEmpty());
        profiler.stop().log();
    }
    
    @Test
    public void listDrives()
    {
        profiler.start("createDrives");
        TestData data = new TestData();
        data.data(conn, "list_drives_data.edn");
        profiler.start("listDrives");
        List<ItemData> drives = listDrives.execute(conn);
        assertNotNull(drives);
        assertEquals(3, drives.size());
        profiler.stop().log();
    }
    
}

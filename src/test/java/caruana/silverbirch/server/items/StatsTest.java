package caruana.silverbirch.server.items;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.SilverBirchModule;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.TestData;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class StatsTest {

    private static Logger logger = LoggerFactory.getLogger(ItemsImplTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private Stats stats;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("StatsTest");
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
        data.data(conn, "items_stats.edn");
        
        Injector injector = Guice.createInjector(new SilverBirchModule());
        stats = injector.getInstance(Stats.class);
    }

    @Test
    public void getDriveCount()
    {
        profiler.start("getDriveCount");
        int count = stats.getDriveCount(conn);
        assertEquals(3, count);
    }

    @Test
    public void getItemCount()
    {
        profiler.start("getItemCount");
        int count = stats.getItemCount(conn);
        assertEquals(6, count);
    }

}

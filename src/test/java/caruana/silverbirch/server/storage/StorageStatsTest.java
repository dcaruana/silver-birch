package caruana.silverbirch.server.storage;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.SilverBirchModule;
import caruana.silverbirch.server.schema.TestData;

import com.google.inject.Guice;
import com.google.inject.Injector;

import datomic.Peer;

public class StorageStatsTest {

    private static Logger logger = LoggerFactory.getLogger(StorageImplTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private StorageStats storageStats;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("StorageStatsTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        Peer.createDatabase(repo);
        conn = Peer.connect(repo);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.bootstrap(conn);
        TestData data = new TestData();
        data.data(conn, "storage_stats.edn");
        
        Injector injector = Guice.createInjector(new SilverBirchModule());
        storageStats = injector.getInstance(StorageStats.class);
    }

    @Test
    public void getDriveCount()
    {
        profiler.start("getDriveCount");
        int count = storageStats.getDriveCount(conn);
        assertEquals(3, count);
    }

    @Test
    public void getNodeCount()
    {
        profiler.start("getNodeCount");
        int count = storageStats.getNodeCount(conn);
        assertEquals(6, count);
    }

}

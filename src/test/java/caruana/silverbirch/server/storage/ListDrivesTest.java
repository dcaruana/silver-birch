package caruana.silverbirch.server.storage;

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
import caruana.silverbirch.server.schema.TestData;
import datomic.Peer;

public class ListDrivesTest {

    private static Logger logger = LoggerFactory.getLogger(StorageImplTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private ListDrives listDrives;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("ListDrivesTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        Peer.createDatabase(repo);
        conn = Peer.connect(repo);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.bootstrap(conn);
        listDrives = new ListDrives();
    }

    @Test
    public void listEmptyDrives()
    {
        profiler.start("listDrives");
        List<NodeData> drives = listDrives.execute(conn);
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
        List<NodeData> drives = listDrives.execute(conn);
        assertNotNull(drives);
        assertEquals(3, drives.size());
        profiler.stop().log();
    }
    
}

package org.caruana.silverbirch.server.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.server.Bootstrap;
import org.caruana.silverbirch.server.schema.TestData;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import datomic.Peer;

public class GetDriveTest {

    private static Logger logger = LoggerFactory.getLogger(StorageImplTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private GetDrive getDrive;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("GetDriveTest");
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
        data.data(conn, "get_drive.edn");
        getDrive = new GetDrive();
    }

    @Test
    public void getDriveNotExist()
    {
        profiler.start("getDrive");
        Node drive = getDrive.execute(conn, "test" + System.currentTimeMillis());
        assertNull(drive);
        profiler.stop().log();
    }
    
    @Test
    public void getDrive()
    {
        profiler.start("getDrive");
        Node drive = getDrive.execute(conn, "test");
        assertNotNull(drive);
        assertNotNull(drive.getId());
        assertEquals(drive.getName(), "test");
        assertEquals(drive.getDriveId(), drive.getId());
        assertEquals(drive.getRootId(), drive.getId());
        profiler.stop().log();
    }
    
}

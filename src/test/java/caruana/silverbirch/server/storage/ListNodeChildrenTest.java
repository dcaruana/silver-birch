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

public class ListNodeChildrenTest {

    private static Logger logger = LoggerFactory.getLogger(StorageImplTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private GetDrive getDrive;
    private ListNodeChildren listNodeChildren;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("ListNodeChildrenTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        Peer.createDatabase(repo);
        conn = Peer.connect(repo);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.bootstrap(conn);
        getDrive = new GetDrive();
        listNodeChildren = new ListNodeChildren();
    }

    @Test
    public void listParentNotExist()
    {
        profiler.start("listNodeChildren");
        List<NodeData> children = listNodeChildren.execute(conn, new NodeData(null, null, null, 1234l, null));
        assertNotNull(children);
        assertTrue(children.isEmpty());
        profiler.stop().log();
    }

    @Test
    public void listEmptyChildren()
    {
        profiler.start("createNodes");
        TestData data = new TestData();
        data.data(conn, "list_node_children_data.edn");
        profiler.start("listNodes");
        NodeData drive1 = getDrive.execute(conn, "drive2");
        assertNotNull(drive1);
        List<NodeData> nodes1 = listNodeChildren.execute(conn, drive1);
        assertNotNull(nodes1);
        assertTrue(nodes1.isEmpty());
        profiler.stop().log();
    }

    @Test
    public void listChildren()
    {
        profiler.start("createNodes");
        TestData data = new TestData();
        data.data(conn, "list_node_children_data.edn");
        profiler.start("listNodes");
        NodeData drive1 = getDrive.execute(conn, "drive1");
        assertNotNull(drive1);
        List<NodeData> nodes1 = listNodeChildren.execute(conn, drive1);
        assertNotNull(nodes1);
        assertEquals(2, nodes1.size());
        NodeData drive3 = getDrive.execute(conn, "drive3");
        assertNotNull(drive3);
        List<NodeData> nodes3 = listNodeChildren.execute(conn, drive3);
        assertNotNull(nodes3);
        assertEquals(1, nodes3.size());
        profiler.stop().log();
    }
    
}

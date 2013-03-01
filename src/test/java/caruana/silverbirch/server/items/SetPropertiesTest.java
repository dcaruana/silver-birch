package caruana.silverbirch.server.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Item;
import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.TestData;
import datomic.Peer;
import datomic.Util;

public class SetPropertiesTest {

    private static Logger logger = LoggerFactory.getLogger(SetPropertiesTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("SetPropertiesTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        InMemoryRepoStore repoStore = new InMemoryRepoStore();
        repoStore.create(repo);
        conn = repoStore.connect(repo);
    }

    @Test
    public void setProperties()
    {
        profiler.start("setProperties");
        Map m = Util.map(TestData.TEST_PROPERTY, "value1");
        ItemData item = new ItemData(Peer.squuid(), 1l, 1l, 1l, "test");
        SetProperties setProperties = new SetProperties(conn, item, m);
        assertNotNull(setProperties.data());
        profiler.stop().log();
    }

}

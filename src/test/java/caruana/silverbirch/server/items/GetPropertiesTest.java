package caruana.silverbirch.server.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.Schema;
import caruana.silverbirch.server.schema.TestData;
import datomic.Peer;

public class GetPropertiesTest {

    private static Logger logger = LoggerFactory.getLogger(ItemsImplTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private GetProperties getProperties;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("GetPropertiesTest");
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
        TestData testData = new TestData();
        testData.bootstrap(conn);
        testData.data(conn, "get_properties_data.edn");
        getProperties = new GetProperties();
    }

    @Test
    public void getPropertiesNotExist()
    {
        profiler.start("getProperties");
        Object notExist = Peer.tempid(Schema.DB_PARTITION_USER, 0);
        Map<String, Object> properties = getProperties.execute(conn, notExist);
        assertNotNull(properties);
        assertEquals(0, properties.size());
        profiler.stop().log();
    }

    @Test
    public void getProperties()
    {
        profiler.start("getProperties");
        Object notExist = Peer.tempid(Schema.DB_PARTITION_USER, 1);
        Map<String, Object> properties = getProperties.execute(conn, notExist);
        assertNotNull(properties);
        String value = (String)properties.get(TestData.TEST_PROPERTY);
        assertNotNull(value);
        assertEquals("value1", value);
        profiler.stop().log();
    }

}

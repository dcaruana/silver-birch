package caruana.silverbirch.server.items;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.server.blobs.BlobData;
import caruana.silverbirch.server.items.SetPropertiesStatementFactory.SetPropertiesStatement;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.Schema;
import caruana.silverbirch.server.schema.TestData;
import datomic.Peer;
import datomic.Util;

public class SetPropertiesTest {

    private static Logger logger = LoggerFactory.getLogger(SetPropertiesTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private SetPropertiesStatementFactory setProperties;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(SetPropertiesTest.class.getSimpleName());
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        InMemoryRepoStore repoStore = new InMemoryRepoStore();
        repoStore.create(repo);
        conn = repoStore.connect(repo);
        setProperties = new SetPropertiesStatementFactory();
    }

    @Test
    public void setProperties()
    {
        profiler.start("setProperties");
        Map m = Util.map(TestData.TEST_PROPERTY, "value1");
        ItemData item = new ItemData(Peer.squuid(), 1l, 1l, 1l, "test");
        SetPropertiesStatement statement = setProperties.statement(item, m);
        assertNotNull(statement.data());
        profiler.stop().log();
    }
    
    @Test
    public void setBlob()
    {
        profiler.start("setBlob");
        BlobData blob = new BlobData(1l, Peer.squuid(), 1l, "plain/text");
        Map m = Util.map(Schema.ITEM_CONTENT, blob);
        ItemData item = new ItemData(Peer.squuid(), 2l, 2l, 2l, "test");
        SetPropertiesStatement statement = setProperties.statement(item, m);
        assertNotNull(statement.data());
        profiler.stop().log();
    }

}

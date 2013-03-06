package caruana.silverbirch.server.blobs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Blob;
import caruana.silverbirch.Transaction.Result;
import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.Schema;
import caruana.silverbirch.server.schema.TestData;
import datomic.Peer;

public class GetBlobQueryTest {

    private static Logger logger = LoggerFactory.getLogger(GetBlobQueryTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private GetBlobQuery getBlob;
    private Object blobId;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("GetBlobTest");
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
        Result result = data.data(conn, "get_blob_data.edn");
        blobId = result.resolveId(Peer.tempid(Schema.DB_PARTITION_USER, -1));
        getBlob = new GetBlobQuery();
    }

    @Test
    public void getBlobNotExist()
    {
        profiler.start("getBlob");
        Blob blob = getBlob.execute(conn, 0);
        assertNull(blob);
        profiler.stop().log();
    }
    
    @Test
    public void getBlob()
    {
        profiler.start("getBlob");
        Blob blob = getBlob.execute(conn, blobId);
        assertNotNull(blob);
        assertNotNull(blob.getId());
        assertEquals(blob.getStreamId(), UUID.fromString("f81d4fae-7dec-11d0-a765-00a0c91e6bf6"));
        assertEquals(blob.getLength(), 12345l);
        assertEquals(blob.getMimetype(), "text/plain");
        profiler.stop().log();
    }
    
}

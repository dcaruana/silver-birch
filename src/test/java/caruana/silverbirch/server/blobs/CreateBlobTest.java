package caruana.silverbirch.server.blobs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Blob;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import datomic.Peer;


public class CreateBlobTest {

    private static Logger logger = LoggerFactory.getLogger(CreateBlobTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(CreateBlobTest.class.getSimpleName());
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
    public void createBlob()
    {
        profiler.start("createBlob");
        UUID uuid = Peer.squuid();
        long length = 100;
        String mimetype = "text/plain";
        Stream stream = new Stream(uuid, length);
        CreateBlob statement = new CreateBlob(conn, stream, mimetype);
        Blob blob = statement.getBlob();
        assertNotNull(blob);
        assertNotNull(blob.getId());
        assertNotNull(blob.getStreamId());
        assertEquals(length, blob.getLength());
        assertEquals(mimetype, blob.getMimetype());
        profiler.stop().log();
    }

}

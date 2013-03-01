package caruana.silverbirch.server.blobs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Blob;
import caruana.silverbirch.server.Bootstrap;
import caruana.silverbirch.server.SilverBirchModule;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.Schema;

import com.google.inject.Guice;
import com.google.inject.Injector;

import datomic.Peer;


public class BlobsImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(BlobsImplTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private BlobsImpl blobs;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("BlobsImplTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void initItems()
    {
        InMemoryRepoStore repoStore = new InMemoryRepoStore();
        repoStore.create(repo);
        conn = repoStore.connect(repo);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.bootstrap(conn);
        
        Injector injector = Guice.createInjector(new SilverBirchModule());
        blobs = injector.getInstance(BlobsImpl.class);
    }

    @Test
    public void createBlob()
        throws IOException
    {
        byte[] streambytes = IOUtils.toByteArray(new StringReader("stream"), "UTF-8");
        CreateBlob statement = blobs.create(conn, new ByteArrayInputStream(streambytes), "text/plain");
        assertNotNull(statement);
    }

    @Test
    public void getBlob()
    {
        Blob blob = blobs.get(conn, Peer.tempid(Schema.DB_PARTITION_USER, 1));
        assertNull(blob);
    }

    @Test
    public void getStream()
    {
        InputStream stream = blobs.read(Peer.squuid());
        assertNull(stream);
    }

}
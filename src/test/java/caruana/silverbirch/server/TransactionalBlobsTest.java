package caruana.silverbirch.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import caruana.silverbirch.Transaction.Result;
import caruana.silverbirch.server.blobs.BlobsImpl;
import caruana.silverbirch.server.blobs.GetBlobQuery;
import caruana.silverbirch.server.blobs.InMemoryBlobStore;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.schema.Schema;
import datomic.Peer;


public class TransactionalBlobsTest {
    
    private static Logger logger = LoggerFactory.getLogger(TransactionalBlobsTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private TransactionImpl transaction;
    private TransactionalBlobs transactionalBlobs;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(TransactionalBlobsTest.class.getSimpleName());
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
        BlobsImpl blobs = new BlobsImpl();
        blobs.setGetBlob(new GetBlobQuery());
        blobs.setBlobStore(new InMemoryBlobStore());
        transaction = new TransactionImpl(conn);
        transactionalBlobs = new TransactionalBlobs(blobs, transaction);
    }

    @Test
    public void getBlobNotExist()
    {
        Blob blob = transactionalBlobs.get(Peer.tempid(Schema.DB_PARTITION_USER, 1));
        assertNull(blob);
    }
    
    @Test
    public void createBlob()
        throws IOException
    {
        profiler.start("createBlob");
        Blob blob = transactionalBlobs.create(createStream("stream1"), "plain/text");
        assertNotNull(blob);
        assertNotNull(blob.getId());
        assertNotNull(blob.getStreamId());
        assertEquals("plain/text", blob.getMimetype());
        assertTrue(blob.getLength() > 0);
        profiler.start("hasChanges");
        assertTrue(transaction.hasChanges());
        profiler.start("apply");
        transaction.applyChanges();
        profiler.stop().log();
    }
    
    @Test
    public void getBlob()
        throws IOException
    {
        profiler.start("createBlob");
        Blob blob1 = transactionalBlobs.create(createStream("stream1"), "plain/text");
        assertNotNull(blob1);
        profiler.start("apply");
        Result result = transaction.applyChanges();
        profiler.start("getBlob");
        Object blob1Id = result.resolveId(blob1.getId());
        assertNotNull(blob1Id);
        Blob blob2 = transactionalBlobs.get(blob1Id);
        assertNotNull(blob2);
        assertEquals(blob2.getStreamId(), blob1.getStreamId());
        assertEquals(blob2.getLength(), blob1.getLength());
        assertEquals(blob2.getMimetype(), blob1.getMimetype());
        profiler.stop().log();
    }

    @Test
    public void getStream()
        throws IOException
    {
        profiler.start("createBlob");
        Blob blob1 = transactionalBlobs.create(createStream("stream1"), "plain/text");
        assertNotNull(blob1);
        profiler.start("apply");
        Result result = transaction.applyChanges();
        profiler.start("getBlob");
        Object blob1Id = result.resolveId(blob1.getId());
        assertNotNull(blob1Id);
        Blob blob2 = transactionalBlobs.get(blob1Id);
        assertNotNull(blob2);
        profiler.start("getStream");
        InputStream stream = transactionalBlobs.read(blob2.getStreamId());
        assertNotNull(stream);
        String content = IOUtils.toString(stream, "UTF-8");
        assertNotNull(content);
        assertEquals("stream1", content);
        profiler.stop().log();
    }

    private InputStream createStream(String content)
        throws IOException
    {
        byte[] stream = IOUtils.toByteArray(new StringReader(content), "UTF-8");
        return new ByteArrayInputStream(stream);
    }
    
}
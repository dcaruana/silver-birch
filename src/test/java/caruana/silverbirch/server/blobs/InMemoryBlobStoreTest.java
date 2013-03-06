package caruana.silverbirch.server.blobs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import datomic.Peer;

public class InMemoryBlobStoreTest
{
    private static Logger logger = LoggerFactory.getLogger(InMemoryBlobStoreTest.class);

    private Profiler profiler;
    private InMemoryBlobStore store;

    
    @Before
    public void initProfiler()
    {
        profiler = new Profiler(InMemoryBlobStoreTest.class.getSimpleName());
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        store = new InMemoryBlobStore();
    }

    @Test
    public void readNotExist()
    {
        profiler.start("readStream");
        assertNull(store.read(Peer.squuid()));
        profiler.stop().log();
    }

//    public void writeNull()
//    {
//        
//    }
    
    @Test
    public void writeRead()
        throws IOException
    {
        profiler.start("writeStream");
        Stream stream1 = writeStream("stream1");
        profiler.start("writeStream");
        Stream stream2 = writeStream("stream2");
        profiler.start("readStream");
        String stream1read = readStream(stream1.getStreamId());
        assertEquals("stream1", stream1read);
        profiler.start("readStream");
        String stream2read = readStream(stream2.getStreamId());
        assertEquals("stream2", stream2read);
        profiler.stop().log();
    }

    private Stream writeStream(String content)
        throws IOException
    {
        byte[] streambytes = IOUtils.toByteArray(new StringReader(content), "UTF-8");
        Stream stream = store.write(new ByteArrayInputStream(streambytes));
        assertNotNull(stream);
        assertNotNull(stream.getStreamId());
        assertEquals(streambytes.length, stream.getLength());
        return stream;
    }
    
    private String readStream(UUID stream)
        throws IOException
    {
        InputStream blob = store.read(stream);
        assertNotNull(blob);
        String content = IOUtils.toString(blob, "UTF-8");
        assertNotNull(content);
        return content;
    }
}

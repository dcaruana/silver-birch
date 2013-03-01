package caruana.silverbirch.server.blobs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import caruana.silverbirch.SilverBirchException.SilverBirchBlobException;
import datomic.Peer;

public class InMemoryBlobStore implements BlobStore
{
    private Map<UUID, byte[]> blobs = new HashMap<UUID, byte[]>();
    
    @Override
    public Stream write(InputStream stream)
    {
        UUID uuid = Peer.squuid();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try
        {
            IOUtils.copy(stream, output);
        }
        catch(IOException e)
        {
            throw new SilverBirchBlobException(e);
        }
        blobs.put(uuid, output.toByteArray());
        return new Stream(uuid, output.size());
    }
    
    @Override
    public InputStream read(UUID stream)
    {
        byte[] bytes = blobs.get(stream);
        if (bytes == null)
        {
            return null;
        }
        return new ByteArrayInputStream(bytes);
    }
}

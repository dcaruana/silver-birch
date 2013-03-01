package caruana.silverbirch.server.blobs;

import java.util.UUID;

import caruana.silverbirch.Blob;


public class BlobData implements Blob
{
    private Object id;
    private UUID streamId;
    private long length;
    private String mimetype;

    public BlobData(Object id, UUID streamId, long length, String mimetype)
    {
        this.id = id;
        this.streamId = streamId;
        this.length = length;
        this.mimetype = mimetype;
    }

    @Override
    public Object getId()
    {
        return id;
    }
    
    @Override
    public UUID getStreamId()
    {
        return streamId;
    }

    @Override
    public long getLength()
    {
        return length;
    }
    
    @Override
    public String getMimetype()
    {
        return mimetype;
    }
}

package caruana.silverbirch.server.blobs;

import java.util.UUID;

public class Stream
{
    private UUID streamId;
    private long length;
    
    public Stream(UUID streamId, long length)
    {
        this.streamId = streamId;
        this.length = length;
    }

    public UUID getStreamId()
    {
        return streamId;
    }

    public long getLength()
    {
        return length;
    }
}

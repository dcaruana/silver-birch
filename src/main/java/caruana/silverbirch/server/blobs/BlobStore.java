package caruana.silverbirch.server.blobs;

import java.io.InputStream;
import java.util.UUID;

public interface BlobStore
{
    // TODO: consider write callback
        
    Stream write(InputStream stream);
    
    InputStream read(UUID streamId);
    
}

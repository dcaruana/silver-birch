package caruana.silverbirch;

import java.util.UUID;

public interface Blob
{

    Object getId();
    
    UUID getStreamId();
    
    long getLength();
    
    String getMimetype();

}

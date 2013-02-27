package caruana.silverbirch;

import java.util.UUID;

public interface Item {

    UUID getUniqueId();
    
    Object getId();
    
    String getName();
    
    Object getDriveId();
    
    Object getRootId();
}

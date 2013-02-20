package org.caruana.silverbirch;

import java.util.UUID;

public interface Node {

    UUID getUniqueId();
    
    Object getId();
    
    String getName();
    
    Object getDriveId();
    
    Object getRootId();
}

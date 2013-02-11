package org.caruana.silverbirch;

public interface Connection {

    Storage storage();
    
    boolean hasChanges();
    
    void applyChanges();
}

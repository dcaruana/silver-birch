package org.caruana.silverbirch;

public interface Transaction {

    boolean hasChanges();
    
    void clearChanges();
    
    void applyChanges();
    
}

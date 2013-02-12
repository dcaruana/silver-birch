package org.caruana.silverbirch;

import org.caruana.silverbirch.SilverBirchException.SilverBirchTransactionException;

public interface Transaction {

    boolean hasChanges();
    
    void clearChanges();
    
    void applyChanges()
        throws SilverBirchTransactionException;
    
}

package caruana.silverbirch;

import caruana.silverbirch.SilverBirchException.SilverBirchTransactionException;

public interface Transaction {

    boolean hasChanges();
    
    void clearChanges();
    
    Result applyChanges()
        throws SilverBirchTransactionException;
    
    
    public interface Result
    {
        Object getTransactionId();
        
        Object resolveId(Object tempId);
    }
}

package caruana.silverbirch.server;

import caruana.silverbirch.Connection;
import caruana.silverbirch.Storage;
import caruana.silverbirch.Transaction;


public class ConnectionImpl implements Connection {
    
    private TransactionImpl transaction;
    private TransactionalStorage storage;
    

    public ConnectionImpl(TransactionalStorage storage, TransactionImpl transaction)
    {
        this.storage = storage;
        this.transaction = transaction;
    }
    
    @Override
    public Transaction transaction()
    {
        return transaction;
    }
    
    @Override
    public Storage storage()
    {
        return storage;
    }
    
}

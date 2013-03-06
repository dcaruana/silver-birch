package caruana.silverbirch.server;

import caruana.silverbirch.Blobs;
import caruana.silverbirch.ChangeLog;
import caruana.silverbirch.Connection;
import caruana.silverbirch.Items;
import caruana.silverbirch.Transaction;


public class ConnectionImpl implements Connection {
    
    private TransactionImpl transaction;
    private TransactionalItems items;
    private TransactionalBlobs blobs;
    private TransactionalChangeLog log;
    
    public ConnectionImpl(TransactionImpl transaction)
    {
        this.transaction = transaction;
    }
    
    public void setTransactionalItems(TransactionalItems items)
    {
        this.items = items;
    }

    public void setTransactionalBlobs(TransactionalBlobs blobs)
    {
        this.blobs = blobs;
    }

    public void setTransactionalChangeLog(TransactionalChangeLog log)
    {
        this.log = log;
    }

    
    @Override
    public Transaction transaction()
    {
        return transaction;
    }
    
    @Override
    public Items items()
    {
        return items;
    }

    @Override
    public Blobs blobs()
    {
        return blobs;
    }

    @Override
    public ChangeLog changelog()
    {
        return log;
    }
    
}

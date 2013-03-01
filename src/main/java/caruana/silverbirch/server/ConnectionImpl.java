package caruana.silverbirch.server;

import caruana.silverbirch.Blobs;
import caruana.silverbirch.Connection;
import caruana.silverbirch.Items;
import caruana.silverbirch.Transaction;


public class ConnectionImpl implements Connection {
    
    private TransactionImpl transaction;
    private TransactionalItems items;
    private TransactionalBlobs blobs;
    
    // TODO: consider setters

    public ConnectionImpl(TransactionalItems items, TransactionalBlobs blobs, TransactionImpl transaction)
    {
        this.items = items;
        this.blobs = blobs;
        this.transaction = transaction;
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
    
}

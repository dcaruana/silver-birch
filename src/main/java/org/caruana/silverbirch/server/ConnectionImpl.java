package org.caruana.silverbirch.server;

import org.caruana.silverbirch.Connection;
import org.caruana.silverbirch.Storage;
import org.caruana.silverbirch.Transaction;


public class ConnectionImpl implements Connection {
    
    private datomic.Connection conn;
    private TransactionImpl transaction;
    private TransactionWrapper transactionWrapper;
    private StorageImpl storage;


    public ConnectionImpl(datomic.Connection conn)
    {
        this.conn = conn;
        this.transaction = new TransactionImpl();
        this.transactionWrapper = new TransactionWrapper(transaction);
        this.storage = new StorageImpl(this);
    }
    
    public datomic.Connection getConnection()
    {
        return conn;
    }
    
    public TransactionImpl getTransaction()
    {
        return transaction;
    }

    @Override
    public Transaction transaction()
    {
        return transactionWrapper;
    }
    
    @Override
    public Storage storage()
    {
        return storage;
    }
    
    
    private class TransactionWrapper implements Transaction
    {
        private TransactionImpl transaction;
        
        private TransactionWrapper(TransactionImpl transaction)
        {
            this.transaction = transaction;
        }

        @Override
        public boolean hasChanges()
        {
            return transaction.hasChanges();
        }

        @Override
        public void clearChanges()
        {
            transaction.clearChanges();
        }

        @Override
        public void applyChanges()
        {
            transaction.applyChanges(ConnectionImpl.this);
        }
    }
    
}

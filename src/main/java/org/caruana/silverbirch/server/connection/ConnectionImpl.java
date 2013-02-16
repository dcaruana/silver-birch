package org.caruana.silverbirch.server.connection;

import org.caruana.silverbirch.Connection;
import org.caruana.silverbirch.Storage;
import org.caruana.silverbirch.Transaction;
import org.caruana.silverbirch.server.SilverBirchImpl;


public class ConnectionImpl implements Connection {
    
    private datomic.Connection conn;
    private SilverBirchImpl server;
    
    private TransactionImpl transaction;
    private ConnectionTransaction connectionTransaction;
    private ConnectionStorage connectionStorage;
    

    public ConnectionImpl(SilverBirchImpl server, datomic.Connection conn)
    {
        this.conn = conn;
        this.server = server;
        this.transaction = new TransactionImpl();
        this.connectionTransaction = new ConnectionTransaction(transaction);
        this.connectionStorage = new ConnectionStorage(this);
    }
    
    public SilverBirchImpl getServer()
    {
        return server;
    }
    
    public TransactionImpl getTransaction()
    {
        return transaction;
    }
    
    public datomic.Connection getConnection()
    {
        return conn;
    }
    
    @Override
    public Transaction transaction()
    {
        return connectionTransaction;
    }
    
    @Override
    public Storage storage()
    {
        return connectionStorage;
    }
    
    
    private class ConnectionTransaction implements Transaction
    {
        private TransactionImpl transaction = new TransactionImpl();

        /*package*/ ConnectionTransaction(TransactionImpl transaction)
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

package caruana.silverbirch.server;

import caruana.silverbirch.Connection;
import caruana.silverbirch.Items;
import caruana.silverbirch.Transaction;


public class ConnectionImpl implements Connection {
    
    private TransactionImpl transaction;
    private TransactionalItems items;
    

    public ConnectionImpl(TransactionalItems items, TransactionImpl transaction)
    {
        this.items = items;
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
    
}

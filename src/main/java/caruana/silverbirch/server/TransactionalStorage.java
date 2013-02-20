package caruana.silverbirch.server;


import caruana.silverbirch.Node;
import caruana.silverbirch.Storage;
import caruana.silverbirch.server.storage.CreateDrive;
import caruana.silverbirch.server.storage.CreateNode;
import caruana.silverbirch.server.storage.StorageImpl;

public class TransactionalStorage implements Storage
{
    private StorageImpl storage;
    private TransactionImpl transaction;
    
    public TransactionalStorage(StorageImpl storage, TransactionImpl transaction)
    {
        this.storage = storage;
        this.transaction = transaction;
    }
    
    @Override
    public Node createDrive(String name)
    {
        CreateDrive statement = storage.createDrive(transaction.getConnection(), name);
        transaction.addStatement(statement);
        return statement.getDrive();
    }

    @Override
    public Node getDrive(String name)
    {
        return storage.getDrive(transaction.getConnection(), name);
    }

    @Override
    public Node createNode(Node parent, String name)
    {
        CreateNode statement = storage.createNode(transaction.getConnection(), parent, name);
        transaction.addStatement(statement);
        return statement.getNode();
    }
    
}

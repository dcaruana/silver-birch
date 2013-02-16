package org.caruana.silverbirch.server.connection;

import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.Storage;
import org.caruana.silverbirch.server.StorageImpl;

public class ConnectionStorage implements Storage
{
    private ConnectionImpl connection;
    private StorageImpl storage;
    
    /*package*/ ConnectionStorage(ConnectionImpl connection)
    {
        this.connection = connection;
        this.storage = connection.getServer().getStorage();
    }
    
    @Override
    public Node createDrive(String name)
    {
        return storage.createDrive(connection, name);
    }

    @Override
    public Node getDrive(String name)
    {
        return storage.getDrive(connection, name);
    }

    @Override
    public Node createNode(Node parent, String name)
    {
        return storage.createNode(connection, parent, name);
    }
}

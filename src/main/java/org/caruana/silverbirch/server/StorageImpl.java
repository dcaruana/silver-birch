package org.caruana.silverbirch.server;

import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.Storage;
import org.caruana.silverbirch.data.NodeImpl;
import org.caruana.silverbirch.statements.CreateDrive;

public class StorageImpl implements Storage
{
    public static final String NODE_NAME = ":node/name";
    public static final String NODE_ROOT = ":node/root";

    private ConnectionImpl conn;
    
    StorageImpl(ConnectionImpl conn)
    {
        this.conn = conn;
    }

    @Override
    public Node createDrive(String name)
    {
        CreateDrive cmd = new CreateDrive(conn.getConnection(), name);
        NodeImpl node = cmd.init();
        conn.addStatement(cmd);
        return node;
    }

    @Override
    public Node getDrive(String name)
    {
        return null;
    }
    
}

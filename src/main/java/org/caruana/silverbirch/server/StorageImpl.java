package org.caruana.silverbirch.server;

import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.data.NodeImpl;
import org.caruana.silverbirch.queries.GetDrive;
import org.caruana.silverbirch.server.connection.ConnectionImpl;
import org.caruana.silverbirch.statements.storage.CreateDrive;
import org.caruana.silverbirch.statements.storage.CreateNode;

import com.google.inject.Inject;

public class StorageImpl
{
    public static final String NODE_NAME = ":node/name";
    public static final String NODE_ROOT = ":node/root";
    public static final String NODE_PARENTS = ":node/parents";

    @Inject GetDrive getDrive;
    

    public Node createDrive(ConnectionImpl conn, String name)
    {
        CreateDrive cmd = new CreateDrive(conn.getConnection(), name);
        NodeImpl node = cmd.init();
        conn.getTransaction().addStatement(cmd);
        return node;
    }

    public Node getDrive(ConnectionImpl conn, String name)
    {
        return getDrive.execute(name);
    }

    public Node createNode(ConnectionImpl conn, Node parent, String name)
    {
        CreateNode cmd = new CreateNode(conn.getConnection(), parent, name);
        NodeImpl node = cmd.init();
        conn.getTransaction().addStatement(cmd);
        return node;
    }

}

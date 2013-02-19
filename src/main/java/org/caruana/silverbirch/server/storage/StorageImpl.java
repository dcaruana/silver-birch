package org.caruana.silverbirch.server.storage;

import org.caruana.silverbirch.Node;

import com.google.inject.Inject;

public class StorageImpl
{
    public static final String NODE_NAME = ":node/name";
    public static final String NODE_ROOT = ":node/root";
    public static final String NODE_PARENTS = ":node/parents";

    private GetDrive getDrive;
    
    @Inject public void setGetDrive(GetDrive getDrive)
    {
        this.getDrive = getDrive;
    }
    

    public CreateDrive createDrive(datomic.Connection conn, String name)
    {
        CreateDrive statement = new CreateDrive(conn, name);
        return statement;
    }

    public Node getDrive(datomic.Connection conn, String name)
    {
        return getDrive.execute(conn, name);
    }

    public CreateNode createNode(datomic.Connection conn, Node parent, String name)
    {
        CreateNode statement = new CreateNode(conn, parent, name);
        return statement;
    }

}

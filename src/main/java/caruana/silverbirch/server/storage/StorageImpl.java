package caruana.silverbirch.server.storage;


import caruana.silverbirch.Node;

import com.google.inject.Inject;

public class StorageImpl
{
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

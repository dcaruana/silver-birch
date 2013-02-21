package caruana.silverbirch.server.storage;


import java.util.List;

import caruana.silverbirch.Node;

import com.google.inject.Inject;

public class StorageImpl
{
    private GetDrive getDrive;
    private ListDrives listDrives;
    
    @Inject public void setGetDrive(GetDrive query)
    {
        this.getDrive = query;
    }

    @Inject public void setListDrives(ListDrives query)
    {
        this.listDrives = query;
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
    
    public List<Node> listDrives(datomic.Connection conn)
    {
        return (List)listDrives.execute(conn);
    }

    public CreateNode createNode(datomic.Connection conn, Node parent, String name)
    {
        CreateNode statement = new CreateNode(conn, parent, name);
        return statement;
    }

}

package caruana.silverbirch.server.items;


import java.util.List;

import caruana.silverbirch.Item;

import com.google.inject.Inject;

public class ItemsImpl
{
    private GetDrive getDrive;
    private ListDrives listDrives;
    private ListItemChildren listItemChildren;
    
    @Inject public void setGetDrive(GetDrive query)
    {
        this.getDrive = query;
    }

    @Inject public void setListDrives(ListDrives query)
    {
        this.listDrives = query;
    }

    @Inject public void setListItemChildren(ListItemChildren query)
    {
        this.listItemChildren = query;
    }

    public CreateDrive createDrive(datomic.Connection conn, String name)
    {
        CreateDrive statement = new CreateDrive(conn, name);
        return statement;
    }

    public Item getDrive(datomic.Connection conn, String name)
    {
        return getDrive.execute(conn, name);
    }
    
    public List<Item> listDrives(datomic.Connection conn)
    {
        return (List)listDrives.execute(conn);
    }

    public CreateItem createItem(datomic.Connection conn, Item parent, String name)
    {
        CreateItem statement = new CreateItem(conn, parent, name);
        return statement;
    }

    public List<Item> listItemChildren(datomic.Connection conn, Item parent)
    {
        return (List)listItemChildren.execute(conn, parent);
    }

}

package caruana.silverbirch.server.items;


import java.util.List;
import java.util.Map;

import caruana.silverbirch.Item;

import com.google.inject.Inject;

public class ItemsImpl
{
    private GetDrive getDrive;
    private ListDrives listDrives;
    private ListItemChildren listItemChildren;
    private GetProperties getProperties;
    
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
    
    @Inject public void setGetProperties(GetProperties query)
    {
        this.getProperties = query;
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

    public SetProperties setProperties(datomic.Connection conn, Item item, Map<String, Object> properties)
    {
        SetProperties statement = new SetProperties(conn, item, properties);
        return statement;
    }
    
    public Map<String, Object> getProperties(datomic.Connection conn, Item item)
    {
        return getProperties.execute(conn, item.getId());
    }
    
}

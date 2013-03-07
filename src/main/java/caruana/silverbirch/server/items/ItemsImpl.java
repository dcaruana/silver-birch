package caruana.silverbirch.server.items;


import java.util.List;
import java.util.Map;

import caruana.silverbirch.Item;
import caruana.silverbirch.server.items.CreateDriveStatement.CreateDrive;

import com.google.inject.Inject;

public class ItemsImpl
{
    private CreateDriveStatement createDrive;
    private GetDriveQuery getDrive;
    private ListDrivesQuery listDrives;
    private ListItemChildrenQuery listItemChildren;
    private GetPropertiesQuery getProperties;
    
    @Inject public void setCreateDrive(CreateDriveStatement statement)
    {
        this.createDrive = statement;
    }
    
    @Inject public void setGetDrive(GetDriveQuery query)
    {
        this.getDrive = query;
    }

    @Inject public void setListDrives(ListDrivesQuery query)
    {
        this.listDrives = query;
    }

    @Inject public void setListItemChildren(ListItemChildrenQuery query)
    {
        this.listItemChildren = query;
    }
    
    @Inject public void setGetProperties(GetPropertiesQuery query)
    {
        this.getProperties = query;
    }

    
    public CreateDrive createDrive(datomic.Connection conn, String name)
    {
        CreateDrive statement = createDrive.statement(name);
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

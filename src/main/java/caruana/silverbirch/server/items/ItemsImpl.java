package caruana.silverbirch.server.items;


import java.util.List;
import java.util.Map;

import caruana.silverbirch.Item;
import caruana.silverbirch.server.items.CreateDriveStatementFactory.CreateDriveStatement;
import caruana.silverbirch.server.items.CreateItemStatementFactory.CreateItemStatement;
import caruana.silverbirch.server.items.SetPropertiesStatementFactory.SetPropertiesStatement;

import com.google.inject.Inject;

public class ItemsImpl
{
    private CreateDriveStatementFactory createDrive;
    private CreateItemStatementFactory createItem;
    private GetDriveQuery getDrive;
    private ListDrivesQuery listDrives;
    private ListItemChildrenQuery listItemChildren;
    private GetPropertiesQuery getProperties;
    private SetPropertiesStatementFactory setProperties;
    
    
    @Inject public void setCreateDrive(CreateDriveStatementFactory statement)
    {
        this.createDrive = statement;
    }

    @Inject public void setCreateItem(CreateItemStatementFactory statement)
    {
        this.createItem = statement;
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

    @Inject public void setSetProperties(SetPropertiesStatementFactory statement)
    {
        this.setProperties = statement;
    }

    
    public CreateDriveStatement createDrive(datomic.Connection conn, String name)
    {
        CreateDriveStatement statement = createDrive.statement(name);
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

    public CreateItemStatement createItem(datomic.Connection conn, Item parent, String name)
    {
        CreateItemStatement statement = createItem.statement(parent, name);
        return statement;
    }

    public List<Item> listItemChildren(datomic.Connection conn, Item parent)
    {
        return (List)listItemChildren.execute(conn, parent);
    }

    public SetPropertiesStatement setProperties(datomic.Connection conn, Item item, Map<String, Object> properties)
    {
        SetPropertiesStatement statement = setProperties.statement(item, properties);
        return statement;
    }
    
    public Map<String, Object> getProperties(datomic.Connection conn, Item item)
    {
        return getProperties.execute(conn, item.getId());
    }
    
}

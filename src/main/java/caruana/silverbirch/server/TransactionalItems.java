package caruana.silverbirch.server;


import java.util.List;
import java.util.Map;

import caruana.silverbirch.Item;
import caruana.silverbirch.Items;
import caruana.silverbirch.server.items.CreateDriveStatementFactory.CreateDriveStatement;
import caruana.silverbirch.server.items.CreateItemStatementFactory.CreateItemStatement;
import caruana.silverbirch.server.items.ItemsImpl;
import caruana.silverbirch.server.items.SetPropertiesStatementFactory.SetPropertiesStatement;

public class TransactionalItems implements Items
{
    private ItemsImpl items;
    private TransactionImpl transaction;
    
    public TransactionalItems(ItemsImpl items, TransactionImpl transaction)
    {
        this.items = items;
        this.transaction = transaction;
    }
    
    @Override
    public Item createDrive(String name)
    {
        CreateDriveStatement statement = items.createDrive(transaction.getConnection(), name);
        transaction.addStatement(statement);
        return statement.getDrive();
    }
    
    @Override
    public List<Item> listDrives()
    {
        return items.listDrives(transaction.getConnection());
    }
    
    @Override
    public Item getDrive(String name)
    {
        return items.getDrive(transaction.getConnection(), name);
    }

    @Override
    public Item createItem(Item parent, String name)
    {
        CreateItemStatement statement = items.createItem(transaction.getConnection(), parent, name);
        transaction.addStatement(statement);
        return statement.getItem();
    }

    @Override
    public List<Item> listChildren(Item item)
    {
        return items.listItemChildren(transaction.getConnection(), item);
    }

    @Override
    public void setProperties(Item item, Map<String, Object> properties)
    {
        SetPropertiesStatement statement = items.setProperties(transaction.getConnection(), item, properties);
        transaction.addStatement(statement);
    }

    @Override
    public Map<String, Object> getProperties(Item item)
    {
        return items.getProperties(transaction.getConnection(), item);
    }

}

package caruana.silverbirch.server;


import java.util.List;

import caruana.silverbirch.Item;
import caruana.silverbirch.Items;
import caruana.silverbirch.server.items.CreateDrive;
import caruana.silverbirch.server.items.CreateItem;
import caruana.silverbirch.server.items.ItemsImpl;

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
        CreateDrive statement = items.createDrive(transaction.getConnection(), name);
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
        CreateItem statement = items.createItem(transaction.getConnection(), parent, name);
        transaction.addStatement(statement);
        return statement.getItem();
    }

    @Override
    public List<Item> listChildren(Item item)
    {
        return items.listItemChildren(transaction.getConnection(), item);
    }

}

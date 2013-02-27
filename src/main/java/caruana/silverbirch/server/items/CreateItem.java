package caruana.silverbirch.server.items;

import java.util.List;
import java.util.Map;
import java.util.UUID;


import caruana.silverbirch.Item;
import caruana.silverbirch.server.schema.Schema;
import caruana.silverbirch.server.statement.AbstractConnectionStatement;

import datomic.Peer;
import datomic.Util;


public class CreateItem extends AbstractConnectionStatement
{
    private ItemData item;
    private Item parent;     // TODO: consider ItemImpl
    
    public CreateItem(datomic.Connection conn, Item parent, String name)
    {
        super(conn);
        this.parent = parent;
        ItemName.validator.checkValid(name);
        UUID uuid = Peer.squuid();
        Object id = Peer.tempid(Schema.DB_PARTITION_USER);
        item = new ItemData(uuid, parent.getRootId(), parent.getDriveId(), id, name);
    }

    public ItemData getItem()
    {
        return item;
    }
    
    @Override
    public List data()
    {
        Map m = Util.map(
                Schema.DB_ID, item.getId(),
                Schema.SYSTEM_UUID, item.getUniqueId(),
                Schema.SYSTEM_UNIQUE_NAME, item.getName() + ItemName.separator + parent.getUniqueId(),
                Schema.ITEM_NAME, item.getName(), 
                Schema.ITEM_ROOT, item.getRootId(),
                Schema.ITEM_PARENTS, parent.getId()
            );
        List d = Util.list(m);
        return d;
    }
    
}

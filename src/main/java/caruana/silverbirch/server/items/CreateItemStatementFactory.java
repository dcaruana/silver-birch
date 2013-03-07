package caruana.silverbirch.server.items;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import caruana.silverbirch.ChangeLog;
import caruana.silverbirch.Item;
import caruana.silverbirch.server.log.ChangeLogImpl;
import caruana.silverbirch.server.schema.Schema;
import caruana.silverbirch.server.statement.Statement;

import com.google.inject.Inject;

import datomic.Peer;
import datomic.Util;


public class CreateItemStatementFactory
{
    private ChangeLogImpl changeLog;
    
    @Inject void setChangeLog(ChangeLogImpl changeLog)
    {
        this.changeLog = changeLog;
    }
    
    public CreateItemStatement statement(Item parent, String name)
    {
        return new CreateItemStatement(parent, name);
    }
    
    public class CreateItemStatement implements Statement
    {
        private ItemData item;
        private Item parent;     // TODO: consider ItemImpl
        
        public CreateItemStatement(Item parent, String name)
        {
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
            Map e = Util.map(
                    Schema.DB_ID, item.getId(),
                    Schema.SYSTEM_UUID, item.getUniqueId(),
                    Schema.SYSTEM_UNIQUE_NAME, item.getName() + ItemName.separator + parent.getUniqueId(),
                    Schema.ITEM_NAME, item.getName(), 
                    Schema.ITEM_ROOT, item.getRootId(),
                    Schema.ITEM_PARENTS, parent.getId()
                );
            
            return Util.list(e);
        }
    
        @Override
        public List log()
        {
            List attrs = Util.list(Schema.ITEM_NAME, Schema.ITEM_ROOT, Schema.ITEM_PARENTS);
            return changeLog.createEntry(ChangeLog.CREATE_ITEM, item.getId(), attrs);
        }
    }
}

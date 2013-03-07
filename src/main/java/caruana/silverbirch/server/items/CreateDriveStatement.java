package caruana.silverbirch.server.items;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import caruana.silverbirch.ChangeLog;
import caruana.silverbirch.server.log.ChangeLogImpl;
import caruana.silverbirch.server.schema.Schema;
import caruana.silverbirch.server.statement.Statement;

import com.google.inject.Inject;

import datomic.Peer;
import datomic.Util;


public class CreateDriveStatement
{
    private ChangeLogImpl changeLog;
    
    @Inject void setChangeLog(ChangeLogImpl changeLog)
    {
        this.changeLog = changeLog;
    }
    
    public CreateDrive statement(String name)
    {
        return new CreateDrive(name);
    }

    public class CreateDrive implements Statement
    {
        private ItemData item;
        
        public CreateDrive(String name)
        {
            ItemName.validator.checkValid(name);
            UUID uuid = Peer.squuid();
            Object id = Peer.tempid(Schema.DB_PARTITION_USER);
            item = new ItemData(uuid, id, id, id, name);
        }
        
        public ItemData getDrive()
        {
            return item;
        }
        
        @Override
        public List data()
        {
            // entity
            Map e = Util.map(
                        Schema.DB_ID, item.getId(),
                        Schema.SYSTEM_UUID, item.getUniqueId(),
                        Schema.SYSTEM_UNIQUE_NAME, item.getName(),
                        Schema.ITEM_NAME, item.getName(), 
                        Schema.ITEM_ROOT, item.getRootId()
                    );
            
            return Util.list(e);
        }

        @Override
        public List log()
        {
            List attrs = Util.list(Schema.ITEM_NAME, Schema.ITEM_ROOT);
            return changeLog.createEntry(ChangeLog.CREATE_DRIVE, item.getId(), attrs);
        }
    }
}
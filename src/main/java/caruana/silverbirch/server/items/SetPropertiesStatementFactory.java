package caruana.silverbirch.server.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import caruana.silverbirch.Blob;
import caruana.silverbirch.ChangeLog;
import caruana.silverbirch.Item;
import caruana.silverbirch.server.log.ChangeLogImpl;
import caruana.silverbirch.server.schema.Schema;
import caruana.silverbirch.server.statement.Statement;

import com.google.inject.Inject;

import datomic.Util;


public class SetPropertiesStatementFactory
{
    private ChangeLogImpl changeLog;
    
    @Inject void setChangeLog(ChangeLogImpl changeLog)
    {
        this.changeLog = changeLog;
    }
    
    public SetPropertiesStatement statement(Item item, Map<String, Object> properties)
    {
        return new SetPropertiesStatement(item, properties);
    }
    
    
    public class SetPropertiesStatement implements Statement
    {
        private Item item;
        private Map<String, Object> properties;
        
        public SetPropertiesStatement(Item item, Map<String, Object> properties)
        {
            this.item = item;
            // TODO: properties is mutable (probably not a problem)
            this.properties = properties;
        }
    
        @Override
        public List data()
        {
            Map<String, Object> m = new HashMap<String, Object>();
            
            // TODO: validate against item schema
            for (Map.Entry<String, Object> entry : properties.entrySet())
            {
                Object val = entry.getValue();
                if (val instanceof Blob)
                {
                    val = ((Blob)val).getId();
                }
                m.put(entry.getKey(), val);
            }
    
            // NOTE: for now, add item id last, in case provided in custom properties map
            m.put(Schema.DB_ID, item.getId());
            List d = Util.list(m);
            return d;
        }
        
        @Override
        public List log()
        {
            return changeLog.createEntry(ChangeLog.SET_PROPERTIES, item.getId(), new ArrayList<String>(properties.keySet()));
        }
    }
}

package caruana.silverbirch.server.items;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import datomic.Entity;


public class GetProperties {

    public Map<String, Object> execute(datomic.Connection connection, Object entityId)
    {
        Map<String, Object> properties = new HashMap<String, Object>();
        
        // TODO: for now, just return all properties
        
        Entity entity = connection.db().entity(entityId);
        Iterator iter = entity.keySet().iterator();
        while(iter.hasNext())
        {
            Object keyword = iter.next();
            properties.put(keyword.toString(), entity.get(keyword));
        }
        
        return properties;
    }
}

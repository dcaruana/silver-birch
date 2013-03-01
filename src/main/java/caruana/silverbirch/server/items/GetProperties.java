package caruana.silverbirch.server.items;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import caruana.silverbirch.Blob;
import caruana.silverbirch.SilverBirchException.SilverBirchItemException;
import caruana.silverbirch.server.blobs.BlobData;
import caruana.silverbirch.server.schema.Schema;
import datomic.Entity;
import datomic.query.EntityMap;


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
            
            // TODO: hard-coded blob property
            if (keyword.toString().equals(Schema.ITEM_CONTENT))
            {
                properties.put(keyword.toString(), toBlob(entity.get(keyword)));
            }
            else
            {
                properties.put(keyword.toString(), entity.get(keyword));
            }
        }
        
        return properties;
    }
    
    private Blob toBlob(Object value)
    {
        if (!(value instanceof Entity))
        {
            throw new SilverBirchItemException("Cannot convert value " + value + " to blob");
        }
        // TODO: this should be Entity, but id is not available
        EntityMap e = (EntityMap)value;
        return new BlobData(e.eid, (UUID)e.get(Schema.SYSTEM_UUID), (Long)e.get(Schema.BLOB_LENGTH), (String)e.get(Schema.BLOB_MIMETYPE));
    }
}

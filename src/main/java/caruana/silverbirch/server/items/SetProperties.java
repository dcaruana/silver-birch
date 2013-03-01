package caruana.silverbirch.server.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import caruana.silverbirch.Item;
import caruana.silverbirch.server.schema.Schema;
import caruana.silverbirch.server.statement.AbstractConnectionStatement;
import datomic.Util;


public class SetProperties extends AbstractConnectionStatement
{
    private Item item;
    private Map<String, Object> properties;
    
    public SetProperties(datomic.Connection conn, Item item, Map<String, Object> properties)
    {
        super(conn);
        this.item = item;
        // TODO: properties is mutable (probably not a problem)
        this.properties = properties;
    }

    @Override
    public List data()
    {
        Map<String, Object> m = new HashMap<String, Object>();
        // TODO: validate against item schema
        m.putAll(properties);
        // NOTE: for now, add item id last, in case provided in custom properties map
        m.put(Schema.DB_ID, item.getId());
        List d = Util.list(m);
        return d;
    }
    
}

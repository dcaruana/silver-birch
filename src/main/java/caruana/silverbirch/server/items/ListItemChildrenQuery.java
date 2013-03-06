package caruana.silverbirch.server.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import caruana.silverbirch.Item;
import caruana.silverbirch.datomic.Data;
import caruana.silverbirch.datomic.Datomic;


public class ListItemChildrenQuery {

    private List query;
    
    public ListItemChildrenQuery()
    {
        query = (List)Data.read("/queries/list_item_children_q.edn").get(0);
    }
    
    public List<ItemData> execute(datomic.Connection connection, Item parent)
    {
        Collection<List<Object>> results = Datomic.query(query, connection, new Object[] { parent.getId() });
        List<ItemData> items = new ArrayList<ItemData>(results.size());
        for (List<Object> result : results)
        {
            ItemData item = new ItemData((UUID)result.get(1), result.get(2), result.get(2), result.get(0), (String)result.get(3));
            items.add(item);
        }
        return Collections.unmodifiableList(items);
    }
}

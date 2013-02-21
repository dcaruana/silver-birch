package caruana.silverbirch.server.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import caruana.silverbirch.Node;
import caruana.silverbirch.datomic.Data;
import caruana.silverbirch.datomic.Datomic;


public class ListNodeChildren {

    private List query;
    
    public ListNodeChildren()
    {
        query = (List)Data.read("/queries/list_node_children.edn").get(0);
    }
    
    public List<NodeData> execute(datomic.Connection connection, Node parent)
    {
        Collection<List<Object>> results = Datomic.query(query, connection, new Object[] { parent.getId() });
        List<NodeData> nodes = new ArrayList<NodeData>(results.size());
        for (List<Object> result : results)
        {
            NodeData node = new NodeData((UUID)result.get(1), result.get(2), result.get(2), result.get(0), (String)result.get(3));
            nodes.add(node);
        }
        return Collections.unmodifiableList(nodes);
    }
}

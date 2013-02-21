package caruana.silverbirch.server.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import caruana.silverbirch.datomic.Data;
import caruana.silverbirch.datomic.Datomic;


public class ListDrives {

    private List query;
    
    public ListDrives()
    {
        query = (List)Data.read("/queries/list_drives.edn").get(0);
    }
    
    public List<NodeData> execute(datomic.Connection connection)
    {
        Collection<List<Object>> results = Datomic.query(query, connection);
        List<NodeData> drives = new ArrayList<NodeData>(results.size());
        for (List<Object> drive : results)
        {
            NodeData node = new NodeData((UUID)drive.get(1), drive.get(0), drive.get(0), drive.get(0), (String)drive.get(2));
            drives.add(node);
        }
        return Collections.unmodifiableList(drives);
    }
}

package caruana.silverbirch.server.items;

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
    
    public List<ItemData> execute(datomic.Connection connection)
    {
        Collection<List<Object>> results = Datomic.query(query, connection);
        List<ItemData> drives = new ArrayList<ItemData>(results.size());
        for (List<Object> drive : results)
        {
            ItemData item = new ItemData((UUID)drive.get(1), drive.get(0), drive.get(0), drive.get(0), (String)drive.get(2));
            drives.add(item);
        }
        return Collections.unmodifiableList(drives);
    }
}

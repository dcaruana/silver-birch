package caruana.silverbirch.server.items;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


import caruana.silverbirch.SilverBirchException.SilverBirchInternalException;
import caruana.silverbirch.datomic.Data;
import caruana.silverbirch.datomic.Datomic;


public class GetDrive {

    private List query;
    
    public GetDrive()
    {
        query = (List)Data.read("/queries/get_drive.edn").get(0);
    }
    
    public ItemData execute(datomic.Connection connection, String name)
    {
        Collection<List<Object>> results = Datomic.query(query, connection, new Object[] {name});
        if (results.size() == 0)
        {
            return null;
        }
        List<Object> drive = results.iterator().next();
        return new ItemData((UUID)drive.get(1), drive.get(0), drive.get(0), drive.get(0), (String)drive.get(2));
    }
}

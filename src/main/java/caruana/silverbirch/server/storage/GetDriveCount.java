package caruana.silverbirch.server.storage;

import java.util.Collection;
import java.util.List;

import caruana.silverbirch.datomic.Data;
import caruana.silverbirch.datomic.Datomic;


public class GetDriveCount {

    private List query;
    
    public GetDriveCount()
    {
        query = (List)Data.read("/queries/get_drive_count.edn").get(0);
    }
    
    public int execute(datomic.Connection connection)
    {
        Collection<List<Object>> results = Datomic.query(query, connection);
        List<Object> count = results.iterator().next();
        return (Integer)count.get(0);
    }
}

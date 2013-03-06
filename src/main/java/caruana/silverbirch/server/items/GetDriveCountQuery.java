package caruana.silverbirch.server.items;

import java.util.Collection;
import java.util.List;

import caruana.silverbirch.datomic.Data;
import caruana.silverbirch.datomic.Datomic;


public class GetDriveCountQuery {

    private List query;
    
    public GetDriveCountQuery()
    {
        query = (List)Data.read("/queries/get_drive_count_q.edn").get(0);
    }
    
    public int execute(datomic.Connection connection)
    {
        Collection<List<Object>> results = Datomic.query(query, connection);
        List<Object> count = results.iterator().next();
        return (Integer)count.get(0);
    }
}

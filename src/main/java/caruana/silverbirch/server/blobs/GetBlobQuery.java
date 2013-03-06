package caruana.silverbirch.server.blobs;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import caruana.silverbirch.datomic.Data;
import caruana.silverbirch.datomic.Datomic;


public class GetBlobQuery {

    private List query;
    
    public GetBlobQuery()
    {
        query = (List)Data.read("/queries/get_blob_q.edn").get(0);
    }
    
    public BlobData execute(datomic.Connection connection, Object id)
    {
        Collection<List<Object>> results = Datomic.query(query, connection, new Object[] {id});
        if (results.size() == 0)
        {
            return null;
        }
        List<Object> blob = results.iterator().next();
        return new BlobData(blob.get(0), (UUID)blob.get(1), (Long)blob.get(2), (String)blob.get(3));
    }
}

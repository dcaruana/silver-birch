package org.caruana.silverbirch.server.storage;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.caruana.silverbirch.SilverBirchException.SilverBirchInternalException;
import org.caruana.silverbirch.datomic.Data;
import org.caruana.silverbirch.datomic.Datomic;


public class GetDrive {

    private List query;
    
    public GetDrive()
    {
        query = (List)Data.read("/queries/get_drive.edn").get(0);
    }
    
    public NodeData execute(datomic.Connection connection, String name)
    {
        Collection<List<Object>> results = Datomic.query(query, connection, new Object[] {name});
        if (results.size() == 0)
        {
            return null;
        }
        if (results.size() > 1)
        {
            throw new SilverBirchInternalException("Found " + results.size() + " drives named " + name);
        }
        List<Object> drive = results.iterator().next();
        return new NodeData((UUID)drive.get(1), drive.get(0), drive.get(0), drive.get(0), (String)drive.get(2));
    }
}

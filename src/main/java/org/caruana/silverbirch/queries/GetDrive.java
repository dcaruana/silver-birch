package org.caruana.silverbirch.queries;

import java.util.Collection;
import java.util.List;

import org.caruana.silverbirch.SilverBirchException.SilverBirchInternalException;
import org.caruana.silverbirch.data.NodeImpl;
import org.caruana.silverbirch.server.connection.ConnectionImpl;
import org.caruana.silverbirch.util.Data;
import org.caruana.silverbirch.util.DatomicImpl;


public class GetDrive {

    private List query;
    
    public GetDrive()
    {
        query = (List)Data.read("/queries/get_drive.edn").get(0);
    }
    
    public NodeImpl execute(ConnectionImpl conn, String name)
    {
        Collection<List<Object>> results = DatomicImpl.query(query, conn.getConnection(), new Object[] {name});
        if (results.size() == 0)
        {
            return null;
        }
        if (results.size() > 1)
        {
            throw new SilverBirchInternalException("Found " + results.size() + " drives named " + name);
        }
        List<Object> drive = results.iterator().next();
        return new NodeImpl(drive.get(0), drive.get(0), drive.get(0), (String)drive.get(1));
    }
}

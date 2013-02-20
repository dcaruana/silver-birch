package caruana.silverbirch.datomic;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import datomic.Connection;
import datomic.ListenableFuture;
import datomic.Peer;


public class Datomic
{
    
    public static ListenableFuture<Map> transact(Connection conn, List data)
    {
        ListenableFuture<Map> future = conn.transact(data);
        return future;
    }

    public static Collection<List<Object>> query(String queryFile, Object source, Object... inputs)
            throws ExecutionException, InterruptedException
    {
        List query = (List)Data.read(queryFile).get(0);
        return query(query, source, inputs);
    }
        
    public static Collection<List<Object>> queryWithRules(String queryFile, String rulesFile, Object source, Object... inputs)
    {
        List query = (List)Data.read(queryFile).get(0);
        List rules = (List)Data.read(rulesFile).get(0);
        Object[] queryInputs = new Object[inputs.length + 1];
        queryInputs[0] = rules;
        System.arraycopy(inputs, 0, queryInputs, 1, inputs.length);
        return query(query, source, queryInputs);
    }

    public static Collection<List<Object>> query(List query, Object source, Object... args)
    {
        if (source instanceof Connection) source = ((Connection)source).db();
        Object[] inputs = new Object[args.length + 1];
        inputs[0] = source;
        System.arraycopy(args, 0, inputs, 1, args.length);
        Collection<List<Object>> results = Peer.q(query, inputs);
        Data.print(results);
        return results;
    }

}

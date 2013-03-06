package caruana.silverbirch.server.log;

import java.util.Collection;
import java.util.List;

import caruana.silverbirch.datomic.Data;
import caruana.silverbirch.datomic.Datomic;
import datomic.Database;


public class GetTransactionChangeLog
{

    private List query;
    
    public GetTransactionChangeLog()
    {
        query = (List)Data.read("/queries/get_transaction_change_log.edn").get(0);
    }
    
    public Collection<List<Object>> execute(datomic.Connection connection, Object transactionId)
    {
        Database asOf = connection.db().history();
        Collection<List<Object>> results = Datomic.query(query, asOf, transactionId);
        return results;
    }
}

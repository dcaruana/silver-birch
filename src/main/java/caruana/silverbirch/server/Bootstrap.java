package caruana.silverbirch.server;

import caruana.silverbirch.server.schema.EDN;

public class Bootstrap 
{
    private static String BOOTSTRAP_EDN = "/bootstrap/";

    
    public void bootstrap(datomic.Connection conn)
    {
        TransactionImpl transaction = new TransactionImpl(conn);
        
        EDN itemsSchema = new EDN(BOOTSTRAP_EDN + "items_schema.edn");
        transaction.addStatement(itemsSchema);

        transaction.applyChanges();
    }
}

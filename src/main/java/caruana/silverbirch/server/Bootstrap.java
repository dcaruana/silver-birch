package caruana.silverbirch.server;

import caruana.silverbirch.server.schema.EDNStatement;

public class Bootstrap 
{
    private static String BOOTSTRAP_EDN = "/bootstrap/";

    
    public void bootstrap(datomic.Connection conn)
    {
        TransactionImpl transaction = new TransactionImpl(conn);
        
        EDNStatement systemSchema = new EDNStatement(BOOTSTRAP_EDN + "system_schema.edn");
        transaction.addStatement(systemSchema);

        EDNStatement logSchema = new EDNStatement(BOOTSTRAP_EDN + "log_schema.edn");
        transaction.addStatement(logSchema);

        EDNStatement itemsSchema = new EDNStatement(BOOTSTRAP_EDN + "items_schema.edn");
        transaction.addStatement(itemsSchema);

        EDNStatement blobsSchema = new EDNStatement(BOOTSTRAP_EDN + "blobs_schema.edn");
        transaction.addStatement(blobsSchema);

        transaction.applyChanges();
    }
}

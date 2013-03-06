package caruana.silverbirch.server;

import caruana.silverbirch.server.schema.EDN;

public class Bootstrap 
{
    private static String BOOTSTRAP_EDN = "/bootstrap/";

    
    public void bootstrap(datomic.Connection conn)
    {
        TransactionImpl transaction = new TransactionImpl(conn);
        
        EDN systemSchema = new EDN(BOOTSTRAP_EDN + "system_schema.edn");
        transaction.addStatement(systemSchema);

        EDN logSchema = new EDN(BOOTSTRAP_EDN + "log_schema.edn");
        transaction.addStatement(logSchema);

        EDN itemsSchema = new EDN(BOOTSTRAP_EDN + "items_schema.edn");
        transaction.addStatement(itemsSchema);

        EDN blobsSchema = new EDN(BOOTSTRAP_EDN + "blobs_schema.edn");
        transaction.addStatement(blobsSchema);

        transaction.applyChanges();
    }
}

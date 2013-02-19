package org.caruana.silverbirch.server;

import org.caruana.silverbirch.server.schema.EDN;

public class Bootstrap 
{
    private static String BOOTSTRAP_EDN = "/bootstrap/";

    
    public void bootstrap(datomic.Connection conn)
    {
        TransactionImpl transaction = new TransactionImpl(conn);
        
        EDN storageSchema = new EDN(BOOTSTRAP_EDN + "storage_schema.edn");
        transaction.addStatement(storageSchema);

        transaction.applyChanges();
    }
}

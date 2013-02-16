package org.caruana.silverbirch.server;

import org.caruana.silverbirch.server.connection.ConnectionImpl;
import org.caruana.silverbirch.server.connection.TransactionImpl;
import org.caruana.silverbirch.statements.util.EDN;

public class Bootstrap 
{
    private static String BOOTSTRAP_EDN = "/bootstrap/";

    
    public void bootstrap(ConnectionImpl conn)
    {
        TransactionImpl transaction = conn.getTransaction();
        
        EDN storageSchema = new EDN(BOOTSTRAP_EDN + "storage_schema.edn");
        transaction.addStatement(storageSchema);

        transaction.applyChanges(conn);
    }
}

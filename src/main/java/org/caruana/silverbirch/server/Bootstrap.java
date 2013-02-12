package org.caruana.silverbirch.server;

import org.caruana.silverbirch.statements.util.EDN;

public class Bootstrap 
{
    private static String BOOTSTRAP_EDN = "/bootstrap/";

    private ConnectionImpl conn;

    Bootstrap(ConnectionImpl conn)
    {
        this.conn = conn;
    }
    
    public void bootsrap()
    {
        TransactionImpl transaction = conn.getTransaction();
        
        EDN storageSchema = new EDN(BOOTSTRAP_EDN + "storage_schema.edn");
        transaction.addStatement(storageSchema);

        transaction.applyChanges(conn);
    }
}

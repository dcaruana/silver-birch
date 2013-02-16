package org.caruana.silverbirch.server;

import org.caruana.silverbirch.server.connection.ConnectionImpl;
import org.caruana.silverbirch.server.connection.TransactionImpl;
import org.caruana.silverbirch.statements.util.EDN;

public class TestBootstrap
{
    private static final String BOOTSTRAP_EDN = "/bootstrap/";
    
    public static final String TEST_NAME = ":test/name";

    public void bootstrap(ConnectionImpl conn)
    {
        TransactionImpl transaction = conn.getTransaction();
        
        EDN testSchema = new EDN(BOOTSTRAP_EDN + "test_schema.edn");
        transaction.addStatement(testSchema);

        transaction.applyChanges(conn);
    }
}

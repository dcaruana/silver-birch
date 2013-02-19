package org.caruana.silverbirch.server.schema;

import org.caruana.silverbirch.server.TransactionImpl;
import org.caruana.silverbirch.server.schema.EDN;

public class TestData
{
    private static final String DATA_EDN = "/data/";
    
    public static final String TEST_NAME = ":test/name";

    public void bootstrap(datomic.Connection conn)
    {
        data(conn, "test_schema.edn");
    }
    
    public void data(datomic.Connection conn, String datafile)
    {
        TransactionImpl transaction = new TransactionImpl(conn);
        
        EDN testSchema = new EDN(DATA_EDN + datafile);
        transaction.addStatement(testSchema);

        transaction.applyChanges();
    }
}

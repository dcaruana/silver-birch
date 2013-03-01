package caruana.silverbirch.server.schema;


import caruana.silverbirch.Transaction.Result;
import caruana.silverbirch.server.TransactionImpl;

public class TestData
{
    private static final String DATA_EDN = "/data/";
    
    public static final String TEST_NAME = ":test/name";

    public void bootstrap(datomic.Connection conn)
    {
        data(conn, "test_schema.edn");
    }
    
    public Result data(datomic.Connection conn, String datafile)
    {
        TransactionImpl transaction = new TransactionImpl(conn);
        
        EDN testSchema = new EDN(DATA_EDN + datafile);
        transaction.addStatement(testSchema);

        return transaction.applyChanges();
    }
}

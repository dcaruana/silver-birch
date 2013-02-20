package org.caruana.silverbirch.datomic;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.Ignore;
import org.junit.Test;

import datomic.Connection;
import datomic.ListenableFuture;
import datomic.Peer;
import datomic.Util;

public class UniqueValueWithinTransaction {

//    public static void main(String[] args)
//        throws Exception
//    {
//        UniqueValueWithinTransaction test = new UniqueValueWithinTransaction();
//        test.test();
//    }

    @Ignore("https://groups.google.com/forum/#!topic/datomic/30ADvlLV9f4") @Test
    public void test()
        throws ExecutionException, InterruptedException
    {
        // create db
        String db = "datomic:mem://unique";
        Peer.createDatabase(db);
        datomic.Connection conn = Peer.connect(db);
        
        // add unique attribute defn
        transact(conn, Util.list(uniqueAttributeDefn()));
        
        // works: add two entities with same value in two transactions
        //        i.e. unique exception is thrown
        try
        {
            transact(conn, Util.list(entity1()));
            transact(conn, Util.list(entity1()));
            fail("Failed to catch unqie value");
        }
        catch(ExecutionException e)
        {
        }
        
        // fails: add two entities with same value in same transaction
        //        i.e. exception is not thrown
        try
        {
            transact(conn, Util.list(entity2(), entity2()));
            fail("Failed to catch unique value");
        }
        catch(ExecutionException e)
        {
        }
    }

    private void transact(Connection conn, List txData)
        throws ExecutionException, InterruptedException
    {
        ListenableFuture<Map> future = Datomic.transact(conn, txData);
        // force exception if trx failed
        Map m = future.get();
    }
    
    private Map uniqueAttributeDefn()
    {
        return Util.map(":db/id", Peer.tempid(":db.part/db"), 
                 ":db/ident", ":system/unique_name",
                 ":db/valueType", ":db.type/string",
                 ":db/cardinality", ":db.cardinality/one",
                 ":db/unique", ":db.unique/value",
                 ":db.install/_attribute", ":db.part/db");
    }

    private Map entity1()
    {
        return Util.map(":db/id", Peer.tempid(":db.part/user"),
                        ":system/unique_name", "test1" );
    }
    
    private Map entity2()
    {
        return Util.map(":db/id", Peer.tempid(":db.part/user"),
                        ":system/unique_name", "test2" );
    }
}

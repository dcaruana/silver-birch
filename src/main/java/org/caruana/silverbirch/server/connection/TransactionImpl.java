package org.caruana.silverbirch.server.connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.caruana.silverbirch.SilverBirchException.SilverBirchTransactionException;
import org.caruana.silverbirch.Transaction.Result;
import org.caruana.silverbirch.statements.Statement;
import org.caruana.silverbirch.util.Data;
import org.caruana.silverbirch.util.DatomicImpl;

import datomic.Connection;
import datomic.Database;
import datomic.ListenableFuture;
import datomic.Peer;


public class TransactionImpl {
    
    private List<Statement> statements;
    

    public TransactionImpl()
    {
        this.statements = new ArrayList<Statement>();
    }
    
    public void addStatement(Statement statement)
    {
        statements.add(statement);
    }

    public boolean hasChanges()
    {
        return statements.size() > 0;
    }

    public void clearChanges()
    {
        statements.clear();
    }
    
    public ResultImpl applyChanges(ConnectionImpl conn)
    {
        List transaction = new ArrayList();
        for (Statement statement : statements)
        {
            List data = statement.data();
            transaction.addAll(data);
        }
        try
        {
            ListenableFuture<Map> future = DatomicImpl.transact(conn.getConnection(), transaction);
            Map m = future.get();
            Data.print(future);
            
            clearChanges();
            return new ResultImpl(conn.getConnection().db(), m);
        }
        catch(ExecutionException e)
        {
            throw new SilverBirchTransactionException("Error executing transaction", e.getCause());
        }
        catch(InterruptedException e)
        {
            throw new SilverBirchTransactionException("Timeout during transaction", e);
        }
    }

    public static class ResultImpl implements Result
    {
        private Database db;
        private Map result;
        
        public ResultImpl(Database db, Map result)
        {
            this.db = db;
            this.result = result;
        }
        
        @Override
        public Object resolveId(Object tempId)
        {
            return Peer.resolveTempid(db, result.get(Connection.TEMPIDS), tempId);
        }
        
    }
}

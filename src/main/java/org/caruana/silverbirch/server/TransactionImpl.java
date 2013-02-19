package org.caruana.silverbirch.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.caruana.silverbirch.SilverBirchException.SilverBirchTransactionException;
import org.caruana.silverbirch.Transaction;
import org.caruana.silverbirch.server.statement.Statement;
import org.caruana.silverbirch.util.Data;
import org.caruana.silverbirch.util.DatomicImpl;

import datomic.Connection;
import datomic.Database;
import datomic.ListenableFuture;
import datomic.Peer;


public class TransactionImpl implements Transaction 
{
    
    private datomic.Connection conn;
    private List<Statement> statements;
    

    public TransactionImpl(datomic.Connection conn)
    {
        this.conn = conn;
        this.statements = new ArrayList<Statement>();
    }
    
    public datomic.Connection getConnection()
    {
        return conn;
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
    
    public ResultImpl applyChanges()
    {
        List transaction = new ArrayList();
        for (Statement statement : statements)
        {
            List data = statement.data();
            transaction.addAll(data);
        }
        try
        {
            ListenableFuture<Map> future = DatomicImpl.transact(conn, transaction);
            Map m = future.get();
            Data.print(future);
            
            clearChanges();
            return new ResultImpl(conn.db(), m);
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

package org.caruana.silverbirch.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.caruana.silverbirch.SilverBirchException.SilverBirchTransactionException;
import org.caruana.silverbirch.statements.Statement;
import org.caruana.silverbirch.util.Data;
import org.caruana.silverbirch.util.DatomicImpl;

import datomic.ListenableFuture;


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
    
    public void applyChanges(ConnectionImpl conn)
    {
        List transaction = new ArrayList();
        for (Statement statement : statements)
        {
            List data = statement.data();
            transaction.addAll(data);
        }
        ListenableFuture<Map> future = DatomicImpl.transact(conn.getConnection(), transaction);
        try
        {
            Map m = future.get();
            //Data.print(future);
        }
        catch(ExecutionException e)
        {
            throw new SilverBirchTransactionException("Error executing transaction", e.getCause());
        }
        catch(InterruptedException e)
        {
            throw new SilverBirchTransactionException("Timeout during transaction", e);
        }
        
        clearChanges();
    }

}

package org.caruana.silverbirch.server;

import java.util.ArrayList;
import java.util.List;

import org.caruana.silverbirch.util.DatomicImpl;


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
        DatomicImpl.transact(conn.getConnection(), transaction);

        clearChanges();
    }

}

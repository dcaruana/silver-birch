package org.caruana.silverbirch.server;

import java.util.ArrayList;
import java.util.List;

import org.caruana.silverbirch.Connection;
import org.caruana.silverbirch.Storage;
import org.caruana.silverbirch.util.DatomicImpl;


public class ConnectionImpl implements Connection {
    
    private datomic.Connection conn;
    private List<Statement> statements;
    

    public ConnectionImpl(datomic.Connection conn)
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

    @Override
    public boolean hasChanges()
    {
        return statements.size() > 0;
    }

    @Override
    public void applyChanges()
    {
        List transaction = new ArrayList();
        for (Statement statement : statements)
        {
            List data = statement.data();
            transaction.addAll(data);
        }
        DatomicImpl.transact(conn, transaction);
        statements.clear();
    }

    public Storage storage()
    {
        return new StorageImpl(this);
    }
    
}

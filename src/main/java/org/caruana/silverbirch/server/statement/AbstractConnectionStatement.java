package org.caruana.silverbirch.server.statement;



public abstract class AbstractConnectionStatement implements Statement
{
    protected datomic.Connection conn;
    
    public AbstractConnectionStatement(datomic.Connection conn)
    {
        this.conn = conn;
    }
    
}

package org.caruana.silverbirch.statements;


public abstract class AbstractConnectionStatement implements Statement
{
    protected datomic.Connection conn;
    
    public AbstractConnectionStatement(datomic.Connection conn)
    {
        this.conn = conn;
    }
    
}

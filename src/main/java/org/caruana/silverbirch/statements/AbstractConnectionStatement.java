package org.caruana.silverbirch.statements;

import org.caruana.silverbirch.server.Statement;

/*protected*/ abstract class AbstractConnectionStatement implements Statement
{
    protected datomic.Connection conn;
    
    public AbstractConnectionStatement(datomic.Connection conn)
    {
        this.conn = conn;
    }
    
}

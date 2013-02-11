package org.caruana.silverbirch.commands;

import org.caruana.silverbirch.server.Command;

/*protected*/ abstract class AbstractConnectionCommand implements Command
{
    protected datomic.Connection conn;
    
    public AbstractConnectionCommand(datomic.Connection conn)
    {
        this.conn = conn;
    }
    
}

package org.caruana.clockwork.datomic;

import org.caruana.clockwork.Connection;

public class ConnectionImpl implements Connection {
    
    private datomic.Connection conn;
    
    public ConnectionImpl(datomic.Connection conn)
    {
        this.conn = conn;
    }

}

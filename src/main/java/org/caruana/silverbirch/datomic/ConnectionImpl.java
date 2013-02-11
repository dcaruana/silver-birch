package org.caruana.silverbirch.datomic;

import org.caruana.silverbirch.Connection;

public class ConnectionImpl implements Connection {
    
    private datomic.Connection conn;
    
    public ConnectionImpl(datomic.Connection conn)
    {
        this.conn = conn;
    }

}

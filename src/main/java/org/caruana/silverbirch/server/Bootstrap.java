package org.caruana.silverbirch.server;

import org.caruana.silverbirch.util.DatomicImpl;

public class Bootstrap 
{
    private static String BOOTSTRAP_EDN = "/bootstrap/";

    private ConnectionImpl conn;

    Bootstrap(ConnectionImpl conn)
    {
        this.conn = conn;
    }
    
    public void bootsrap()
    {
        DatomicImpl.transact(conn.getConnection(), BOOTSTRAP_EDN + "storage_schema.edn");
    }
}

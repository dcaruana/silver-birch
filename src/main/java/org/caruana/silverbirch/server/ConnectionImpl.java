package org.caruana.silverbirch.server;

import java.util.ArrayList;
import java.util.List;

import org.caruana.silverbirch.Connection;
import org.caruana.silverbirch.Storage;
import org.caruana.silverbirch.util.DatomicImpl;


public class ConnectionImpl implements Connection {
    
    private datomic.Connection conn;
    private List<Command> commands;
    

    public ConnectionImpl(datomic.Connection conn)
    {
        this.conn = conn;
        this.commands = new ArrayList<Command>();
    }
    
    public datomic.Connection getConnection()
    {
        return conn;
    }
    
    public void addCommand(Command cmd)
    {
        commands.add(cmd);
    }

    @Override
    public boolean hasChanges()
    {
        return commands.size() > 0;
    }

    @Override
    public void applyChanges()
    {
        List transaction = new ArrayList();
        for (Command cmd : commands)
        {
            List data = cmd.data();
            transaction.addAll(data);
        }
        DatomicImpl.transact(conn, transaction);
        commands.clear();
    }

    public Storage storage()
    {
        return new StorageImpl(this);
    }
    
}

package org.caruana.silverbirch.statements;

import java.util.List;
import java.util.Map;

import org.caruana.silverbirch.data.NodeImpl;
import org.caruana.silverbirch.server.StorageImpl;
import org.caruana.silverbirch.util.DatomicImpl;

import datomic.Peer;
import datomic.Util;


public class CreateDrive extends AbstractConnectionStatement
{
    private String name;
    private NodeImpl node;
    
    public CreateDrive(datomic.Connection conn, String name)
    {
        super(conn);
        this.name = name;
    }

    public NodeImpl init()
    {
        Object id = Peer.tempid(DatomicImpl.DB_PARTITION_USER);
        node = new NodeImpl(id, id, name);
        return node;
    }
    
    @Override
    public List data()
    {
        Map m = Util.map(
                    DatomicImpl.DB_ID, node.getId(),
                    StorageImpl.NODE_NAME, node.getName(), 
                    StorageImpl.NODE_ROOT, node.getRootId()
                );
        List d = Util.list(m);
        return d;
    }
    
}

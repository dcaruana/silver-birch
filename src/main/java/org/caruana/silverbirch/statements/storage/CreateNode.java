package org.caruana.silverbirch.statements.storage;

import java.util.List;
import java.util.Map;

import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.data.NodeImpl;
import org.caruana.silverbirch.server.StorageImpl;
import org.caruana.silverbirch.statements.AbstractConnectionStatement;
import org.caruana.silverbirch.util.DatomicImpl;

import datomic.Peer;
import datomic.Util;


public class CreateNode extends AbstractConnectionStatement
{
    private String name;
    private NodeImpl node;
    private Node parent;     // TODO: consider NodeImpl
    
    public CreateNode(datomic.Connection conn, Node parent, String name)
    {
        super(conn);
        this.name = name;
        this.parent = parent;
    }

    public NodeImpl init()
    {
        Object id = Peer.tempid(DatomicImpl.DB_PARTITION_USER);
        node = new NodeImpl(parent.getRootId(), parent.getDriveId(), id, name);
        return node;
    }
    
    @Override
    public List data()
    {
        Map m = Util.map(
                DatomicImpl.DB_ID, node.getId(),
                StorageImpl.NODE_NAME, node.getName(), 
                StorageImpl.NODE_ROOT, node.getRootId(),
                StorageImpl.NODE_PARENTS, parent.getId()
            );
        List d = Util.list(m);
        return d;
    }
    
}

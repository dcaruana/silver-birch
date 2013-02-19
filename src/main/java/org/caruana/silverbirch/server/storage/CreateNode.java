package org.caruana.silverbirch.server.storage;

import java.util.List;
import java.util.Map;

import org.caruana.silverbirch.Node;
import org.caruana.silverbirch.server.schema.Schema;
import org.caruana.silverbirch.server.statement.AbstractConnectionStatement;

import datomic.Peer;
import datomic.Util;


public class CreateNode extends AbstractConnectionStatement
{
    private NodeData node;
    private Node parent;     // TODO: consider NodeImpl
    
    public CreateNode(datomic.Connection conn, Node parent, String name)
    {
        super(conn);
        this.parent = parent;
        NodeNameValidator.instance.checkValid(name);
        Object id = Peer.tempid(Schema.DB_PARTITION_USER);
        node = new NodeData(parent.getRootId(), parent.getDriveId(), id, name);
    }

    public NodeData getNode()
    {
        return node;
    }
    
    @Override
    public List data()
    {
        Map m = Util.map(
                Schema.DB_ID, node.getId(),
                Schema.NODE_NAME, node.getName(), 
                Schema.NODE_ROOT, node.getRootId(),
                Schema.NODE_PARENTS, parent.getId()
            );
        List d = Util.list(m);
        return d;
    }
    
}

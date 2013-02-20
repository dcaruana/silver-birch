package org.caruana.silverbirch.server.storage;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.caruana.silverbirch.server.schema.Schema;
import org.caruana.silverbirch.server.statement.AbstractConnectionStatement;

import datomic.Peer;
import datomic.Util;


public class CreateDrive extends AbstractConnectionStatement
{
    private NodeData node;
    
    public CreateDrive(datomic.Connection conn, String name)
    {
        super(conn);
        NodeName.validator.checkValid(name);
        UUID uuid = Peer.squuid();
        Object id = Peer.tempid(Schema.DB_PARTITION_USER);
        node = new NodeData(uuid, id, id, id, name);
    }

    public NodeData getDrive()
    {
        return node;
    }
    
    @Override
    public List data()
    {
        Map m = Util.map(
                    Schema.DB_ID, node.getId(),
                    Schema.SYSTEM_UUID, node.getUniqueId(),
                    Schema.SYSTEM_UNIQUE_NAME, node.getName(),
                    Schema.NODE_NAME, node.getName(), 
                    Schema.NODE_ROOT, node.getRootId(),
                    Schema.NODE_PARENTS, node.getId()
                );
        List d = Util.list(m);
        return d;
    }
    
}

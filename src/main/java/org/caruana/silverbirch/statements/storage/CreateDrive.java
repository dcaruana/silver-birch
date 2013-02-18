package org.caruana.silverbirch.statements.storage;

import java.util.List;
import java.util.Map;

import org.caruana.silverbirch.data.NodeImpl;
import org.caruana.silverbirch.server.StorageImpl;
import org.caruana.silverbirch.statements.AbstractConnectionStatement;
import org.caruana.silverbirch.util.DatomicImpl;
import org.caruana.silverbirch.validators.NodeNameValidator;

import datomic.Peer;
import datomic.Util;


public class CreateDrive extends AbstractConnectionStatement
{
    private String name;
    private NodeImpl node;
    
    private static NodeNameValidator nameValidator = new NodeNameValidator();

    
    public CreateDrive(datomic.Connection conn, String name)
    {
        super(conn);
        nameValidator.checkValid(name);
        this.name = name;
    }

    public NodeImpl init()
    {
        Object id = Peer.tempid(DatomicImpl.DB_PARTITION_USER);
        node = new NodeImpl(id, id, id, name);
        return node;
    }
    
    @Override
    public List data()
    {
        Map m = Util.map(
                    DatomicImpl.DB_ID, node.getId(),
                    StorageImpl.NODE_NAME, node.getName(), 
                    StorageImpl.NODE_ROOT, node.getRootId(),
                    StorageImpl.NODE_PARENTS, node.getId()
                );
        List d = Util.list(m);
        return d;
    }
    
}

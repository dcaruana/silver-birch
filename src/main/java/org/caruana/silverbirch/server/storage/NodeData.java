package org.caruana.silverbirch.server.storage;

import org.caruana.silverbirch.Node;


public class NodeData implements Node
{
    private Object rootId;
    private Object driveId;
    private Object id;
    private String name;

    public NodeData(Object rootId, Object driveId, Object id, String name)
    {
        this.rootId = rootId;
        this.driveId = driveId;
        this.id = id;
        this.name = name;
    }

    @Override
    public Object getRootId()
    {
        return rootId;
    }

    @Override
    public Object getDriveId()
    {
        return driveId;
    }
    
    @Override
    public Object getId()
    {
        return id;
    }
    
    @Override
    public String getName()
    {
        return name;
    }

}

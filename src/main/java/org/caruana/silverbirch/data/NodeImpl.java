package org.caruana.silverbirch.data;

import org.caruana.silverbirch.Node;


public class NodeImpl implements Node
{
    private Object rootId;
    private Object driveId;
    private Object id;
    private String name;

    public NodeImpl(Object rootId, Object driveId, Object id, String name)
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

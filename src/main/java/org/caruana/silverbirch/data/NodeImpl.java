package org.caruana.silverbirch.data;

import org.caruana.silverbirch.Node;


public class NodeImpl implements Node
{
    private Object rootId;
    private Object id;
    private String name;

    public NodeImpl(Object rootId, Object id, String name)
    {
        this.rootId = rootId;
        this.id = id;
        this.name = name;
    }

    public Object getRootId()
    {
        return rootId;
    }
    
    public Object getId()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
}

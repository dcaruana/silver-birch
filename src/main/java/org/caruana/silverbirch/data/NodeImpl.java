package org.caruana.silverbirch.data;

import org.caruana.silverbirch.Node;


public class NodeImpl implements Node
{
    private Object id;
    private String name;

    public NodeImpl(Object id, String name)
    {
        this.id = id;
        this.name = name;
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

package org.caruana.silverbirch.queries;

import org.caruana.silverbirch.data.NodeImpl;


public class GetDrive {

    public GetDrive()
    {
    }
    
    public NodeImpl execute(String name)
    {
        return new NodeImpl(null, null, null, name);
    }
}

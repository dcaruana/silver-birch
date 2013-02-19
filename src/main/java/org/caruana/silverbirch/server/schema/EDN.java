package org.caruana.silverbirch.server.schema;

import java.util.List;

import org.caruana.silverbirch.server.statement.Statement;
import org.caruana.silverbirch.util.Data;

public class EDN implements Statement
{
    private List data;
    
    public EDN(String ednResource)
    {
        data = (List)Data.read(ednResource).get(0);
    }
    
    @Override
    public List data()
    {
        return data;
    }
}

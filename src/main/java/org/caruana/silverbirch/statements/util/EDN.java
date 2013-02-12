package org.caruana.silverbirch.statements.util;

import java.util.List;

import org.caruana.silverbirch.statements.Statement;
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

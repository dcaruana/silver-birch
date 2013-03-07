package caruana.silverbirch.server.schema;

import java.util.List;


import caruana.silverbirch.datomic.Data;
import caruana.silverbirch.server.statement.Statement;

public class EDNStatement implements Statement
{
    private List data;
    
    public EDNStatement(String ednResource)
    {
        data = (List)Data.read(ednResource).get(0);
    }
    
    @Override
    public List data()
    {
        return data;
    }
    
    @Override
    public List log()
    {
        return null;
    }
}

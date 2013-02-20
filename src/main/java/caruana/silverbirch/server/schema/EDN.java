package caruana.silverbirch.server.schema;

import java.util.List;


import caruana.silverbirch.datomic.Data;
import caruana.silverbirch.server.statement.Statement;

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

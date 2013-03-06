package caruana.silverbirch.server.log;

import java.util.HashMap;
import java.util.Map;

import caruana.silverbirch.Change;

public class ChangeData implements Change
{
    private String statement;
    private Object subject;
    private Map properties = new HashMap();

    
    public ChangeData(String statement, Object subject)
    {
        this.statement = statement;
        this.subject = subject;
    }
    
    @Override
    public String getStatement()
    {
        return statement;
    }

    @Override
    public Object getSubject()
    {
        return subject;
    }

    public Map<String, PropertyData> createProperties()
    {
        return properties;
    }

    @Override
    public Map<String, Property> getProperties()
    {
        return properties;
    }

}

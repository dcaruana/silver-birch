package caruana.silverbirch.server.log;

import caruana.silverbirch.Change.Property;

public class PropertyData implements Property
{
    private Object value;
    private Object previous;

    @Override
    public Object getValue()
    {
        return value;
    }
    
    public void setValue(Object value)
    {
        this.value = value;
    }

    @Override
    public Object getPrev()
    {
        return previous;
    }
    
    public void setPrevious(Object previous)
    {
        this.previous = previous;
    }
}

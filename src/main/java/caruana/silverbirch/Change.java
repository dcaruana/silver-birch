package caruana.silverbirch;

import java.util.Map;

public interface Change
{
    String getStatement();
    
    // TODO: need to iron out mapping between statement and subject types
    Object getSubject();
    
    Map<String, Property> getProperties();

    
    public interface Property
    {
        Object getValue();
        
        Object getPrev();
    }
}

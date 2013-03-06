package caruana.silverbirch;

import java.util.Collection;

public interface ChangeLog
{
    public static final String CREATE_DRIVE = "create_drive";
    public static final String CREATE_ITEM = "create_item";
    public static final String SET_PROPERTIES = "set_properties";
    
    
    Collection<Change> getChanges(Object transactionId);
}

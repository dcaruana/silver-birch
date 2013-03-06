package caruana.silverbirch.server.log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import caruana.silverbirch.Change;
import caruana.silverbirch.server.schema.Schema;

import com.google.inject.Inject;

import datomic.Peer;
import datomic.Util;

public class ChangeLogImpl
{
    private GetTransactionChangeLog getChangeLog;
    
    
    @Inject public void setGetTransactionChangeLog(GetTransactionChangeLog query)
    {
        this.getChangeLog = query;
    }
    
    public Collection<Change> getChangeLog(datomic.Connection conn, Object transactionId)
    {
        Map changes = new HashMap();
        Collection<List<Object>> results = getChangeLog.execute(conn, transactionId);
        
        for (List<Object> result : results)
        {
            String statement = (String)result.get(0);
            Object subject = result.get(1);
            Integer key = (statement.hashCode() + subject.hashCode()) * 37;
            
            ChangeData changeData = (ChangeData)changes.get(key);
            if (changeData == null)
            {
                changeData = new ChangeData((String)result.get(0), result.get(1));
                changes.put(key, changeData);
            }
            
            String propName = (String)result.get(2);
            Object value = result.get(3);
            boolean added = (Boolean)result.get(4);
            Map<String, PropertyData> props = changeData.createProperties();
            PropertyData prop = props.get(propName);
            if (prop == null)
            {
                prop = new PropertyData();
                props.put(propName, prop);
            }
            if (added)
            {
                prop.setValue(value);
            }
            else
            {
                prop.setPrevious(value);
            }
        }
        return changes.values();
    }
    
    
    // TODO: make createEntry methods non-static - convert when statement state is sorted i.e. conn
    
    public static List createEntry(String statement, Object subject, List<String> attrs)
    {
        return Util.list(createLog(statement, subject, attrs));
    }

    public static List createEntry(String statement, Object subject, List<String> attrs, Map<String, Object> customAttrs)
    {
        return Util.list(createLog(statement, subject, attrs), createLog(statement, subject, customAttrs));
    }

    public static Map createLog(String statement, Object subject, List<String> attrs)
    {
        // TODO: think about separate partition
        // allocate id for log entry
        Object id = Peer.tempid(Schema.DB_PARTITION_USER);
        
        // construct log entity
        Map l = new HashMap();
        l.put(Schema.DB_ID, id);
        l.put(Schema.LOG_STATEMENT, statement);
        l.put(Schema.LOG_SUBJECT, subject);
        l.put(Schema.LOG_ATTRIBUTES_ENTITY, subject);
        l.put(Schema.LOG_ATTRIBUTES, attrs);
        return l;
    }
    
    public static Map createLog(String statement, Object subject, Map<String, Object> customAttrs)
    {
        Map l = createLog(statement, subject, new ArrayList<String>(customAttrs.keySet()));
        l.put(Schema.LOG_ATTRIBUTES_ENTITY, l.get(Schema.DB_ID));
        l.putAll(customAttrs);
        return l;
    }
}

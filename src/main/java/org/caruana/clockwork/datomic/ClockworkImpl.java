package org.caruana.clockwork.datomic;

import org.caruana.clockwork.Clockwork;
import org.caruana.clockwork.ClockworkException.ClockworkConnectionException;
import org.caruana.clockwork.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datomic.Peer;

public class ClockworkImpl implements Clockwork {

    final Logger logger = LoggerFactory.getLogger(ClockworkImpl.class);
    
    private static String PROTOCOL = "datomic:";


    public boolean createRepo(String repo)
    {
        if (logger.isDebugEnabled())
            logger.debug("Creating repository {}", repo);
        
        boolean created = Peer.createDatabase(PROTOCOL + repo);
        
        if (logger.isDebugEnabled())
        {
            if (created)
                logger.debug("Created repository {}", repo);
            else
                logger.debug("Repository {} already exists", repo);
        }
        
        return created;
    }
    
    public Connection connect(String repo)
        throws ClockworkConnectionException
    {
        datomic.Connection conn;

        if (logger.isDebugEnabled())
            logger.debug("Connecting to repository {}", repo);

        try
        {
            conn = Peer.connect(PROTOCOL + repo);
        
            if (logger.isDebugEnabled())
                logger.debug("Connected to repository {}", repo);
            
            return new ConnectionImpl(conn);
        }
        catch(clojure.lang.ExceptionInfo e)
        {
            throw new ClockworkConnectionException(e);
        }
    }

}

package org.caruana.silverbirch.datomic;

import org.caruana.silverbirch.SilverBirch;
import org.caruana.silverbirch.Connection;
import org.caruana.silverbirch.SilverBirchException.SilverBirchConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import datomic.Peer;

public class SilverBirchImpl implements SilverBirch {

    final Logger logger = LoggerFactory.getLogger(SilverBirchImpl.class);
    
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
        throws SilverBirchConnectionException
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
            throw new SilverBirchConnectionException(e);
        }
    }

}

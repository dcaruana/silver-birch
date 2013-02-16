package org.caruana.silverbirch.server;

import org.caruana.silverbirch.Connection;
import org.caruana.silverbirch.SilverBirch;
import org.caruana.silverbirch.SilverBirchException.SilverBirchConnectionException;
import org.caruana.silverbirch.server.connection.ConnectionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import datomic.Peer;

public class SilverBirchImpl implements SilverBirch {

    final Logger logger = LoggerFactory.getLogger(SilverBirchImpl.class);
    
    private static String PROTOCOL = "datomic:";

    @Inject private Bootstrap bootstrap;
    @Inject private StorageImpl storage;
    
    public StorageImpl getStorage()
    {
        return storage;
    }

    @Override
    public boolean createRepo(String repo)
    {
        if (logger.isDebugEnabled())
            logger.debug("Creating repository {}", repo);
        
        boolean created = Peer.createDatabase(PROTOCOL + repo);
        if (created)
        {
            ConnectionImpl conn = internalConnect(repo);
            bootstrap.bootstrap(conn);
            
            if (logger.isDebugEnabled())
                logger.debug("Created repository {}", repo);
        }
        else
        {            
            if (logger.isDebugEnabled())
                logger.debug("Repository {} already exists", repo);
        }
        
        return created;
    }

    @Override
    public Connection connect(String repo)
        throws SilverBirchConnectionException
    {
        return internalConnect(repo);
    }
    
    public ConnectionImpl internalConnect(String repo)
    {
        datomic.Connection conn;

        try
        {
            if (logger.isDebugEnabled())
                logger.debug("Connected to repository {}", repo);
            
            conn = Peer.connect(PROTOCOL + repo);
        
            if (logger.isDebugEnabled())
                logger.debug("Connected to repository {}", repo);
            
            return new ConnectionImpl(this, conn);
        }
        catch(clojure.lang.ExceptionInfo e)
        {
            throw new SilverBirchConnectionException(e);
        }
    }

}

package caruana.silverbirch.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caruana.silverbirch.Connection;
import caruana.silverbirch.SilverBirch;
import caruana.silverbirch.SilverBirchException.SilverBirchConnectionException;
import caruana.silverbirch.server.items.ItemsImpl;

import com.google.inject.Inject;

import datomic.Peer;

public class SilverBirchImpl implements SilverBirch {

    final Logger logger = LoggerFactory.getLogger(SilverBirchImpl.class);
    
    private static String PROTOCOL = "datomic:";

    @Inject private Bootstrap bootstrap;
    @Inject private ItemsImpl items;
    
    public ItemsImpl getItems()
    {
        return items;
    }

    @Override
    public boolean createRepo(String repo)
    {
        if (logger.isDebugEnabled())
            logger.debug("Creating repository {}", repo);
        
        boolean created = Peer.createDatabase(PROTOCOL + repo);
        if (created)
        {
            datomic.Connection conn = Peer.connect(PROTOCOL + repo);
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
        return createConnection(repo);
    }
    
    public TransactionImpl createTransaction(String repo)
    {
        datomic.Connection conn;

        try
        {
            if (logger.isDebugEnabled())
                logger.debug("Creating transaction for {}", repo);
            
            conn = Peer.connect(PROTOCOL + repo);
            TransactionImpl transaction = new TransactionImpl(conn);
        
            if (logger.isDebugEnabled())
                logger.debug("Transaction created for {}", repo);
            
            return transaction;
        }
        catch(clojure.lang.ExceptionInfo e)
        {
            throw new SilverBirchConnectionException(e);
        }
    }
    
    public ConnectionImpl createConnection(String repo)
    {
        try
        {
            TransactionImpl transaction = createTransaction(repo);
            TransactionalItems transactionalItems = new TransactionalItems(items, transaction);
            return new ConnectionImpl(transactionalItems, transaction);
        }
        catch(clojure.lang.ExceptionInfo e)
        {
            throw new SilverBirchConnectionException(e);
        }
    }

}

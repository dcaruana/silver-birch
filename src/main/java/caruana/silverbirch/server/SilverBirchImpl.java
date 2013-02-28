package caruana.silverbirch.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caruana.silverbirch.Connection;
import caruana.silverbirch.SilverBirch;
import caruana.silverbirch.SilverBirchException.SilverBirchConnectionException;
import caruana.silverbirch.server.items.ItemsImpl;
import caruana.silverbirch.server.repo.RepoStore;

import com.google.inject.Inject;


public class SilverBirchImpl implements SilverBirch {

    final Logger logger = LoggerFactory.getLogger(SilverBirchImpl.class);
    
    @Inject private RepoStore repoStore;
    @Inject private Bootstrap bootstrap;
    @Inject private ItemsImpl items;
    

    @Override
    public boolean createRepo(String repo)
    {
        if (logger.isDebugEnabled())
            logger.debug("Creating repository {}", repo);
        
        boolean created = repoStore.create(repo);
        if (created)
        {
            datomic.Connection conn = repoStore.connect(repo);
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
            
            conn = repoStore.connect(repo);
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

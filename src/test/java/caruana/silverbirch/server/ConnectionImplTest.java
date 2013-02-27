package caruana.silverbirch.server;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.server.ConnectionImpl;
import caruana.silverbirch.server.TransactionImpl;
import caruana.silverbirch.server.TransactionalItems;
import caruana.silverbirch.server.items.GetDrive;
import caruana.silverbirch.server.items.ItemsImpl;

import datomic.Peer;


public class ConnectionImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(ConnectionImplTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private ConnectionImpl connection;
    private TransactionImpl transaction;
    private TransactionalItems transactionalItems;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("ConnectionImplTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        Peer.createDatabase(repo);
        datomic.Connection conn = Peer.connect(repo);
        ItemsImpl items = new ItemsImpl();
        items.setGetDrive(new GetDrive());
        transaction = new TransactionImpl(conn);
        transactionalItems = new TransactionalItems(items, transaction);
        connection = new ConnectionImpl(transactionalItems, transaction);
    }
    
    @Test
    public void services()
    {
        profiler.start("transaction()");
        assertNotNull(connection.transaction());
        profiler.start("items()");
        assertNotNull(connection.items());
        profiler.stop().log();
    }

}
package caruana.silverbirch.server;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.server.ConnectionImpl;
import caruana.silverbirch.server.TransactionImpl;
import caruana.silverbirch.server.TransactionalStorage;
import caruana.silverbirch.server.storage.GetDrive;
import caruana.silverbirch.server.storage.StorageImpl;

import com.google.inject.Guice;
import com.google.inject.Injector;

import datomic.Peer;


public class ConnectionImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(ConnectionImplTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private ConnectionImpl connection;
    private TransactionImpl transaction;
    private TransactionalStorage transactionalStorage;

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
        StorageImpl storage = new StorageImpl();
        storage.setGetDrive(new GetDrive());
        transaction = new TransactionImpl(conn);
        transactionalStorage = new TransactionalStorage(storage, transaction);
        connection = new ConnectionImpl(transactionalStorage, transaction);
    }
    
    @Test
    public void services()
    {
        profiler.start("transaction()");
        assertNotNull(connection.transaction());
        profiler.start("storage()");
        assertNotNull(connection.storage());
        profiler.stop().log();
    }

}
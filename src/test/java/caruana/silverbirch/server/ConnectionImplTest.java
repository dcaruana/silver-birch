package caruana.silverbirch.server;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.server.blobs.BlobsImpl;
import caruana.silverbirch.server.blobs.GetBlobQuery;
import caruana.silverbirch.server.blobs.InMemoryBlobStore;
import caruana.silverbirch.server.items.GetDriveQuery;
import caruana.silverbirch.server.items.ItemsImpl;
import caruana.silverbirch.server.log.ChangeLogImpl;
import caruana.silverbirch.server.log.GetChangeLogQuery;
import caruana.silverbirch.server.repo.InMemoryRepoStore;


public class ConnectionImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(ConnectionImplTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private ConnectionImpl connection;
    private TransactionImpl transaction;
    private TransactionalItems transactionalItems;
    private TransactionalBlobs transactionalBlobs;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("ConnectionImplTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        InMemoryRepoStore repoStore = new InMemoryRepoStore();
        repoStore.create(repo);
        datomic.Connection conn = repoStore.connect(repo);
        ItemsImpl items = new ItemsImpl();
        items.setGetDrive(new GetDriveQuery());
        BlobsImpl blobs = new BlobsImpl();
        blobs.setGetBlob(new GetBlobQuery());
        blobs.setBlobStore(new InMemoryBlobStore());
        ChangeLogImpl log = new ChangeLogImpl();
        log.setGetTransactionChangeLog(new GetChangeLogQuery());
        transaction = new TransactionImpl(conn);
        connection = new ConnectionImpl(transaction);
        connection.setTransactionalItems(new TransactionalItems(items, transaction));
        connection.setTransactionalBlobs(new TransactionalBlobs(blobs, transaction));
        connection.setTransactionalChangeLog(new TransactionalChangeLog(log, transaction));
    }
    
    @Test
    public void services()
    {
        profiler.start("transaction()");
        assertNotNull(connection.transaction());
        profiler.start("items()");
        assertNotNull(connection.items());
        profiler.start("blobs()");
        assertNotNull(connection.blobs());
        profiler.start("changelog()");
        assertNotNull(connection.changelog());
        profiler.stop().log();
    }

}
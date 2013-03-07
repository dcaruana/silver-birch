package caruana.silverbirch.server;


import caruana.silverbirch.SilverBirch;
import caruana.silverbirch.server.blobs.BlobStore;
import caruana.silverbirch.server.blobs.BlobsImpl;
import caruana.silverbirch.server.blobs.CreateBlobStatementFactory;
import caruana.silverbirch.server.blobs.GetBlobQuery;
import caruana.silverbirch.server.blobs.InMemoryBlobStore;
import caruana.silverbirch.server.items.CreateDriveStatementFactory;
import caruana.silverbirch.server.items.CreateItemStatementFactory;
import caruana.silverbirch.server.items.GetDriveQuery;
import caruana.silverbirch.server.items.GetPropertiesQuery;
import caruana.silverbirch.server.items.ItemsImpl;
import caruana.silverbirch.server.items.ListDrivesQuery;
import caruana.silverbirch.server.items.SetPropertiesStatementFactory;
import caruana.silverbirch.server.log.ChangeLogImpl;
import caruana.silverbirch.server.log.GetChangeLogQuery;
import caruana.silverbirch.server.repo.InMemoryRepoStore;
import caruana.silverbirch.server.repo.RepoStore;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class SilverBirchModule extends AbstractModule 
{
    @Override
    protected void configure()
    {
        // server
        bind(SilverBirch.class).to(SilverBirchImpl.class);
        bind(RepoStore.class).to(InMemoryRepoStore.class).in(Singleton.class);
        bind(Bootstrap.class).in(Singleton.class);
        
        // items
        bind(ItemsImpl.class).in(Singleton.class);
        bind(CreateDriveStatementFactory.class).in(Singleton.class);
        bind(CreateItemStatementFactory.class).in(Singleton.class);
        bind(GetDriveQuery.class).in(Singleton.class);
        bind(ListDrivesQuery.class).in(Singleton.class);
        bind(GetPropertiesQuery.class).in(Singleton.class);
        bind(SetPropertiesStatementFactory.class).in(Singleton.class);

        // blobs
        bind(BlobStore.class).to(InMemoryBlobStore.class).in(Singleton.class);
        bind(BlobsImpl.class).in(Singleton.class);
        bind(CreateBlobStatementFactory.class).in(Singleton.class);
        bind(GetBlobQuery.class).in(Singleton.class);
        
        // change log
        bind(ChangeLogImpl.class).in(Singleton.class);
        bind(GetChangeLogQuery.class).in(Singleton.class);
    }
    
}

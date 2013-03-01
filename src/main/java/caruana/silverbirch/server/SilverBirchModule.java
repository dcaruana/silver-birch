package caruana.silverbirch.server;


import caruana.silverbirch.SilverBirch;
import caruana.silverbirch.server.blobs.BlobStore;
import caruana.silverbirch.server.blobs.BlobsImpl;
import caruana.silverbirch.server.blobs.GetBlob;
import caruana.silverbirch.server.blobs.InMemoryBlobStore;
import caruana.silverbirch.server.items.GetDrive;
import caruana.silverbirch.server.items.GetProperties;
import caruana.silverbirch.server.items.ItemsImpl;
import caruana.silverbirch.server.items.ListDrives;
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
        bind(ItemsImpl.class).in(Singleton.class);
        bind(BlobStore.class).to(InMemoryBlobStore.class).in(Singleton.class);
        bind(BlobsImpl.class).in(Singleton.class);
        bind(Bootstrap.class).in(Singleton.class);
        
        // queries
        bind(GetDrive.class).in(Singleton.class);
        bind(ListDrives.class).in(Singleton.class);
        bind(GetProperties.class).in(Singleton.class);
        bind(GetBlob.class).in(Singleton.class);
    }
    
}

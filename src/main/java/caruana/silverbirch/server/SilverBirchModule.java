package caruana.silverbirch.server;


import caruana.silverbirch.SilverBirch;
import caruana.silverbirch.server.storage.GetDrive;
import caruana.silverbirch.server.storage.StorageImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class SilverBirchModule extends AbstractModule 
{
    @Override
    protected void configure()
    {
        // server
        bind(SilverBirch.class).to(SilverBirchImpl.class);
        bind(StorageImpl.class).in(Singleton.class);
        bind(Bootstrap.class).in(Singleton.class);
        
        // queries
        bind(GetDrive.class).in(Singleton.class);
    }
    
}

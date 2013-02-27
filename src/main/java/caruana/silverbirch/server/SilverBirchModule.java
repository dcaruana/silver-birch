package caruana.silverbirch.server;


import caruana.silverbirch.SilverBirch;
import caruana.silverbirch.server.items.GetDrive;
import caruana.silverbirch.server.items.ListDrives;
import caruana.silverbirch.server.items.ItemsImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class SilverBirchModule extends AbstractModule 
{
    @Override
    protected void configure()
    {
        // server
        bind(SilverBirch.class).to(SilverBirchImpl.class);
        bind(ItemsImpl.class).in(Singleton.class);
        bind(Bootstrap.class).in(Singleton.class);
        
        // queries
        bind(GetDrive.class).in(Singleton.class);
        bind(ListDrives.class).in(Singleton.class);
    }
    
}

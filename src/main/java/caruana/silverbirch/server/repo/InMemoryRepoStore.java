package caruana.silverbirch.server.repo;

import datomic.Peer;


public class InMemoryRepoStore implements RepoStore 
{
    private static String PROTOCOL = "datomic:mem://";

    
    @Override
    public boolean create(String name)
    {
        return Peer.createDatabase(PROTOCOL + name);
    }
    
    @Override
    public datomic.Connection connect(String name)
    {
        return Peer.connect(PROTOCOL + name);
    }

}

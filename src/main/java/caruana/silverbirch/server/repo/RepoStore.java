package caruana.silverbirch.server.repo;


public interface RepoStore 
{

    boolean create(String name);
    
    datomic.Connection connect(String name);

}

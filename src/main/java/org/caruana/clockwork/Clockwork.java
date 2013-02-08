package org.caruana.clockwork;


public interface Clockwork {
    
    boolean createRepo(String repo);
    
    Connection connect(String repo);
    
}

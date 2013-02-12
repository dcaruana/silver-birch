package org.caruana.silverbirch;

public interface Connection {

    Transaction transaction();

    Storage storage();
    
}

package caruana.silverbirch;

public interface Connection {

    Transaction transaction();

    Items items();
 
    Blobs blobs();
    
    ChangeLog changelog();
}

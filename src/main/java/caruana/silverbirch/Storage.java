package caruana.silverbirch;

public interface Storage {

    Node createDrive(String name);
    
    Node getDrive(String name);
    
//    Node getDrive(Node node);
//
//    Node[] listDrives();
//
//    Node getNode(Node drive, String path);
//    Node getNode(long id);
//    Node[] getParents(Node node);
//
//    Node[] getChildren(Node node);
//
    Node createNode(Node parent, String name);
//
//    void move(Node node, Node parent);
//    void rename(Node node, String name);
//
//    void link(Node node, Node parent);
//    void unlink(Node node, Node parent);
//
//    void deleteNode(Node node);
}

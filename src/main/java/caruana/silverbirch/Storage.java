package caruana.silverbirch;

import java.util.List;

public interface Storage {

    Node createDrive(String name);
    
    Node getDrive(String name);
    
//    Node getDrive(Node node);
//
    List<Node> listDrives();
//
//    Node getNode(Node drive, String path);
//    Node getNode(long id);
//    Node[] getParents(Node node);
//
    List<Node> listChildren(Node node);
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

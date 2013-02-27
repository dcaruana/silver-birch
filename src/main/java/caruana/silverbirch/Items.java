package caruana.silverbirch;

import java.util.List;

public interface Items {

    Item createDrive(String name);
    
    Item getDrive(String name);
    
//    Item getDrive(Item item);
//
    List<Item> listDrives();
//
//    Item getItem(Item drive, String path);
//    Item getItem(long id);
//    Item[] getParents(Item item);
//
    List<Item> listChildren(Item item);
//
    Item createItem(Item parent, String name);
//
//    void move(Item item, Item parent);
//    void rename(Item item, String name);
//
//    void link(Item item, Item parent);
//    void unlink(Item item, Item parent);
//
//    void deleteItem(Item item);
}

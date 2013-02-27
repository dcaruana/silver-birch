package caruana.silverbirch.server.items;

import java.util.UUID;

import caruana.silverbirch.Item;


public class ItemData implements Item
{
    private UUID uniqueId;
    private Object rootId;
    private Object driveId;
    private Object id;
    private String name;

    public ItemData(UUID uniqueId, Object rootId, Object driveId, Object id, String name)
    {
        this.uniqueId = uniqueId;
        this.rootId = rootId;
        this.driveId = driveId;
        this.id = id;
        this.name = name;
    }

    @Override
    public UUID getUniqueId()
    {
        return uniqueId;
    }
    
    @Override
    public Object getRootId()
    {
        return rootId;
    }

    @Override
    public Object getDriveId()
    {
        return driveId;
    }
    
    @Override
    public Object getId()
    {
        return id;
    }
    
    @Override
    public String getName()
    {
        return name;
    }

}

package caruana.silverbirch.server.storage;

import com.google.inject.Inject;

public class StorageStats
{
    private GetDriveCount getDriveCount;
    private GetNodeCount getNodeCount;
    
    @Inject public void setGetDriveCount(GetDriveCount getDriveCount)
    {
        this.getDriveCount = getDriveCount;
    }

    @Inject public void setGetNodeCount(GetNodeCount getNodeCount)
    {
        this.getNodeCount = getNodeCount;
    }

    
    public int getDriveCount(datomic.Connection conn)
    {
        return getDriveCount.execute(conn);
    }
    
    public int getNodeCount(datomic.Connection conn)
    {
        return getNodeCount.execute(conn);
    }
    
}

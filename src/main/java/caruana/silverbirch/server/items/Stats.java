package caruana.silverbirch.server.items;

import com.google.inject.Inject;

public class Stats
{
    private GetDriveCountQuery getDriveCount;
    private GetItemCountQuery getItemCount;
    
    @Inject public void setGetDriveCount(GetDriveCountQuery getDriveCount)
    {
        this.getDriveCount = getDriveCount;
    }

    @Inject public void setGetItemCount(GetItemCountQuery getItemCount)
    {
        this.getItemCount = getItemCount;
    }

    
    public int getDriveCount(datomic.Connection conn)
    {
        return getDriveCount.execute(conn);
    }
    
    public int getItemCount(datomic.Connection conn)
    {
        return getItemCount.execute(conn);
    }
    
}

package caruana.silverbirch.server.items;

import com.google.inject.Inject;

public class Stats
{
    private GetDriveCount getDriveCount;
    private GetItemCount getItemCount;
    
    @Inject public void setGetDriveCount(GetDriveCount getDriveCount)
    {
        this.getDriveCount = getDriveCount;
    }

    @Inject public void setGetItemCount(GetItemCount getItemCount)
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

package caruana.silverbirch.server.blobs;


import java.io.InputStream;
import java.util.UUID;

import caruana.silverbirch.Blob;

import com.google.inject.Inject;

public class BlobsImpl
{
    private BlobStore store;
    private GetBlob getBlob;

    @Inject public void setBlobStore(BlobStore store)
    {
        this.store = store;
    }
    
    @Inject public void setGetBlob(GetBlob query)
    {
        this.getBlob = query;
    }

    
    public CreateBlob create(datomic.Connection conn, InputStream stream, String mimetype)
    {
        Stream s = store.write(stream);
        CreateBlob statement = new CreateBlob(conn, s, mimetype);
        return statement;
    }

    public InputStream read(UUID streamId)
    {
        return store.read(streamId);
    }
  
    public Blob get(datomic.Connection conn, Object id)
    {
        return getBlob.execute(conn, id);
    }
    
}

package caruana.silverbirch.server.blobs;


import java.io.InputStream;
import java.util.UUID;

import caruana.silverbirch.Blob;
import caruana.silverbirch.server.blobs.CreateBlobStatementFactory.CreateBlobStatement;

import com.google.inject.Inject;

public class BlobsImpl
{
    private BlobStore store;
    
    private CreateBlobStatementFactory createBlob;
    private GetBlobQuery getBlob;

    @Inject public void setBlobStore(BlobStore store)
    {
        this.store = store;
    }

    @Inject public void setCreateBlob(CreateBlobStatementFactory statement)
    {
        this.createBlob = statement;
    }

    @Inject public void setGetBlob(GetBlobQuery query)
    {
        this.getBlob = query;
    }

    
    public CreateBlobStatement create(InputStream stream, String mimetype)
    {
        Stream s = store.write(stream);
        CreateBlobStatement statement = createBlob.statement(s, mimetype);
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

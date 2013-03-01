package caruana.silverbirch.server;


import java.io.InputStream;
import java.util.UUID;

import caruana.silverbirch.Blob;
import caruana.silverbirch.Blobs;
import caruana.silverbirch.server.blobs.BlobsImpl;
import caruana.silverbirch.server.blobs.CreateBlob;

public class TransactionalBlobs implements Blobs
{
    private BlobsImpl blobs;
    private TransactionImpl transaction;
    
    public TransactionalBlobs(BlobsImpl blobs, TransactionImpl transaction)
    {
        this.blobs = blobs;
        this.transaction = transaction;
    }
    
    @Override
    public Blob create(InputStream stream, String mimetype)
    {
        CreateBlob statement = blobs.create(transaction.getConnection(), stream, mimetype);
        transaction.addStatement(statement);
        return statement.getBlob();
    }

    @Override
    public InputStream read(UUID streamId)
    {
        return blobs.read(streamId);
    }

    @Override
    public Blob get(Object id)
    {
        return blobs.get(transaction.getConnection(), id);
    }

}

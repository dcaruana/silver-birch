package caruana.silverbirch.server.blobs;

import java.util.List;
import java.util.Map;

import caruana.silverbirch.server.schema.Schema;
import caruana.silverbirch.server.statement.Statement;
import datomic.Peer;
import datomic.Util;

public class CreateBlobStatementFactory
{
    public CreateBlobStatement statement(Stream stream, String mimetype)
    {
        return new CreateBlobStatement(stream, mimetype);
    }
    
    public class CreateBlobStatement implements Statement
    {
        private BlobData blob;
        
        public CreateBlobStatement(Stream stream, String mimetype)
        {
            Object id = Peer.tempid(Schema.DB_PARTITION_USER);
            // TODO: mimetype guessing (is that here?)
            blob = new BlobData(id, stream.getStreamId(), stream.getLength(), mimetype);
        }
    
        public BlobData getBlob()
        {
            return blob;
        }
        
        @Override
        public List data()
        {
            Map m = Util.map(
                        Schema.DB_ID, blob.getId(),
                        Schema.SYSTEM_UUID, blob.getStreamId(),
                        Schema.BLOB_LENGTH, blob.getLength(), 
                        Schema.BLOB_MIMETYPE, blob.getMimetype()
                    );
            List d = Util.list(m);
            return d;
        }
    
        @Override
        public List log()
        {
            return null;
        }
    }
}

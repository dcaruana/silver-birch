package caruana.silverbirch;

import java.io.InputStream;
import java.util.UUID;

public interface Blobs
{
    Blob create(InputStream stream, String mimetype);

    InputStream read(UUID streamId);
  
    Blob get(Object id);

}

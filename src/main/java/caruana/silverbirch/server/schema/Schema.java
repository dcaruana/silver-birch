package caruana.silverbirch.server.schema;

public interface Schema
{

    public final static String SYS_NS = "sys";
    public final static String USR_NS = "usr";
    
    public final static String DB_ID = ":db/id";
    public final static String DB_IDENT = ":db/ident";
    public final static String DB_FN = ":db/fn";
    public final static String DB_PARTITION_USER = ":db.part/user";

    public static final String SYSTEM_UUID = ":" + SYS_NS + ".core/uuid";
    public static final String SYSTEM_UNIQUE_NAME = ":" + SYS_NS + ".core/unique_name";
    
    public static final String LOG_STATEMENT = ":" + SYS_NS + ".log/statement";
    public static final String LOG_SUBJECT = ":" + SYS_NS + ".log/subject";
    public static final String LOG_ATTRIBUTES_ENTITY = ":" + SYS_NS + ".log/attrs.entity";
    public static final String LOG_ATTRIBUTES = ":" + SYS_NS + ".log/attrs";
    
    public static final String ITEM_NAME = ":" + SYS_NS + ".item/name";
    public static final String ITEM_ROOT = ":" + SYS_NS + ".item/root";
    public static final String ITEM_PARENTS = ":" + SYS_NS + ".item/parents";

    public static final String BLOB_LENGTH = ":" + SYS_NS + ".blob/length";
    public static final String BLOB_MIMETYPE = ":" + SYS_NS + ".blob/mimetype";
    
    
    // TODO: temporary while lacking user model support
    public static final String ITEM_CONTENT = ":" + USR_NS + ".item/content";
}

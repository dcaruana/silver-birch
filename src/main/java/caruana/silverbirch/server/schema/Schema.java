package caruana.silverbirch.server.schema;

public interface Schema {

    public final static String DB_ID = ":db/id";
    public final static String DB_IDENT = ":db/ident";
    public final static String DB_FN = ":db/fn";
    public final static String DB_PARTITION_USER = ":db.part/user";

    public static final String SYSTEM_UUID = ":system/uuid";
    public static final String SYSTEM_UNIQUE_NAME = ":system/unique_name";
    
    public static final String ITEM_NAME = ":item/name";
    public static final String ITEM_ROOT = ":item/root";
    public static final String ITEM_PARENTS = ":item/parents";

    public static final String BLOB_LENGTH = ":blob/length";
    public static final String BLOB_MIMETYPE = ":blob/mimetype";
}

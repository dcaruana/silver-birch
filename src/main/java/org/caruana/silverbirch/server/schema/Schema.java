package org.caruana.silverbirch.server.schema;

public interface Schema {

    public final static String DB_ID = ":db/id";
    public final static String DB_IDENT = ":db/ident";
    public final static String DB_FN = ":db/fn";
    public final static String DB_PARTITION_USER = ":db.part/user";

    public static final String SYSTEM_UUID = ":system/uuid";
    public static final String SYSTEM_UNIQUE_NAME = ":system/unique_name";
    
    public static final String NODE_NAME = ":node/name";
    public static final String NODE_ROOT = ":node/root";
    public static final String NODE_PARENTS = ":node/parents";

}

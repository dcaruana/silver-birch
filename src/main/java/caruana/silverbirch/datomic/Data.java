package caruana.silverbirch.datomic;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import clojure.lang.Keyword;
import clojure.lang.Symbol;
import datomic.Connection;
import datomic.Datom;
import datomic.Util;

public class Data
{

    public static List read(String path)
    {
        InputStream stream = Data.class.getResourceAsStream(path);
        if (stream == null)
            throw new RuntimeException("File not found: " + path);
        
        InputStreamReader reader = new InputStreamReader(stream);
        List data = Util.readAll(reader);
        return data;
    }

    public static void print(Object data)
    {
        print(data, 0);
    }
    
    
    
    private static void print(Object o, int level)
    {
        if (o == null)
        {
            System.out.println("<null>");
        }
        else if (o instanceof String)
        {
            System.out.println((String)o);
        }
        else if (o instanceof Long)
        {
            System.out.println(o);
        }
        else if (o instanceof Keyword || o instanceof Symbol)
        {
            System.out.println(o);
        }
        else if (o instanceof Datom)
        {
            Datom d = (Datom)o;
            System.out.println("$e=" + d.e() + ",a=" + d.a() + ",v=" + d.v() + ",tx=" + d.tx() + ",added=" + d.added());
        }
        else if (o instanceof Map.Entry)
        {
            System.out.print(((Map.Entry)o).getKey() + " => ");
            print(((Map.Entry)o).getValue(), level);
        }
        else if (o instanceof Object[])
        {
            System.out.println("[");
            for (Object n : (Object[])o)
            {
                printIndent((level + 1) * 2);
                print(n, level + 1);
            }
            printIndent(level * 2);
            System.out.println("]");
        }
        else if (o instanceof List)
        {
            System.out.println("[");
            for (Object n : (List)o)
            {
                printIndent((level + 1) * 2);
                print(n, level + 1);
            }
            printIndent(level * 2);
            System.out.println("]");
        }
        else if (o instanceof Set)
        {
            System.out.println("[");
            for (Object n : (Set)o)
            {
                printIndent((level + 1) * 2);
                print(n, level + 1);
            }
            printIndent(level * 2);
            System.out.println("]");
        }
        else if (o instanceof Map)
        {
            System.out.println("{");
            for (Map.Entry e : (Set<Map.Entry>)((Map)o).entrySet())
            {
                printIndent((level + 1) * 2);
                print(e, level + 1);
            }
            printIndent(level * 2);
            System.out.println("}");
        }
        else
        {
            System.out.println(o.toString() + " <" + o.getClass().getCanonicalName() + ">");
        }
    }
    
    
    public static void print(Connection conn)
    {
        System.out.println("Connection[db=" + conn.db().id() + ",asOf=" + conn.db().asOfT() + "]");
    }

    public static void print(Future<Map> future)
        throws ExecutionException, InterruptedException
    {
        System.out.println("Transaction isDone=" + future.isDone() + ":");
        print(future.get().get(Connection.TX_DATA));
    }

    private static void printIndent(int n)
    {
        for (int i = 0; i < n; i++)
            System.out.print(" ");
    }
    
}

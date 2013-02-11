package org.caruana.silverbirch.datomic.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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

}

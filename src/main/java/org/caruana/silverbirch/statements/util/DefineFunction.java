package org.caruana.silverbirch.statements.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.caruana.silverbirch.SilverBirchException;
import org.caruana.silverbirch.statements.Statement;
import org.caruana.silverbirch.util.DatomicImpl;

import datomic.Peer;
import datomic.Util;
import datomic.functions.Fn;


public class DefineFunction implements Statement
{
    private String name;
    private String[] params;
    private String code;
    
    public DefineFunction(String name, String[] params, String codeResource)
    {
        this.name = name;
        this.params = params;
                
        InputStream stream = DefineFunction.class.getResourceAsStream(codeResource);
        if (stream == null)
            throw new SilverBirchException("File not found: " + codeResource);

        try
        {
            code = IOUtils.toString(stream, "UTF-8");
        }
        catch(IOException e)
        {
            throw new SilverBirchException("Failed to read " + codeResource, e);
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }
    }
    
    @Override
    public List data()
    {
        Fn func = Peer.function(Util.map(
                "lang", "java",
                "params", Util.list(params),
                "code", code));
        
        Map m = Util.map(
                    DatomicImpl.DB_ID, Peer.tempid(DatomicImpl.DB_PARTITION_USER),
                    DatomicImpl.DB_IDENT, name,
                    DatomicImpl.DB_FN, func);
        
        return Util.list(m);
    }
    
}

package org.caruana.silverbirch.statements.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.caruana.silverbirch.SilverBirchException;
import org.caruana.silverbirch.SilverBirchException.SilverBirchFunctionException;
import org.caruana.silverbirch.statements.Statement;
import org.caruana.silverbirch.util.DatomicImpl;
import org.codehaus.commons.compiler.CompileException;

import datomic.Peer;
import datomic.Util;
import datomic.functions.Fn;
import datomic.functions.Fn0;
import datomic.functions.Fn1;
import datomic.functions.Fn10;
import datomic.functions.Fn2;
import datomic.functions.Fn3;
import datomic.functions.Fn4;
import datomic.functions.Fn5;
import datomic.functions.Fn6;
import datomic.functions.Fn7;
import datomic.functions.Fn8;
import datomic.functions.Fn9;


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
        
        // note: trigger compile up front
        try
        {
            forceCompile(func);
        }
        catch(CompileException e)
        {
            throw new SilverBirchFunctionException(e);
        }
        
        Map m = Util.map(
                    DatomicImpl.DB_ID, Peer.tempid(DatomicImpl.DB_PARTITION_USER),
                    DatomicImpl.DB_IDENT, name,
                    DatomicImpl.DB_FN, func);
        
        return Util.list(m);
    }

    private void forceCompile(Fn func)
        throws CompileException
    {
        List<String> p = func.params();
        int size = p.size();
        if (size == 0)
            ((Fn0)func).invoke();
        else if (size == 1)
            ((Fn1)func).invoke(p);
        else if (size == 2)
            ((Fn2)func).invoke(p, p);
        else if (size == 3)
            ((Fn3)func).invoke(p, p, p);
        else if (size == 4)
            ((Fn4)func).invoke(p, p, p, p);
        else if (size == 5)
            ((Fn5)func).invoke(p, p, p, p, p);
        else if (size == 6)
            ((Fn6)func).invoke(p, p, p, p, p, p);
        else if (size == 7)
            ((Fn7)func).invoke(p, p, p, p, p, p, p);
        else if (size == 8)
            ((Fn8)func).invoke(p, p, p, p, p, p, p, p);
        else if (size == 9)
            ((Fn9)func).invoke(p, p, p, p, p, p, p, p, p);
        else if (size == 10)
            ((Fn10)func).invoke(p, p, p, p, p, p, p, p, p, p);
    }
    
}

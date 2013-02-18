package org.caruana.silverbirch.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.caruana.silverbirch.SilverBirchException.SilverBirchValidatorException;


public class NodeNameValidator
{
    // todo: expand on this list of invalid chars
    private static final Pattern p = Pattern.compile("[\\\\/:*?\"<>|]");
    
    public boolean isValid(String name)
    {
        Matcher m = p.matcher(name);
        boolean matches = m.find();
        
        return !matches;
    }

    public void checkValid(String name)
        throws SilverBirchValidatorException
    {
        if (!isValid(name))
        {
            throw new SilverBirchValidatorException("Node name " + name + " is not valid");
        }
    }
}

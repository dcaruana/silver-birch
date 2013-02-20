package caruana.silverbirch.server.storage;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import caruana.silverbirch.SilverBirchException.SilverBirchValidatorException;
import caruana.silverbirch.server.storage.NodeName.Validator;


public class NodeNameTest
{
    private static final String[] valid = new String[]
            {
                "a",
                "a.doc",
                "a b.doc"
            };

    private static final String[] invalid = new String[]
            {
                "\\",
                "/",
                ":",
                "*",
                "?",
                "\"",
                "<",
                ">",
                "|"
            };

    private Validator v;
    
    @Before
    public void init()
    {
        v = new Validator();
    }
    
    @Test
    public void isValidNames()
    {
        for (String name : valid)
        {
            assertTrue(v.isValid(name));
        }
    }

    @Test
    public void checkValidNames()
    {
        for (String name : valid)
        {
            try
            {
                v.checkValid(name);
            }
            catch(SilverBirchValidatorException e)
            {
                fail("Thrown exception for valid name " + name);
            }
        }
    }

    @Test
    public void isInvalidNames()
    {
        for (String name : invalid)
        {
            assertFalse(v.isValid(name));
        }
    }
    
    @Test
    public void checkInvalidNames()
    {
        for (String name : invalid)
        {
            try
            {
                v.checkValid(name);
                fail("Exception not thrown for invalid name " + name);
            }
            catch(SilverBirchValidatorException e)
            {
            }
        }
    }
    
}


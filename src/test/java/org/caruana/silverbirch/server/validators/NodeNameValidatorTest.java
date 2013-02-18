package org.caruana.silverbirch.server.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.caruana.silverbirch.SilverBirchException.SilverBirchValidatorException;
import org.caruana.silverbirch.validators.NodeNameValidator;
import org.junit.Before;
import org.junit.Test;


public class NodeNameValidatorTest
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

    private NodeNameValidator v;
    
    @Before
    public void initValidator()
    {
        v = new NodeNameValidator();
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


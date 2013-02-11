package org.caruana.silverbirch.datomic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.caruana.silverbirch.SilverBirch;
import org.caruana.silverbirch.SilverBirchException;
import org.caruana.silverbirch.SilverBirchException.SilverBirchConnectionException;
import org.caruana.silverbirch.datomic.SilverBirchImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;


public class SilverBirchImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(SilverBirchImplTest.class);

    private String repo = "mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("EngineTest");
        profiler.setLogger(logger);
    }
    
    
    @Test(expected = SilverBirchConnectionException.class)
    public void connectFailure()
    {
        profiler.start("connect");
        SilverBirch silverbirch = new SilverBirchImpl();
        try
        {
            silverbirch.connect(repo);
        }
        finally
        {
            profiler.stop().log();
        }
    }

    @Test
    public void createRepo()
    {
        SilverBirch silverbirch = new SilverBirchImpl();
        
        profiler.start("create");
        boolean created = silverbirch.createRepo(repo);
        assertTrue(created);
        profiler.start("connect");;
        silverbirch.connect(repo);
        profiler.start("createagain");
        created = silverbirch.createRepo(repo);
        assertFalse(created);
        profiler.stop().log();
    }
}
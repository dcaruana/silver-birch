package org.caruana.clockwork;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.caruana.clockwork.ClockworkException.ClockworkConnectionException;
import org.caruana.clockwork.datomic.ClockworkImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;


public class EngineTest {
    
    private static Logger logger = LoggerFactory.getLogger(EngineTest.class);

    private String repo = "mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("EngineTest");
        profiler.setLogger(logger);
    }
    
    
    @Test(expected = ClockworkConnectionException.class)
    public void connectFailure()
    {
        profiler.start("connect");
        Clockwork clockwork = new ClockworkImpl();
        try
        {
            clockwork.connect(repo);
        }
        finally
        {
            profiler.stop().log();
        }
    }

    @Test
    public void createRepo()
    {
        Clockwork clockwork = new ClockworkImpl();
        
        profiler.start("create");
        boolean created = clockwork.createRepo(repo);
        assertTrue(created);
        profiler.start("connect");;
        clockwork.connect(repo);
        profiler.start("createagain");
        created = clockwork.createRepo(repo);
        assertFalse(created);
        profiler.stop().log();
    }
}
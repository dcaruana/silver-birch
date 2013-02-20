package caruana.silverbirch.server;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.SilverBirch;
import caruana.silverbirch.SilverBirchException.SilverBirchConnectionException;
import caruana.silverbirch.server.SilverBirchModule;

import com.google.inject.Guice;
import com.google.inject.Injector;


public class SilverBirchImplTest {
    
    private static Logger logger = LoggerFactory.getLogger(SilverBirchImplTest.class);

    private String repo = "mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private SilverBirch silverbirch;
    
    @Before
    public void initProfiler()
    {
        profiler = new Profiler("SilverBirchImplTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        Injector injector = Guice.createInjector(new SilverBirchModule());
        silverbirch = injector.getInstance(SilverBirch.class);
    }
    
    
    @Test(expected = SilverBirchConnectionException.class)
    public void connectFailure()
    {
        profiler.start("connect");
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
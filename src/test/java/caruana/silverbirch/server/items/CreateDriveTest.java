package caruana.silverbirch.server.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.Item;
import caruana.silverbirch.SilverBirchException.SilverBirchValidatorException;
import caruana.silverbirch.server.items.CreateDrive;

import datomic.Peer;

public class CreateDriveTest {

    private static Logger logger = LoggerFactory.getLogger(ItemsImplTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("CreateDriveTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        Peer.createDatabase(repo);
        conn = Peer.connect(repo);
    }

    @Test
    public void invalidDriveName()
    {
        profiler.start("createInvalidDrive");
        try
        {
            new CreateDrive(conn, "/");
            fail("Failed to catch invalid drive name");
        }
        catch(SilverBirchValidatorException e)
        {
        }
        profiler.stop().log();
    }
    
    @Test
    public void createDrive()
    {
        profiler.start("createDrive");
        CreateDrive statement = new CreateDrive(conn, "test");
        Item drive = statement.getDrive();
        assertNotNull(drive);
        assertNotNull(drive.getUniqueId());
        assertNotNull(drive.getDriveId());
        assertNotNull(drive.getId());
        assertEquals(drive.getDriveId(), drive.getId());
        assertEquals("test", drive.getName());
        profiler.stop().log();
    }

}

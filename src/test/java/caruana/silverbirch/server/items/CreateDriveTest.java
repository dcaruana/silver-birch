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
import caruana.silverbirch.server.repo.InMemoryRepoStore;

public class CreateDriveTest {

    private static Logger logger = LoggerFactory.getLogger(CreateDriveTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(CreateDriveTest.class.getSimpleName());
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        InMemoryRepoStore repoStore = new InMemoryRepoStore();
        repoStore.create(repo);
        conn = repoStore.connect(repo);
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
        assertNotNull(statement.data());
        profiler.stop().log();
    }

}

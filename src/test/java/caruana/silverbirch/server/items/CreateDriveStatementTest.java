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
import caruana.silverbirch.server.items.CreateDriveStatementFactory.CreateDriveStatement;
import caruana.silverbirch.server.log.ChangeLogImpl;

public class CreateDriveStatementTest {

    private static Logger logger = LoggerFactory.getLogger(CreateDriveStatementTest.class);

    private Profiler profiler;
    private CreateDriveStatementFactory createDrive;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(CreateDriveStatementTest.class.getSimpleName());
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        createDrive = new CreateDriveStatementFactory();
        createDrive.setChangeLog(new ChangeLogImpl());
    }

    @Test
    public void invalidDriveName()
    {
        profiler.start("createInvalidDrive");
        try
        {
            createDrive.statement("/");
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
        CreateDriveStatement statement = createDrive.statement("test");
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

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
import caruana.silverbirch.server.items.CreateDriveStatement.CreateDrive;
import caruana.silverbirch.server.repo.InMemoryRepoStore;

public class CreateItemTest {

    private static Logger logger = LoggerFactory.getLogger(CreateItemTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private datomic.Connection conn;
    private CreateDriveStatement createDrive;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler(CreateItemTest.class.getSimpleName());
        profiler.setLogger(logger);
    }
    
    @Before
    public void init()
    {
        InMemoryRepoStore repoStore = new InMemoryRepoStore();
        repoStore.create(repo);
        conn = repoStore.connect(repo);
        createDrive = new CreateDriveStatement();
    }

    @Test
    public void invalidItemName()
    {
        profiler.start("createDrive");
        CreateDrive driveStatement = createDrive.statement("test");
        profiler.start("createInvalidItem");
        try
        {
            new CreateItem(conn, driveStatement.getDrive(), "/");
            fail("Failed to catch invalid item name");
        }
        catch(SilverBirchValidatorException e)
        {
        }
        profiler.stop().log();
    }
    
    @Test
    public void createItem()
    {
        profiler.start("createDrive");
        CreateDrive driveStatement = createDrive.statement("test");
        Item drive = driveStatement.getDrive();
        assertNotNull(drive);
        profiler.start("createItem");
        CreateItem itemStatement = new CreateItem(conn, drive, "item1");
        assertNotNull(itemStatement);
        Item item = itemStatement.getItem();
        assertNotNull(item);
        assertNotNull(item.getUniqueId());
        assertNotNull(item.getId());
        assertEquals(drive.getDriveId(), item.getDriveId());
        assertEquals(drive.getRootId(), item.getRootId());
        assertEquals("item1", item.getName());
        assertNotNull(itemStatement.data());
        profiler.stop().log();
    }
 
}

package caruana.silverbirch.server.schema;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.SilverBirchException.SilverBirchFunctionException;
import caruana.silverbirch.server.TransactionImpl;
import caruana.silverbirch.server.repo.InMemoryRepoStore;

public class DefineFunctionTest {

    private static Logger logger = LoggerFactory.getLogger(DefineFunctionTest.class);

    private String repo = "repo_" + System.currentTimeMillis();
    private Profiler profiler;
    private TransactionImpl transaction;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("DefineFunctionTest");
        profiler.setLogger(logger);
    }
    
    @Before
    public void initTransaction()
    {
        InMemoryRepoStore repoStore = new InMemoryRepoStore();
        repoStore.create(repo);
        datomic.Connection conn = repoStore.connect(repo);
        TestData bootstrap = new TestData();
        bootstrap.bootstrap(conn);
        transaction = new TransactionImpl(conn);
    }
    
    @Test
    public void defineFunction()
    {
        profiler.start("createFunction");
        DefineFunction fn = new DefineFunction("test", new String[] {"db", "name"}, "/statements/test_fn.edn");
        transaction.addStatement(fn);
        profiler.start("applyChanges");
        transaction.applyChanges();
        profiler.start("createInvoke");
        EDN invoke = new EDN("/statements/test_fn_invoke.edn");
        transaction.addStatement(invoke);
        profiler.start("applyChanges");
        transaction.applyChanges();
        transaction.addStatement(invoke);
        profiler.start("applyChanges");
        transaction.applyChanges();
        profiler.stop().log();
    }

    @Test
    public void compileError()
    {
        try
        {
            profiler.start("createFunction");
            DefineFunction fn = new DefineFunction("test", new String[] {"db", "name"}, "/statements/test_invalid_fn.edn");
            transaction.addStatement(fn);
            profiler.start("applyChanges");
            transaction.applyChanges();
            fail("Failed to throw SilverBirchFunctionException");
        }
        catch(SilverBirchFunctionException e)
        {
        }
        finally
        {
            profiler.stop().log();
        }
    }

}

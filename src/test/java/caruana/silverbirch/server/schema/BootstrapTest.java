package caruana.silverbirch.server.schema;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

import caruana.silverbirch.server.Bootstrap;

import datomic.Peer;

public class BootstrapTest {

    private static Logger logger = LoggerFactory.getLogger(BootstrapTest.class);

    private String repo = "datomic:mem://repo_" + System.currentTimeMillis();
    private Profiler profiler;

    @Before
    public void initProfiler()
    {
        profiler = new Profiler("BootstrapTest");
        profiler.setLogger(logger);
    }
    
    @Test
    public void bootstrap()
    {
        profiler.start("createDatabase");
        Peer.createDatabase(repo);
        datomic.Connection conn = Peer.connect(repo);
        profiler.start("bootstrap");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.bootstrap(conn);
        profiler.stop().log();
    }

}

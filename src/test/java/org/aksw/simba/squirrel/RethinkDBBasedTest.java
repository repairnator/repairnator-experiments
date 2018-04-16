package org.aksw.simba.squirrel;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.exc.ReqlDriverError;
import com.rethinkdb.net.Connection;
import org.aksw.simba.squirrel.queue.RDBQueue;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RethinkDBBasedTest {
    protected static Connection connection;
    protected static RethinkDB r = RethinkDB.r;

    @BeforeClass
    public static void setUpRDB() throws Exception {
        String rethinkDockerExecCmd = "docker run --name squirrel-test-rethinkdb "
            + "-p 58015:28015 -p 58887:8080 -d rethinkdb:2.3.5";
        Process p = Runtime.getRuntime().exec(rethinkDockerExecCmd);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
        // read any errors from the attempted command
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        r = RethinkDB.r;
        int retryCount = 0;
        while (true) {
            try {
                connection = r.connection().hostname("localhost").port(58015).connect();
                break;
            } catch (ReqlDriverError error) {
                System.out.println("Could not connect, retrying");
                retryCount++;
                if (retryCount > 10)
                    break;
                Thread.sleep(5000);
            }
        }
    }

    @AfterClass
    public static void tearDownRDB() throws Exception {
        String rethinkDockerStopCommand = "docker stop squirrel-test-rethinkdb";
        Process p = Runtime.getRuntime().exec(rethinkDockerStopCommand);
        p.waitFor();
        String rethinkDockerRmCommand = "docker rm squirrel-test-rethinkdb";
        p = Runtime.getRuntime().exec(rethinkDockerRmCommand);
        p.waitFor();
    }
}

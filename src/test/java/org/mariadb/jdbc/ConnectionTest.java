package org.mariadb.jdbc;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import static org.junit.Assert.*;

public class ConnectionTest extends BaseTest {

    /**
     * Initialisation.
     * @throws SQLException exception
     */
    @BeforeClass()
    public static void initClass() throws SQLException {
        createTable("dummy", "a BLOB");
    }

    /**
     * Conj-166.
     * Connection error code must be thrown
     *
     * @throws SQLException exception
     */
    @Test
    public void testAccessDeniedErrorCode() throws SQLException {
        try {
            DriverManager.getConnection("jdbc:mariadb://" + ((hostname != null) ? hostname : "localhost")
                    + ":" + port + "/" + database + "?user=foo");
            Assert.fail();
        } catch (SQLException e) {
            switch (e.getErrorCode()) {
                case (1524) :
                    //GSSAPI plugin not loaded
                    Assert.assertTrue("HY000".equals(e.getSQLState()));
                    break;

                case (1045) :
                    Assert.assertTrue("28000".equals(e.getSQLState()));
                    break;

                case (1044) :
                    //mysql
                    Assert.assertTrue("42000".equals(e.getSQLState()));
                    break;

                default:
                    fail("Wrong error code : " + e.getErrorCode());
            }
        }
    }

    /**
     * Conj-75 (corrected with CONJ-156)
     * Needs permission java.sql.SQLPermission "abort" or will be skipped.
     *
     * @throws SQLException exception
     */
    @Test
    public void abortTest() throws SQLException {
        Connection connection = null;
        try {
            connection = setConnection();
            Statement stmt = connection.createStatement();
            SQLPermission sqlPermission = new SQLPermission("callAbort");

            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null && sqlPermission != null) {
                try {
                    securityManager.checkPermission(sqlPermission);
                } catch (SecurityException se) {
                    System.out.println("test 'abortTest' skipped  due to missing policy");
                    return;
                }
            }
            Executor executor = new Executor() {
                @Override
                public void execute(Runnable command) {
                    command.run();
                }
            };
            connection.abort(executor);
            assertTrue(connection.isClosed());
            try {
                stmt.executeQuery("SELECT 1");
                assertTrue(false);
            } catch (SQLException sqle) {
                assertTrue(true);
            } finally {
                stmt.close();
            }
        } finally {
            connection.close();
        }
    }

    /**
     * Conj-121: implemented Connection.getNetworkTimeout and Connection.setNetworkTimeout.
     *
     * @throws SQLException exception
     */
    @Test
    public void networkTimeoutTest() throws SQLException {
        Connection connection = null;
        try {
            connection = setConnection();
            int timeout = 1000;
            SQLPermission sqlPermission = new SQLPermission("setNetworkTimeout");
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null && sqlPermission != null) {
                try {
                    securityManager.checkPermission(sqlPermission);
                } catch (SecurityException se) {
                    System.out.println("test 'setNetworkTimeout' skipped  due to missing policy");
                    return;
                }
            }
            Executor executor = new Executor() {
                @Override
                public void execute(Runnable command) {
                    command.run();
                }
            };
            try {
                connection.setNetworkTimeout(executor, timeout);
            } catch (SQLException sqlex) {
                assertTrue(false);
            }
            try {
                int networkTimeout = connection.getNetworkTimeout();
                assertEquals(timeout, networkTimeout);
            } catch (SQLException sqlex) {
                assertTrue(false);
            }
            try {
                connection.createStatement().execute("select sleep(2)");
                fail("Network timeout is " + timeout / 1000 + "sec, but slept for 2sec");
            } catch (SQLException sqlex) {
                assertTrue(connection.isClosed());
            }
        } finally {
            connection.close();
        }
    }

    /**
     * Conj-120 Fix Connection.isValid method.
     *
     * @throws SQLException exception
     */
    @Test
    public void isValidShouldThrowExceptionWithNegativeTimeout() throws SQLException {
        try {
            sharedConnection.isValid(-1);
            fail("The above row should have thrown an SQLException");
        } catch (SQLException sqlex) {
            assertTrue(sqlex.getMessage().contains("negative"));
        }
    }

    /**
     * Conj-116: Make SQLException prettier when too large stream is sent to the server.
     *
     * @throws SQLException exception
     * @throws UnsupportedEncodingException exception
     */
    @Test
    public void checkMaxAllowedPacket() throws Throwable {
        Statement statement = sharedConnection.createStatement();
        ResultSet rs = statement.executeQuery("show variables like 'max_allowed_packet'");
        rs.next();
        int maxAllowedPacket = rs.getInt(2);

        //Create a SQL stream bigger than maxAllowedPacket
        StringBuilder sb = new StringBuilder();
        String rowData = "('this is a dummy row values')";
        int rowsToWrite = (maxAllowedPacket / rowData.getBytes("UTF-8").length) + 1;
        try {
            for (int row = 1; row <= rowsToWrite; row++) {
                if (row >= 2) {
                    sb.append(", ");
                }
                sb.append(rowData);
            }
            statement.executeUpdate("INSERT INTO dummy VALUES " + sb.toString());
            fail("The previous statement should throw an SQLException");
        } catch (OutOfMemoryError e) {
            System.out.println("skip test 'maxAllowedPackedExceptionIsPrettyTest' - not enough memory");
            Assume.assumeNoException(e);
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("max_allowed_packet"));
        } catch (Exception e) {
            fail("The previous statement should throw an SQLException not a general Exception");
        }

        statement.execute("select count(*) from dummy"); //check that the connection is still working

        //added in CONJ-151 to check the 2 different type of query implementation
        PreparedStatement preparedStatement = sharedConnection.prepareStatement("INSERT INTO dummy VALUES (?)");
        try {
            byte[] arr = new byte[maxAllowedPacket + 1000];
            Arrays.fill(arr, (byte) 'a');
            preparedStatement.setBytes(1, arr);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
            fail("The previous statement should throw an SQLException");
        } catch (OutOfMemoryError e) {
            System.out.println("skip second test 'maxAllowedPackedExceptionIsPrettyTest' - not enough memory");
            Assume.assumeNoException(e);
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("max_allowed_packet"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("The previous statement should throw an SQLException not a general Exception");
        } finally {
            statement.execute("select count(*) from dummy"); //to check that connection is open
        }
    }


    @Test
    public void isValidTestWorkingConnection() throws SQLException {
        assertTrue(sharedConnection.isValid(0));
    }

    /**
     * CONJ-120 Fix Connection.isValid method
     *
     * @throws SQLException exception
     */
    @Test
    public void isValidClosedConnection() throws SQLException {
        Connection connection = null;
        try {
            connection = setConnection();
            connection.close();
            boolean isValid = connection.isValid(0);
            assertFalse(isValid);
        } finally {
            connection.close();
        }
    }

    /**
     * CONJ-120 Fix Connection.isValid method
     *
     * @throws SQLException exception
     * @throws InterruptedException exception
     */
    @Test
    public void isValidConnectionThatTimesOutByServer() throws SQLException, InterruptedException {
        Connection connection = null;
        try {
            connection = setConnection();
            Statement statement = connection.createStatement();
            statement.execute("set session wait_timeout=1");
            Thread.sleep(3000); // Wait for the server to kill the connection
            boolean isValid = connection.isValid(0);
            assertFalse(isValid);
            statement.close();
        } finally {
            connection.close();
        }
    }

    /**
     * CONJ-363 : follow JDBC connection client infos rules.
     * @throws Exception if any occur.
     */
    @Test
    public void testConnectionClientInfos() throws Exception {
        assertEquals(null, sharedConnection.getClientInfo("ApplicationName"));
        assertEquals(null, sharedConnection.getClientInfo("ClientUser"));
        assertEquals(null, sharedConnection.getClientInfo("ClientHostname"));

        try {
            sharedConnection.getClientInfo("otherName");
            fail("Must have throw exception since name wasn't correct");
        } catch (SQLException sqlEx) {
            assertTrue(sqlEx.getMessage().contains("name must be"));
        }

        Properties properties = sharedConnection.getClientInfo();
        assertEquals(null, properties.get("ApplicationName"));
        assertEquals(null, properties.get("ClientUser"));
        assertEquals(null, properties.get("ClientHostname"));

        sharedConnection.setClientInfo("ClientHostname", "testHostName");
        assertEquals("testHostName", sharedConnection.getClientInfo("ClientHostname"));

        properties = new Properties();
        properties.setProperty("ApplicationName", "testDriver");
        properties.setProperty("ClientUser", "testClientUser");
        properties.setProperty("NotPermitted", "blabla");
        properties.setProperty("NotPermitted2", "blabla");

        try {
            sharedConnection.setClientInfo(properties);
        } catch (SQLClientInfoException sqle) {
            assertEquals("setClientInfo errors : the following properties where not set :{NotPermitted,NotPermitted2}", sqle.getMessage());
            Map<String, ClientInfoStatus> failedProperties = sqle.getFailedProperties();
            assertTrue(failedProperties.containsKey("NotPermitted"));
            assertTrue(failedProperties.containsKey("NotPermitted2"));
            assertEquals(2, failedProperties.size());
        }

        assertEquals("testDriver", sharedConnection.getClientInfo("ApplicationName"));
        assertEquals("testClientUser", sharedConnection.getClientInfo("ClientUser"));
        assertEquals(null, sharedConnection.getClientInfo("ClientHostname"));

        sharedConnection.setClientInfo("ClientUser", "otherValue");

        assertEquals("testDriver", sharedConnection.getClientInfo("ApplicationName"));
        assertEquals("otherValue", sharedConnection.getClientInfo("ClientUser"));
        assertEquals(null, sharedConnection.getClientInfo("ClientHostname"));

        try {
            sharedConnection.setClientInfo("NotPermitted", "otherValue");
            fail("Must have send an exception");
        } catch (SQLClientInfoException sqle) {
            assertEquals("setClientInfo() parameters can only be \"ApplicationName\",\"ClientUser\" or \"ClientHostname\", "
                    + "but was : NotPermitted", sqle.getMessage());
            Map<String, ClientInfoStatus> failedProperties = sqle.getFailedProperties();
            assertTrue(failedProperties.containsKey("NotPermitted"));
            assertEquals(1, failedProperties.size());
        }
    }
}

package org.mariadb.jdbc;

import com.sun.jna.Platform;
import org.junit.*;

import java.io.*;
import java.lang.reflect.Field;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.sql.*;
import java.util.Collection;
import java.util.Properties;
import java.util.UUID;

import static org.junit.Assert.*;

public class SslTest extends BaseTest {
    String serverCertificatePath;
    String clientKeystorePath;
    String clientKeystorePassword;

    /**
     * Enable Crypto.
     */
    @BeforeClass
    public static void enableCrypto() {
        Assume.assumeFalse("MAXSCALE".equals(System.getenv("TYPE")));

        try {
            Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, Boolean.FALSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Check requirement.
     *
     * @throws SQLException exception exception
     */
    @Before
    public void checkSsl() throws SQLException {
        boolean isJava7 = System.getProperty("java.version").contains("1.7.");
        Assume.assumeTrue(haveSsl(sharedConnection));
        //Skip SSL test on java 7 since SSL stream size JDK-6521495).
        Assume.assumeFalse(isJava7);

        if (System.getProperty("serverCertificatePath") == null) {
            ResultSet rs = sharedConnection.createStatement().executeQuery("select @@ssl_cert");
            rs.next();
            serverCertificatePath = rs.getString(1);
            rs.close();
        } else {
            serverCertificatePath = System.getProperty("serverCertificatePath");
        }
        clientKeystorePath = System.getProperty("keystorePath");
        clientKeystorePassword = System.getProperty("keystorePassword");
        Statement stmt = sharedConnection.createStatement();
        try {
            stmt.execute("DROP USER 'ssltestUser'@'%'");
        } catch (SQLException e) {
        }
        stmt.execute("CREATE USER 'ssltestUser'@'%'");
        stmt.execute("GRANT ALL PRIVILEGES ON *.* TO 'ssltestUser'@'%' REQUIRE SSL");
    }

    @Test
    public void useSsl() throws Exception {
        Assume.assumeTrue(haveSsl(sharedConnection));
        //Skip SSL test on java 7 since SSL stream size JDK-6521495).
        Assume.assumeFalse(System.getProperty("java.version").contains("1.7."));
        Connection connection = setConnection("&useSSL=true&trustServerCertificate=true");
        try {
            connection.createStatement().execute("select 1");
        } finally {
            connection.close();
        }
    }

    protected void useSslForceTls(String tls) throws Exception {
        useSslForceTls(tls, null);
    }

    /**
     * Helper method when checking/enabling secure connections for a specific TLS protocol suite.
     **/
    protected void useSslForceTls(String tls, String ciphers) throws Exception {
        Assume.assumeTrue(haveSsl(sharedConnection));
        //Skip SSL test on java 7 since SSL stream size JDK-6521495).
        Assume.assumeFalse(System.getProperty("java.version").contains("1.7."));
        Properties info = new Properties();
        info.setProperty("useSSL", "true");
        info.setProperty("trustServerCertificate", "true");
        info.setProperty("enabledSslProtocolSuites", tls);
        if (ciphers != null) info.setProperty("enabledSslCipherSuites", ciphers);

        Connection connection = setConnection(info);
        try {
            connection.createStatement().execute("select 1");
        } finally {
            connection.close();
        }
    }

    @Test
    public void testBadOptionEnabledSslProtocolSuites() throws Exception {
        try {
            useSslForceTls("TLSv1,TLSv1.5");
            fail("Must have thrown error since protocol unknown");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("Unsupported SSL protocol 'TLSv1.5'. Supported protocols : "));
        }
    }

    @Test
    public void testUnknownProtocol() throws Exception {
        try {
            useSslForceTls("TLSv0");
            fail("Must have thrown error since protocol not set");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("Unsupported SSL protocol 'TLSv0'. Supported protocols : "));
        }
    }

    @Test
    public void testServerRefuseProtocol() throws Exception {
        try {
            useSslForceTls("SSLv3");
            fail("Must have thrown error since protocol is refused by server");
        } catch (SQLNonTransientConnectionException e) {
            assertTrue(e.getMessage().contains("No appropriate protocol "
                    + "(protocol is disabled or cipher suites are inappropriate)"));
        }
    }

    @Test
    public void useSslForceTlsV1() throws Exception {
        useSslForceTls("TLSv1");
    }

    @Test
    public void useSslForceTlsV11() throws Exception {
        // must either be mariadb or mysql version 5.7.10
        if (isMariadbServer() || minVersion(5, 7)) useSslForceTls("TLSv1.1");
    }

    @Test
    public void useSslForceTlsV12() throws Exception {
        Assume.assumeFalse(Platform.isWindows());
        // Only test with MariaDB since MySQL community is compiled with yaSSL
        if (isMariadbServer()) useSslForceTls("TLSv1.2");
    }

    @Test
    public void useSslForceTlsV12AndCipher() throws Exception {
        Assume.assumeFalse(Platform.isWindows());
        // Only test with MariaDB since MySQL community is compiled with yaSSL
        if (isMariadbServer()) {
            useSslForceTls("TLSv1.2", "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384");
        }
    }

    @Test
    public void wrongCipher() throws Exception {
        // Only test with MariaDB since MySQL community is compiled with yaSSL
        try {
            if (isMariadbServer()) {
                useSslForceTls("TLSv1.2", "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256, UNKNOWN_CIPHER");
                fail("Must have thrown error since cipher is refused by server");
            }
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("Unsupported SSL cipher 'UNKNOWN_CIPHER'."));
        }
    }


    @Test
    public void wrongCipherForTls11() throws Exception {
        // Only test with MariaDB since MySQL community is compiled with yaSSL
        try {
            if (isMariadbServer()) {
                useSslForceTls("TLSv1.1", "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256");
                fail("Must have thrown error since cipher aren't TLSv1.1 ciphers");
            }
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("No appropriate protocol (protocol is disabled or cipher suites are inappropriate)"));
        }
    }

    @Test
    public void wrongCipherMysqlOptionCompatibility() throws Exception {
        // Only test with MariaDB since MySQL community is compiled with yaSSL
        try {
            if (isMariadbServer()) {
                Assume.assumeTrue(haveSsl(sharedConnection));
                //Skip SSL test on java 7 since SSL stream size JDK-6521495).
                Assume.assumeFalse(System.getProperty("java.version").contains("1.7."));
                Properties info = new Properties();
                info.setProperty("useSSL", "true");
                info.setProperty("trustServerCertificate", "true");
                info.setProperty("enabledSslProtocolSuites", "TLSv1.1");
                //enabledSSLCipherSuites, not enabledSslCipherSuites (different case)
                info.setProperty("enabledSSLCipherSuites", "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256");

                Connection connection = setConnection(info);
                try {
                    connection.createStatement().execute("select 1");
                    fail("Must have thrown error since cipher aren't TLSv1.1 ciphers");
                } finally {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("No appropriate protocol (protocol is disabled or cipher suites are inappropriate)"));
        }
    }


    @Test
    public void useSslForceTlsCombination() throws Exception {
        if (isMariadbServer() && !Platform.isWindows()) {
            useSslForceTls("TLSv1,TLSv1.1,TLSv1.2");
        } else {
            useSslForceTls("TLSv1,TLSv1");
        }
    }

    @Test
    public void useSslForceTlsCombinationWithSpace() throws Exception {
        if (isMariadbServer() && !Platform.isWindows()) {
            useSslForceTls("TLSv1, TLSv1.1, TLSv1.2");
        } else {
            useSslForceTls("TLSv1, TLSv1");
        }
    }


    @Test
    public void useSslForceTlsCombinationWithOnlySpace() throws Exception {
        if (isMariadbServer() && !Platform.isWindows()) {
            useSslForceTls("TLSv1 TLSv1.1 TLSv1.2");
        } else {
            useSslForceTls("TLSv1 TLSv1");
        }
    }

    private String getServerCertificate() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(serverCertificatePath)));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    //eat exception
                }
            }
        }
    }

    private void saveToFile(String path, String contents) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(path));
            out.print(contents);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    //eat exception
                }
            }
        }
    }

    private Connection createConnection(Properties info) throws SQLException {
        return createConnection(info, username, password);
    }

    private Connection createConnection(Properties info, String user, String pwd) throws SQLException {
        String jdbcUrl = connUri;
        Properties connProps = new Properties(info);
        connProps.setProperty("user", user);
        if (pwd != null) {
            connProps.setProperty("password", pwd);
        }
        return openNewConnection(jdbcUrl, connProps);
    }

    /**
     * Test connection.
     *
     * @param info        connection properties
     * @param sslExpected is SSL expected
     * @throws SQLException exception
     */
    public void testConnect(Properties info, boolean sslExpected) throws SQLException {
        testConnect(info, sslExpected, "ssltestUser", "");
    }

    /**
     * Test connection.
     *
     * @param info        connection properties
     * @param sslExpected is SSL expected
     * @param user        user
     * @param pwd         password
     * @throws SQLException if exception occur
     */
    public void testConnect(Properties info, boolean sslExpected, String user, String pwd) throws SQLException {
        Connection conn = null;
        Statement stmt = null;

        conn = createConnection(info, user, pwd);
        // First do a basic select test:
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT 1");
        rs.next();
        Assert.assertTrue(rs.getInt(1) == 1);
        rs.close();

        // Then check if SSL matches what is expected
        rs = stmt.executeQuery("SHOW STATUS LIKE 'Ssl_cipher'");
        rs.next();
        String sslCipher = rs.getString(2);
        boolean sslActual = sslCipher != null && sslCipher.length() > 0;
        Assert.assertEquals("sslExpected does not match", sslExpected, sslActual);

    }

    @Test
    public void testConnectNonSsl() throws SQLException {
        Properties info = new Properties();
        try {
            testConnect(info, false);
            fail("Must fail since user require SSL");
        } catch (SQLException e) {
            assertTrue(e.getMessage().contains("Access denied for user 'ssltestUser'"));
        }
    }

    @Test
    public void testTrustedServer() throws SQLException {
        Properties info = new Properties();
        info.setProperty("useSSL", "true");
        info.setProperty("trustServerCertificate", "true");
        testConnect(info, true);
    }

    @Test
    public void testServerCertString() throws SQLException {
        Properties info = new Properties();
        info.setProperty("useSSL", "true");
        info.setProperty("serverSslCert", getServerCertificate());
        testConnect(info, true);
    }

    @Test(expected = SQLException.class)
    public void testBadServerCertString() throws SQLException {
        Properties info = new Properties();
        info.setProperty("useSSL", "true");
        info.setProperty("serverSslCert", "foobar");
        testConnect(info, true);
    }

    @Test
    public void testServerCertFile() throws SQLException {
        Properties info = new Properties();
        info.setProperty("useSSL", "true");
        info.setProperty("serverSslCert", serverCertificatePath);
        testConnect(info, true);
    }

    @Test
    public void testServerCertClasspathFile() throws SQLException {
        Assume.assumeTrue(new File("target/classes").isDirectory());
        Properties info = new Properties();
        info.setProperty("useSSL", "true");
        // Copy the valid server certificate to a known location on the classpath:
        String classpathFilename = "server-ssl-cert.pem";
        saveToFile("target/classes/" + classpathFilename, getServerCertificate());
        info.setProperty("serverSslCert", "classpath:" + classpathFilename);
        testConnect(info, true);
    }

    @Test(expected = SQLException.class)
    public void testWrongServerCert() throws Throwable {
        Properties info = new Properties();
        info.setProperty("useSSL", "true");
        info.setProperty("serverSslCert", "classpath:ssl/wrong-server.crt");
        testConnect(info, true);
    }

    @Test
    public void conc71() throws SQLException {
        try {
            Properties info = new Properties();
            info.setProperty("serverSslCert", getServerCertificate());
            info.setProperty("useSSL", "true");
            Connection conn = createConnection(info);
            assertEquals("testj", conn.getCatalog());
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTruststore() throws SQLException, IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        // generate a truststore from the canned serverCertificate
        File tempKeystore = File.createTempFile("keystore", ".tmp");
        String keystorePath = tempKeystore.getAbsolutePath();
        try {
            generateKeystoreFromFile(serverCertificatePath, keystorePath, null);

            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("trustStore", "file:///" + keystorePath);
            testConnect(info, true);
        } finally {
            tempKeystore.delete();
        }
    }

    @Test
    public void testTrustStoreWithPassword() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, SQLException {
        // generate a truststore from the canned serverCertificate
        File tempKeystore = File.createTempFile("keystore", ".tmp");
        String keystorePath = tempKeystore.getAbsolutePath();
        try {
            generateKeystoreFromFile(serverCertificatePath, keystorePath, "mysecret");

            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("trustStore", "file:///" + keystorePath);
            info.setProperty("trustStorePassword", "mysecret");
            testConnect(info, true);
        } finally {
            tempKeystore.delete();
        }
    }

    @Test
    public void testTrustStoreWithPasswordProperties() throws Exception {
        // generate a truststore from the canned serverCertificate
        File tempKeystore = File.createTempFile("keystore", ".tmp");
        String keystorePath = tempKeystore.getAbsolutePath();

        String initialTrustStore = System.getProperty("javax.net.ssl.trustStore");
        String initialTrustStorePwd = System.getProperty("javax.net.ssl.trustStorePassword");


        try {
            generateKeystoreFromFile(serverCertificatePath, keystorePath, "mysecret");

            System.setProperty("javax.net.ssl.trustStore", keystorePath);
            System.setProperty("javax.net.ssl.trustStorePassword", "mysecret");

            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            testConnect(info, true);
        } finally {
            if (initialTrustStore == null) {
                System.clearProperty("javax.net.ssl.trustStore");
            } else {
                System.setProperty("javax.net.ssl.trustStore", initialTrustStore);
            }
            if (initialTrustStorePwd == null) {
                System.clearProperty("javax.net.ssl.trustStorePassword");
            } else {
                System.setProperty("javax.net.ssl.trustStorePassword", initialTrustStorePwd);
            }
            tempKeystore.delete();
        }
    }

    @Test(expected = SQLException.class)
    public void testTruststoreWithWrongPassword() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException,
            SQLException {
        // generate a truststore from the canned serverCertificate
        File tempKeystore = File.createTempFile("keystore", ".tmp");
        String keystorePath = tempKeystore.getAbsolutePath();
        try {
            generateKeystoreFromFile(serverCertificatePath, keystorePath, "mysecret");

            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("trustStore", "file:///" + keystorePath);
            info.setProperty("trustStorePassword", "notthepassword");
            testConnect(info, true);
        } finally {
            tempKeystore.delete();
        }
    }

    @Test(expected = SQLException.class)
    public void testTruststoreWithWrongCert() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, SQLException {
        // generate a truststore from the canned serverCertificate
        File tempKeystore = File.createTempFile("keystore", ".tmp");
        String keystorePath = tempKeystore.getAbsolutePath();
        try {
            generateKeystoreFromFile("classpath:ssl/wrong-server.crt", keystorePath, "mysecret");

            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("trustStore", "file:///" + keystorePath);
            info.setProperty("trustStorePassword", "mysecret");
            testConnect(info, true);
        } finally {
            tempKeystore.delete();
        }
    }

    @Test
    public void testTruststoreAndClientKeystore() throws SQLException, IOException, KeyStoreException, CertificateException,
            NoSuchAlgorithmException {
        // This test only runs if a client keystore and password have been passed in as properties (-DkeystorePath and -DkeystorePassword)
        // You can create a keystore as follows:
        // echo "kspass" | openssl pkcs12 -export -in "${clientCertFile}" -inkey "${clientKeyFile}" -out "${clientKeystoreFile}"
        //   -name "mysqlAlias" -pass stdin
        Assume.assumeTrue(clientKeystorePathDefined());

        String testUser = "testTsAndKs";
        // For this testcase, the testUser must be configured with ssl_type=X509
        createSslTestUser(testUser);

        // generate a truststore from the canned server certificate
        File tempTruststore = File.createTempFile("truststore", ".tmp");
        String truststorePath = tempTruststore.getAbsolutePath();
        try {
            generateKeystoreFromFile(serverCertificatePath, truststorePath, null);

            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("trustStore", "file:///" + truststorePath);
            info.setProperty("keyStore", "file:///" + clientKeystorePath);
            info.setProperty("keyStorePassword", clientKeystorePassword);
            testConnect(info, true, testUser, "ssltestpassword");
        } finally {
            tempTruststore.delete();
            deleteSslTestUser(testUser);
        }
    }


    @Test
    public void testAliases() throws SQLException, IOException, KeyStoreException, CertificateException,
            NoSuchAlgorithmException {
        // This test only runs if a client keystore and password have been passed in as properties (-DkeystorePath and -DkeystorePassword)
        // You can create a keystore as follows:
        // echo "kspass" | openssl pkcs12 -export -in "${clientCertFile}" -inkey "${clientKeyFile}" -out "${clientKeystoreFile}"
        //   -name "mysqlAlias" -pass stdin
        Assume.assumeTrue(clientKeystorePathDefined());

        String testUser = "testTsAndKs";
        // For this testcase, the testUser must be configured with ssl_type=X509
        createSslTestUser(testUser);

        // generate a truststore from the canned server certificate
        File tempTruststore = File.createTempFile("truststore", ".tmp");
        String truststorePath = tempTruststore.getAbsolutePath();
        try {
            generateKeystoreFromFile(serverCertificatePath, truststorePath, "trustPwd");

            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("trustCertificateKeyStoreUrl", "file:///" + truststorePath);
            info.setProperty("trustCertificateKeyStorePassword", "trustPwd");
            info.setProperty("clientCertificateKeyStoreUrl", "file:///" + clientKeystorePath);
            info.setProperty("clientCertificateKeyStorePassword", clientKeystorePassword);
            testConnect(info, true, testUser, "ssltestpassword");
        } finally {
            tempTruststore.delete();
            deleteSslTestUser(testUser);
        }
    }


    @Test
    public void testClientKeystore() throws SQLException, IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        // This test only runs if a client keystore and password have been passed in as properties (-DkeystorePath and -DkeystorePassword)
        // You can create a keystore as follows:
        // echo "kspass" | openssl pkcs12 -export -in "${clientCertFile}" -inkey "${clientKeyFile}" -out "${clientKeystoreFile}"
        //   -name "mysqlAlias" -pass stdin
        Assume.assumeTrue(clientKeystorePathDefined());

        String testUser = "testKeystore";
        // For this testcase, the testUser must be configured with ssl_type=X509
        createSslTestUser(testUser);

        try {
            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("serverSslCert", serverCertificatePath);
            info.setProperty("keyStore", "file:///" + clientKeystorePath);
            info.setProperty("keyStorePassword", clientKeystorePassword);
            testConnect(info, true, testUser, "ssltestpassword");
        } finally {
            deleteSslTestUser(testUser);
        }
    }

    /**
     * Verification when private key password differ from keyStore password.
     *
     * @throws Exception if error occur
     */
    @Test
    public void testClientKeyStoreWithPrivateKeyPwd() throws Exception {
        String clientKeyStore2Path = System.getProperty("keystore2Path");
        String clientKeyStore2Password = System.getProperty("keystore2Password");
        String clientKeyPassword = System.getProperty("keyPassword");
        Assume.assumeTrue(clientKeyPassword != null);
        String testUser = "testKeystore";
        // For this testcase, the testUser must be configured with ssl_type=X509
        createSslTestUser(testUser);

        //without keyPassword
        try {
            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("serverSslCert", serverCertificatePath);
            info.setProperty("keyStore", "file:///" + clientKeyStore2Path);
            info.setProperty("keyStorePassword", clientKeyStore2Password);
            testConnect(info, true, testUser, "ssltestpassword");
            fail("Must have Error since client private key is protected with a password different than keystore");
        } catch (SQLException sqle) {
            assertTrue(sqle.getMessage().contains("Access denied for user"));
        }

        try {
            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("serverSslCert", serverCertificatePath);
            info.setProperty("keyStore", "file:///" + clientKeyStore2Path);
            info.setProperty("keyStorePassword", clientKeyStore2Password);
            info.setProperty("keyPassword", clientKeyPassword);
            testConnect(info, true, testUser, "ssltestpassword");
        } finally {
            deleteSslTestUser(testUser);
        }
    }

    @Test
    public void testKeyStoreWithProperties() throws Exception {
        Assume.assumeNotNull(clientKeystorePath);
        // generate a trustStore from the canned serverCertificate
        File tempKeystore = File.createTempFile("keystore", ".tmp");
        String keystorePath = tempKeystore.getAbsolutePath();

        String initialTrustStore = System.getProperty("javax.net.ssl.trustStore");
        String initialTrustStorePwd = System.getProperty("javax.net.ssl.trustStorePassword");
        String initialKeyStore = System.getProperty("javax.net.ssl.keyStore");
        String initialKeyStorePwd = System.getProperty("javax.net.ssl.keyStorePassword");

        String testUser = "testKeystore";
        // For this testcase, the testUser must be configured with ssl_type=X509
        createSslTestUser(testUser);

        try {
            generateKeystoreFromFile(serverCertificatePath, keystorePath, "mysecret");

            System.setProperty("javax.net.ssl.trustStore", keystorePath);
            System.setProperty("javax.net.ssl.trustStorePassword", "mysecret");
            System.setProperty("javax.net.ssl.keyStore", clientKeystorePath);
            System.setProperty("javax.net.ssl.keyStorePassword", clientKeystorePassword);

            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            testConnect(info, true, testUser, "ssltestpassword");
        } finally {
            if (initialTrustStore == null) {
                System.clearProperty("javax.net.ssl.trustStore");
            } else {
                System.setProperty("javax.net.ssl.trustStore", initialTrustStore);
            }
            if (initialTrustStorePwd == null) {
                System.clearProperty("javax.net.ssl.trustStorePassword");
            } else {
                System.setProperty("javax.net.ssl.trustStorePassword", initialTrustStorePwd);
            }
            if (initialKeyStore != null) {
                System.setProperty("javax.net.ssl.keyStore", initialKeyStore);
            } else {
                System.clearProperty("javax.net.ssl.keyStore");
            }
            if (initialKeyStorePwd != null) {
                System.setProperty("javax.net.ssl.keyStorePassword", initialKeyStorePwd);
            } else {
                System.clearProperty("javax.net.ssl.keyStorePassword");
            }
            tempKeystore.delete();
        }
    }

    @Test
    public void testKeyStoreWhenServerTrustedWithProperties() throws Exception {
        Assume.assumeNotNull(clientKeystorePath);
        // generate a trustStore from the canned serverCertificate
        File tempKeystore = File.createTempFile("keystore", ".tmp");
        String keystorePath = tempKeystore.getAbsolutePath();

        String initialTrustStore = System.getProperty("javax.net.ssl.trustStore");
        String initialTrustStorePwd = System.getProperty("javax.net.ssl.trustStorePassword");
        String initialKeyStore = System.getProperty("javax.net.ssl.keyStore");
        String initialKeyStorePwd = System.getProperty("javax.net.ssl.keyStorePassword");

        String testUser = "testKeystore";
        // For this testcase, the testUser must be configured with ssl_type=X509
        createSslTestUser(testUser);

        try {
            generateKeystoreFromFile(serverCertificatePath, keystorePath, "mysecret");

            System.clearProperty("javax.net.ssl.trustStore");
            System.clearProperty("javax.net.ssl.trustStorePassword");
            System.setProperty("javax.net.ssl.keyStore", clientKeystorePath);
            System.setProperty("javax.net.ssl.keyStorePassword", clientKeystorePassword);

            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("trustServerCertificate", "true");

            testConnect(info, true, testUser, "ssltestpassword");
        } finally {
            if (initialTrustStore != null) {
                System.setProperty("javax.net.ssl.trustStore", initialTrustStore);
            }
            if (initialTrustStorePwd != null) {
                System.setProperty("javax.net.ssl.trustStorePassword", initialTrustStorePwd);
            }
            if (initialKeyStore != null) {
                System.setProperty("javax.net.ssl.keyStore", initialKeyStore);
            } else {
                System.clearProperty("javax.net.ssl.keyStore");
            }
            if (initialKeyStorePwd != null) {
                System.setProperty("javax.net.ssl.keyStorePassword", initialKeyStorePwd);
            } else {
                System.clearProperty("javax.net.ssl.keyStorePassword");
            }
            tempKeystore.delete();
        }
    }


    @Test
    public void testClientKeyStoreProperties() throws SQLException, IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        // This test only runs if a client keystore and password have been passed in as properties (-DkeystorePath and -DkeystorePassword)
        // You can create a keystore as follows:
        // echo "kspass" | openssl pkcs12 -export -in "${clientCertFile}" -inkey "${clientKeyFile}" -out "${clientKeystoreFile}"
        //   -name "mysqlAlias" -pass stdin
        Assume.assumeTrue(clientKeystorePathDefined());

        File tempKeystore = File.createTempFile("keystore", ".tmp");
        String keystorePath = tempKeystore.getAbsolutePath();

        String testUser = "testKeystore";
        // For this testcase, the testUser must be configured with ssl_type=X509
        createSslTestUser(testUser);
        String initialTrustStore = System.getProperty("javax.net.ssl.trustStore");
        String initialTrustStorePwd = System.getProperty("javax.net.ssl.trustStorePassword");
        try {
            generateKeystoreFromFile(serverCertificatePath, keystorePath, "mysecret");

            System.setProperty("javax.net.ssl.trustStore", keystorePath);
            System.setProperty("javax.net.ssl.trustStorePassword", "mysecret");

            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("keyStore", "file:///" + clientKeystorePath);
            info.setProperty("keyStorePassword", clientKeystorePassword);

            testConnect(info, true, testUser, "ssltestpassword");
        } finally {
            if (initialTrustStore == null) {
                System.clearProperty("javax.net.ssl.trustStore");
            } else {
                System.setProperty("javax.net.ssl.trustStore", initialTrustStore);
            }
            if (initialTrustStorePwd == null) {
                System.clearProperty("javax.net.ssl.trustStorePassword");
            } else {
                System.setProperty("javax.net.ssl.trustStorePassword", initialTrustStorePwd);
            }

            deleteSslTestUser(testUser);
        }
    }

    @Test(expected = SQLException.class)
    public void testTruststoreAndClientKeystoreWrongPassword() throws SQLException, IOException, KeyStoreException, CertificateException,
            NoSuchAlgorithmException {
        // This test only runs if a client keystore and password have been passed in as properties (-DkeystorePath and -DkeystorePassword)
        // You can create a keystore as follows:
        // echo "kspass" | openssl pkcs12 -export -in "${clientCertFile}" -inkey "${clientKeyFile}"
        // -out "${clientKeystoreFile}" -name "mysqlAlias" -pass stdin
        Assume.assumeTrue(clientKeystorePathDefined());

        String testUser = "testWrongPwd";
        // For this testcase, the testUser must be configured with ssl_type=X509
        createSslTestUser(testUser);

        // generate a truststore from the canned server certificate
        File tempTruststore = File.createTempFile("truststore", ".tmp");
        String truststorePath = tempTruststore.getAbsolutePath();
        try {
            generateKeystoreFromFile(serverCertificatePath, truststorePath, null);

            Properties info = new Properties();
            info.setProperty("useSSL", "true");
            info.setProperty("trustStore", "file:///" + truststorePath);
            info.setProperty("keyStore", "file:///" + clientKeystorePath);
            info.setProperty("keyStorePassword", "notthekeystorepass");
            testConnect(info, true, testUser, "ssltestpassword");
        } finally {
            tempTruststore.delete();
            deleteSslTestUser(testUser);
        }
    }

    private boolean clientKeystorePathDefined() throws SQLException {
        return clientKeystorePath != null && !clientKeystorePath.isEmpty() && clientKeystorePassword != null && !clientKeystorePassword.isEmpty();
    }

    private void createSslTestUser(String user) throws SQLException {
        Statement st = sharedConnection.createStatement();
        st.execute("grant all privileges on *.* to '" + user + "'@'%' identified by 'ssltestpassword' REQUIRE X509");
    }

    private void deleteSslTestUser(String user) throws SQLException {
        Statement st = sharedConnection.createStatement();
        st.execute("drop user '" + user + "'@'%'");
    }

    private void generateKeystoreFromFile(String certificateFile, String keystoreFile, String password)
            throws KeyStoreException, CertificateException,
            IOException, NoSuchAlgorithmException {
        InputStream inStream = null;
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

        // generate a keystore from the provided cert
        if (certificateFile.startsWith("classpath:")) {
            // Load it from a classpath relative file
            String classpathFile = certificateFile.substring("classpath:".length());
            inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathFile);
        } else {
            inStream = new FileInputStream(certificateFile);
        }

        ks = KeyStore.getInstance(KeyStore.getDefaultType());
        try {
            // Note: KeyStore requires it be loaded even if you don't load anything into it:
            ks.load(null);
        } catch (Exception e) {

        }
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> caList = cf.generateCertificates(inStream);
        inStream.close();
        for (Certificate ca : caList) {
            ks.setCertificateEntry(UUID.randomUUID().toString(), ca);
        }

        try (ByteArrayOutputStream keyStoreOut = new ByteArrayOutputStream()) {

            ks.store(keyStoreOut, password == null ? new char[0] : password.toCharArray());

            // write the key to the file system
            try (FileOutputStream keyStoreStream = new FileOutputStream(keystoreFile)) {
                keyStoreStream.write(keyStoreOut.toByteArray());
            }
        }
    }

}

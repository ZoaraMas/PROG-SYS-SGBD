package JDBC;

import java.lang.System.Logger;
import java.sql.*;
import java.util.Properties;

public class MyDriver implements Driver {
    static {
        // Register the driver
        try {
            DriverManager.registerDriver(new MyDriver());
            System.out.println("Driver enregistré avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to register driver", e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        System.out.println("connecticut");
        if (!acceptsURL(url)) {
            return null;
        }
        try {
            return new MyConnection(url, info);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith("jdbc:mysgbd:");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0]; // Can add driver-specific properties if needed
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}

package per.th.fastds.jdbc;

import per.th.fastds.util.ExceptionUtils;
import per.th.fastds.util.ReflectUtils;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
public class DrivierDataSource implements DataSource {

    private final Driver driver;
    private final DriverProperties properties;

    public DrivierDataSource(String jdbcUrl) {
        this(null, jdbcUrl);
    }

    public DrivierDataSource(String driverClassName, String jdbcUrl) {
        this(new DriverProperties()
                .setDriverClassName(driverClassName)
                .setJdbcUrl(jdbcUrl));
    }

    public DrivierDataSource(Properties driverProperties) {
        DriverProperties properties = new DriverProperties(driverProperties);
        String driverClassName = properties.getDriverClassName();
        String jdbcUrl = properties.getJdbcUrl();
        this.driver = getDriver(driverClassName, jdbcUrl);
        this.properties = properties;
    }

    private Driver getDriver(String driverClassName, String jdbcUrl) {
        Driver res;

        try {
            if (driverClassName != null) {
                res = findDriver(driverClassName);
                if (!res.acceptsURL(jdbcUrl)) {
                    throw new IllegalArgumentException(String.format(
                            "The driver '%s' can not accept: %s", driverClassName, jdbcUrl));
                }
            } else {
                res = DriverManager.getDriver(jdbcUrl);
            }
        } catch (Exception e) {
            throw ExceptionUtils.rethrow(e);
        }

        return res;
    }

    private Driver findDriver(String driverClassName) {
        Enumeration<Driver> enumeration = DriverManager.getDrivers();

        while (enumeration.hasMoreElements()) {
            Driver driver = enumeration.nextElement();
            if (isTargetDriver(driver, driverClassName)) {
                return driver;
            }
        }

        return loadDriver(driverClassName);
    }

    private boolean isTargetDriver(Driver driver, String driverClassName) {
        return Objects.equals(driver.getClass().getName(), driverClassName);
    }

    private Driver loadDriver(String driverClassName) {
        Class<Driver> clazz = ReflectUtils.forName(driverClassName);
        return ReflectUtils.newInstance(clazz);
    }

    private boolean isCompatible(Driver driver, String jdbcUrl) {
        try {
            return driver != null && driver.acceptsURL(jdbcUrl);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return driver.connect(properties.getJdbcUrl(), properties);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        DriverProperties clone = new DriverProperties(properties);
        clone.setUserName(username);
        clone.setPassword(password);
        String url = clone.getJdbcUrl();
        return driver.connect(url, clone);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return isWrapperFor(iface) ? (T) driver : null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface != null && iface.isInstance(driver);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return driver.getParentLogger();
    }

}

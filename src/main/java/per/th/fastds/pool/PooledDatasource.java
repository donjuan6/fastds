package per.th.fastds.pool;

import per.th.fastds.jdbc.DriverProperties;
import per.th.fastds.jdbc.DrivierDataSource;
import per.th.fastds.util.Looper;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author th
 * @date 2021/10/7
 * @see
 * @since
 */
public class PooledDatasource extends ConnectionPool implements DataSource {

    private DataSource delegated;

    @Override
    public void init() throws Exception {
        DriverProperties properties = new DriverProperties()
                .setDriverClassName(driverClassName)
                .setJdbcUrl(jdbcUrl)
                .setUserName(username)
                .setPassword(password);
        this.delegated = new DrivierDataSource(properties);
        super.init();
    }

    @Override
    protected Connection createConnection() throws SQLException {
        return delegated.getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return delegated.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        delegated.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delegated.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return delegated.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return delegated.getParentLogger();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getPooledConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getPooledConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return isWrapperFor(iface) ? (T) delegated : null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface != null && iface.isInstance(delegated);
    }

    public static void main(String[] args) throws Exception {
        PooledDatasource datasource = new PooledDatasource();
        datasource.init();
        long before = System.nanoTime();
        Looper.loop(200, 10, () -> {
            try {
                Connection connection = datasource.getConnection();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).await();
        long now = System.nanoTime();
        System.out.println(TimeUnit.NANOSECONDS.toMillis(now - before));
    }

}

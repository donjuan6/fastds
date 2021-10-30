package per.th.fastds.pool;

/**
 * @author th
 * @date 2021/10/6
 * @see
 * @since
 */
public class ConnectionPoolConfig {

    protected volatile long maxWaitTimeout = 3000;
    protected volatile int maxThread = 4;
    protected volatile int maxConnection = 400;
    protected volatile int minConnection = 8;
    protected volatile boolean initOnRun = true;
    protected volatile String driverClassName;
    protected volatile String jdbcUrl = "jdbc:h2:~/h2/ds";
    protected volatile String username;
    protected volatile String password;

}

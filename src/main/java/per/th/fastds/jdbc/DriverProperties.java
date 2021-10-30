package per.th.fastds.jdbc;

import java.util.Properties;

public
class DriverProperties extends Properties {

    public static final String URL = "url";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String DRIVER_CLASS_NAME = "driverClassName";

    public DriverProperties() {
        ;
    }

    public DriverProperties(Properties properties) {
        if (properties != null) {
            putAll(properties);
        }
    }

    public DriverProperties setUserName(String userName) {
        if (userName != null) {
            setProperty(USER, userName);
        }
        return this;
    }

    public String getUserName() {
        return getProperty(USER);
    }

    public DriverProperties setPassword(String password) {
        if (password != null) {
            setProperty(PASSWORD, password);
        }
        return this;
    }

    public String getPassword() {
        return getProperty(PASSWORD);
    }

    public DriverProperties setJdbcUrl(String jdbcUrl) {
        if (jdbcUrl != null) {
            setProperty(URL, jdbcUrl);
        }
        return this;
    }

    public String getJdbcUrl() {
        return getProperty("url");
    }

    public DriverProperties setDriverClassName(String driverClassName) {
        if (driverClassName != null) {
            setProperty(DRIVER_CLASS_NAME, driverClassName);
        }
        return this;
    }

    public String getDriverClassName() {
        return getProperty(DRIVER_CLASS_NAME);
    }

}

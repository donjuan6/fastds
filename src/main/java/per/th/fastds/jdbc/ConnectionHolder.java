package per.th.fastds.jdbc;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author th
 * @date 2021/10/11
 * @see
 * @since
 */
public class ConnectionHolder implements Connection {

    private final Connection delegated;

    public ConnectionHolder(Connection delegated) {
        this.delegated = delegated;
    }

    public Connection getConnection() {
        return delegated;
    }

    @Override
    public Statement createStatement() throws SQLException {
        return delegated.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return delegated.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return delegated.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return delegated.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        delegated.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return delegated.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        delegated.commit();
    }

    @Override
    public void rollback() throws SQLException {
        delegated.rollback();
    }

    @Override
    public void close() throws SQLException {
        delegated.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return delegated.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return delegated.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        delegated.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return delegated.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        delegated.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return delegated.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        delegated.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return delegated.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return delegated.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        delegated.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return delegated.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return delegated.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return delegated.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return delegated.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        delegated.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        delegated.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return delegated.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return delegated.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return delegated.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        delegated.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        delegated.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return delegated.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return delegated.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return delegated.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return delegated.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return delegated.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return delegated.prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return delegated.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return delegated.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return delegated.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return delegated.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return delegated.isValid(timeout);
    }

    public boolean isActived(long timeout) {
        try {
            return isValid((int) timeout);
        } catch (SQLException throwables) {
            return false;
        }
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        delegated.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        delegated.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return delegated.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return delegated.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return delegated.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return delegated.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        delegated.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return delegated.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        delegated.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        delegated.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return delegated.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegated.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegated.isWrapperFor(iface);
    }
}

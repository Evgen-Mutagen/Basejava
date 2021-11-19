package com.urise.webapp.sql;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @FunctionalInterface
    public interface ABlockOfCode<T> {
        T executeSpecific(PreparedStatement ps) throws SQLException;
    }

    public <T> T executeSql(String sql, ABlockOfCode<T> aBlockOfCode) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return aBlockOfCode.executeSpecific(ps);
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                throw new ExistStorageException("ERROR");
            } else {
                throw new StorageException(e.getSQLState());
            }
        }
    }
}

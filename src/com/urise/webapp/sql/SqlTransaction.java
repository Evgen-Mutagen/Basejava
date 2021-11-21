package com.urise.webapp.sql;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlTransaction<T> {
    T executeSql(Connection conn) throws SQLException;
}

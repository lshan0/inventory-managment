package com.bosch.coding.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionUtil {
    private static DBConnectionUtil instance;
    private final Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/inventory_management";
    private static final String USER = System.getenv("username");
    private static final String PASSWORD = System.getenv("password");

    private DBConnectionUtil() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("connection successfull");
        } catch (SQLException e) {
            throw new SQLException("Failed to create the database connection.", e);
        }
    }

    public static synchronized DBConnectionUtil getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DBConnectionUtil();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

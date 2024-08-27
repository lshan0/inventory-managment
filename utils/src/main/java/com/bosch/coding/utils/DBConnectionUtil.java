package com.bosch.coding.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnectionUtil {

    private static final HikariDataSource dataSource;
    private static final String USERNAME = System.getenv("USERNAME");
    private static final String PASSWORD = System.getenv("PASSWORD");
    private static final String JDBC_URL = System.getenv("JDBC_URL");

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10); // Adjust based on your needs
        config.setConnectionTimeout(30000); // Adjust based on your needs
        config.setIdleTimeout(600000); // Adjust based on your needs

        dataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

}

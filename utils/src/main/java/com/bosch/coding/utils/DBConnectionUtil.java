package com.bosch.coding.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnectionUtil {

    private static final HikariDataSource dataSource;
    private static final String USER = System.getenv("USER");
    private static final String PASSWORD = System.getenv("PASSWORD");
    private static final String JDBC_URL = System.getenv("JDBC_URL");
    private static final int MAX_POOL_SIZE = 10;
    private static final int TIMEOUT_DURATION = 30000;
    private static final int IDLE_TIMEOUT = 600000;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(USER);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(MAX_POOL_SIZE);
        config.setConnectionTimeout(TIMEOUT_DURATION);
        config.setIdleTimeout(IDLE_TIMEOUT);

        dataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

}

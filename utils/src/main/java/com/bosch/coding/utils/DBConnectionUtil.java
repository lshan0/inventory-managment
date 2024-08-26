package com.bosch.coding.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnectionUtil {

    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/inventory_management");
        config.setUsername("bosch");
        config.setPassword("very_secret");
        config.setMaximumPoolSize(10); // Adjust based on your needs
        config.setConnectionTimeout(30000); // Adjust based on your needs
        config.setIdleTimeout(600000); // Adjust based on your needs

        dataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

}

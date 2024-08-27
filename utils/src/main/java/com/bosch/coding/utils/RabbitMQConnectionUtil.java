package com.bosch.coding.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQConnectionUtil {

    private static final String HOST = System.getenv("HOST");
    private static final String PORT = System.getenv("PORT");
    private static final String USERNAME = System.getenv("USERNAME");
    private static final String PASSWORD = System.getenv("PASSWORD");

    private static final int CONNECTION_ATTEMPT_COUNT = 10;
    private static final int RETRY_COOLDOWN = 10000;

    public static Connection establishConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(Integer.parseInt(PORT));
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);

        Connection connection = null;
        int attempts = 0;
        while (connection == null && attempts < CONNECTION_ATTEMPT_COUNT) {
            try {
                connection = factory.newConnection();
            } catch (IOException | TimeoutException e) {
                attempts++;
                try {
                    Thread.sleep(RETRY_COOLDOWN); // Wait 10 seconds before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (connection == null) {
            throw new RuntimeException("Failed to connect to RabbitMQ after several attempts.");
        }
        return connection;
    }
}

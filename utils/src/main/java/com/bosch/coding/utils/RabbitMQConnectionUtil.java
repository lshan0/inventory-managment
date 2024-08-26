package com.bosch.coding.utils;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQConnectionUtil {

    private static final String HOSTNAME = System.getenv("hostname");
    private static final String PORT = System.getenv("port");
    private static final String USERNAME = System.getenv("username");
    private static final String PASSWORD = System.getenv("password");

    public static ConnectionFactory establishConnection() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        factory.setPort(Integer.parseInt(PORT));
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);

        return factory;
    }
}

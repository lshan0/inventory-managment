package com.bosch.coding;

import com.bosch.coding.utils.RabbitMQConnectionUtil;
import com.bosch.coding.utils.InventoryRequestEvent;
import com.bosch.coding.utils.InventoryRequestEventFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    public static final String EXCHANGE_NAME = "fruit_exchange";

    public static void main(String[] args) {
        InventoryRequestEventFactory factory = new InventoryRequestEventFactory();
        // Setup RabbitMQ connection
        ConnectionFactory rabbitMQConnection = RabbitMQConnectionUtil.establishConnection();

        try (Connection connection = rabbitMQConnection.newConnection();
             Channel channel = connection.createChannel()) {

            // Declare the exchange
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

            // Declare queues and bind them to the exchange based on the fruit type
            for (String fruit : InventoryRequestEventFactory.getFruits()) {
                String queueName = fruit + "_queue";
                channel.queueDeclare(queueName, true, false, false, null);
                channel.queueBind(queueName, EXCHANGE_NAME, fruit + "_key");
            }

            // Generate and publish events
            while (true) {
                InventoryRequestEvent event = factory.createEvent();

                // Publish the event to the appropriate fruit-specific queue
                String routingKey = event.getFruit() + "_key";
                String message = event.toString();
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());

                System.out.println(" [x] Sent '" + message + "' to " + event.getFruit() + "_queue with routing key " + routingKey);

                // Slow down the event generation
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
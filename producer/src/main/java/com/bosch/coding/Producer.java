package com.bosch.coding;

import com.bosch.coding.utils.RabbitMQConnectionUtil;
import com.bosch.coding.utils.InventoryRequestEvent;
import com.bosch.coding.utils.InventoryRequestEventFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    public static final String EXCHANGE_NAME = "fruit_exchange";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        System.out.println("Starting");
        InventoryRequestEventFactory factory = new InventoryRequestEventFactory();

        try (Connection connection = RabbitMQConnectionUtil.establishConnection();
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
                String routingKey = event.getProductName() + "_key";
                String message = objectMapper.writeValueAsString(event);
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));

                logger.info("Sent '{}' to {}_queue with routing key {}", message, event.getProductName(), routingKey);
                try {
                    Thread.sleep(100); // slow
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
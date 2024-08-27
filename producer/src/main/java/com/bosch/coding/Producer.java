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
    public static final String EXCHANGE_NAME = "fruits_exchange";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ROUTING_KEY = "fruits_key";
    private static final String QUEUE_NAME = "fruits_queue";
    private static final String QUEUE_TYPE = "direct";

    public static void main(String[] args) {
        logger.info("Starting Fruit Producer");

        InventoryRequestEventFactory factory = new InventoryRequestEventFactory();

        try (Connection connection = RabbitMQConnectionUtil.establishConnection();
             Channel channel = connection.createChannel()) {

            // Declare the exchange, queue and bind them using a routing key
            channel.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE, true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            // Generate and publish events
            while (true) {
                InventoryRequestEvent event = factory.createEvent();

                // Publish the event to the appropriate fruit-specific queue
                String message = objectMapper.writeValueAsString(event);
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes(StandardCharsets.UTF_8));

                logger.info("Sent {} to '{}'", message, QUEUE_NAME);
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
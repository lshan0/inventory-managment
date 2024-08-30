package com.bosch.coding;

import com.bosch.coding.consumers.FruitConsumer;
import com.bosch.coding.consumers.VegetableConsumer;
import com.bosch.coding.repositories.FruitInventoryRepository;
import com.bosch.coding.repositories.InventoryRepository;
import com.bosch.coding.services.InventoryService;
import com.bosch.coding.utils.RabbitMQConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerMain {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerMain.class);

    private static final String FRUITS_EXCHANGE_NAME = "fruits_exchange";
    private static final String FRUITS_ROUTING_KEY = "fruits_key";
    private static final String FRUITS_QUEUE_NAME = "fruits_queue";

    private static final String VEGETABLES_EXCHANGE_NAME = "vegetables_exchange";
    private static final String VEGETABLES_ROUTING_KEY = "vegetables_key";
    private static final String VEGETABLES_QUEUE_NAME = "vegetables_queue";

    private static final String EXCHANGE_TYPE = "direct";

    public static void main(String[] args) {
        final InventoryRepository inventoryRepository = new FruitInventoryRepository();
        final InventoryService inventoryService = new InventoryService(inventoryRepository);

        // Separate connections and channel can be created for the consumers
        try (Connection connection = RabbitMQConnectionUtil.establishConnection();
                final Channel channel = connection.createChannel()) {

            // Declare the exchange, queues, and bind them using routing keys
            channel.exchangeDeclare(FRUITS_EXCHANGE_NAME, EXCHANGE_TYPE, true);
            channel.exchangeDeclare(VEGETABLES_EXCHANGE_NAME, EXCHANGE_TYPE, true);

            // Fruits Queue
            channel.queueDeclare(FRUITS_QUEUE_NAME, true, false, false, null);
            channel.queueBind(FRUITS_QUEUE_NAME, FRUITS_EXCHANGE_NAME, FRUITS_ROUTING_KEY);
            channel.basicConsume(FRUITS_QUEUE_NAME, false, new FruitConsumer(channel, inventoryService));

            // Vegetable Queue
            channel.queueDeclare(VEGETABLES_QUEUE_NAME, true, false, false, null);
            channel.queueBind(VEGETABLES_QUEUE_NAME, VEGETABLES_EXCHANGE_NAME, VEGETABLES_ROUTING_KEY);
            channel.basicConsume(VEGETABLES_QUEUE_NAME, false, new VegetableConsumer(channel, inventoryService));

            // Keep the application running
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            logger.info("Unexpected error during consumer creation: {}", e.getMessage());
        }
    }

}

package com.bosch.coding.producers;

import com.bosch.coding.dto.ProducerResponse;
import com.bosch.coding.entity.InventoryItem;
import com.bosch.coding.utils.RabbitMQConnectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class FruitProducer implements Producer {
    Connection connection;
    Channel channel;

    private static final Logger logger = LoggerFactory.getLogger(FruitProducer.class);

    public static final String EXCHANGE_NAME = "fruits_exchange";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ROUTING_KEY = "fruits_key";
    private static final String QUEUE_NAME = "fruits_queue";
    private static final String QUEUE_TYPE = "direct";

    public FruitProducer() {
        logger.info("Starting Fruits Producer");

        try {
            this.connection = RabbitMQConnectionUtil.establishConnection();
            this.channel = connection.createChannel();
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while establishing connection for {}. Error - ", QUEUE_NAME, e);
        }
    }

    @Override
    public String getQueueType() {
        return QUEUE_TYPE;
    }

    @Override
    public String getExchangeName() {
        return EXCHANGE_NAME;
    }

    @Override
    public String getRoutingKey() {
        return ROUTING_KEY;
    }

    @Override
    public void startProducing(InventoryItem inventoryItem) {
        try {
            // Declare the exchange, queue and bind them using a routing key
            channel.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE, true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);


            String message = objectMapper.writeValueAsString(new ProducerResponse(
                    inventoryItem.getProductName(),
                    inventoryItem.getType(),
                    inventoryItem.getQuantity(),
                    inventoryItem.getCommand())
            );
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes(StandardCharsets.UTF_8));

            logger.info("Sent {} to '{}'", message, QUEUE_NAME);
        } catch (Exception e) {
            logger.error("Unexpected exception occurred while producing message", e);
        }
    }
}

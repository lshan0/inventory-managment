package com.bosch.coding;

import com.bosch.coding.repositories.FruitInventoryRepository;
import com.bosch.coding.repositories.InventoryRepository;
import com.bosch.coding.services.InventoryService;
import com.bosch.coding.dto.InventoryRequest;
import com.bosch.coding.utils.RabbitMQConnectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import com.rabbitmq.client.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class FruitConsumer extends DefaultConsumer{

    InventoryService inventoryService;

    private static final Logger logger = LoggerFactory.getLogger(FruitConsumer.class);
    private static final String EXCHANGE_NAME = "fruits_exchange";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ROUTING_KEY = "fruits_key";
    private static final String QUEUE_NAME = "fruits_queue";
    private static final String QUEUE_TYPE = "direct";

    // Constructs a new instance and records its association to the passed-in channel.
    public FruitConsumer(Channel channel, InventoryService inventoryService) {
        super(channel);
        this.inventoryService = inventoryService;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        InventoryRequest request;
        try {
            request = objectMapper.readValue(body, InventoryRequest.class);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse message: " + e.getMessage());
            // Reject the message and not requeue
            getChannel().basicReject(envelope.getDeliveryTag(), false);
            return;
        }

        String queue = envelope.getRoutingKey();
        logger.info("Received {} from '{}'", request.toString(), QUEUE_NAME);

        // Process the message based on the queue
        try {
            inventoryService.processRequest(request);
            getChannel().basicAck(envelope.getDeliveryTag(), false);
        } catch (SQLException e) {
            System.err.println("Database error during processing: " + e.getMessage());
            getChannel().basicReject(envelope.getDeliveryTag(), false);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            getChannel().basicReject(envelope.getDeliveryTag(), false);
        }
    }

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        logger.info("Starting Fruit Consumer");
        final InventoryRepository inventoryRepository = new FruitInventoryRepository();
        final InventoryService inventoryService = new InventoryService(inventoryRepository);

        try (Connection connection = RabbitMQConnectionUtil.establishConnection();
             final Channel channel = connection.createChannel()) {

            // Declare the exchange, queue and bind them using a routing key
            channel.exchangeDeclare(EXCHANGE_NAME, QUEUE_TYPE, true);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            channel.basicConsume(QUEUE_NAME, false, new FruitConsumer(channel, inventoryService));

            // Keep the application running
            while (true) {
                Thread.sleep(1000);
            }
        }
    }
}
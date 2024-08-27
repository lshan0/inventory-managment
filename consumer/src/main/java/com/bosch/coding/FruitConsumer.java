package com.bosch.coding;

import com.bosch.coding.Repositories.FruitInventoryRepository;
import com.bosch.coding.Repositories.InventoryRepository;
import com.bosch.coding.Services.InventoryService;
import com.bosch.coding.dto.InventoryRequest;
import com.bosch.coding.utils.RabbitMQConnectionUtil;
import com.bosch.coding.utils.InventoryRequestEventFactory;
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

    private static final String EXCHANGE_NAME = "fruit_exchange";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(FruitConsumer.class);

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
        System.out.println(" [x] Received '" + request.toString() + "' from " + queue + "_queue");

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
        System.out.println("Sleeping");
        Thread.sleep(50000);
        final InventoryRepository inventoryRepository = new FruitInventoryRepository();
        final InventoryService inventoryService = new InventoryService(inventoryRepository);

        final String[] fruits = InventoryRequestEventFactory.getFruits();

        try (Connection connection = RabbitMQConnectionUtil.establishConnection();
             final Channel channel = connection.createChannel()) {
            System.out.println("Connection Successful");
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

            // Declare queues and bind them to the exchange based on the fruit type
            for (final String fruit : fruits) {
                final String queueName = fruit + "_queue";
                channel.queueDeclare(queueName, true, false, false, null);
                channel.queueBind(queueName, EXCHANGE_NAME, queueName.replace("_queue", "_key"));
            }

            // Create a FruitConsumer for each queue
            for (final String fruit : fruits) {
                channel.basicConsume(fruit + "_queue", false, new FruitConsumer(channel, inventoryService));
            }

            // Keep the application running
            while (true) {
                Thread.sleep(1000);
            }
        }
    }
}
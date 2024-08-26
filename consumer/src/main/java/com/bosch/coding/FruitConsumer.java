package com.bosch.coding;

import com.bosch.coding.Repositories.FruitInventoryRepository;
import com.bosch.coding.Repositories.InventoryRepository;
import com.bosch.coding.Services.InventoryService;
import com.bosch.coding.dto.InventoryRequest;
import com.bosch.coding.utils.RabbitMQConnectionUtil;
import com.bosch.coding.utils.InventoryRequestEventFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class FruitConsumer extends DefaultConsumer{

    InventoryService inventoryService;
    private static final String EXCHANGE_NAME = "fruit_exchange";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Constructs a new instance and records its association to the passed-in channel.
    public FruitConsumer(Channel channel, InventoryService inventoryService) {
        super(channel);
        this.inventoryService = inventoryService;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        InventoryRequest request = objectMapper.readValue(body, InventoryRequest.class);
        String queue = envelope.getRoutingKey();

        System.out.println(" [x] Received '" + request.toString() + "' from " + queue + "_queue");

        // Process the message based on the queue
        try {
            inventoryService.processRequest(request);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Acknowledge the message
        getChannel().basicAck(envelope.getDeliveryTag(), false);
    }

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        final InventoryRepository inventoryRepository = new FruitInventoryRepository();
        final InventoryService inventoryService = new InventoryService(inventoryRepository);

        final ConnectionFactory rabbitMQConnection = RabbitMQConnectionUtil.establishConnection();
        final String[] fruits = InventoryRequestEventFactory.getFruits();

        try (Connection connection = rabbitMQConnection.newConnection();
             final Channel channel = connection.createChannel()) {
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
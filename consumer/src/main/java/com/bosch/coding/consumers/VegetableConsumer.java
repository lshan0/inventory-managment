package com.bosch.coding.consumers;

import com.bosch.coding.dto.ProducerRequest;
import com.bosch.coding.services.InventoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public class VegetableConsumer extends DefaultConsumer {

    InventoryService inventoryService;

    private static final Logger logger = LoggerFactory.getLogger(VegetableConsumer.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String QUEUE_NAME = "vegetables_queue";

    // Constructs a new instance and records its association to the passed-in channel.
    public VegetableConsumer(Channel channel, InventoryService inventoryService) {
        super(channel);
        this.inventoryService = inventoryService;
        logger.info("Starting Vegetable Consumer");
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        ProducerRequest request;

        try {
            request = objectMapper.readValue(body, ProducerRequest.class);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse message: " + e.getMessage());
            // Reject the message and not requeue
            getChannel().basicReject(envelope.getDeliveryTag(), false);
            return;
        }

        logger.info("Received {} from '{}'", request.toString(), QUEUE_NAME);

        // Process the message based on the queue
        try {
            inventoryService.processRequest(request);
            getChannel().basicAck(envelope.getDeliveryTag(), false);
        } catch (SQLException e) {
            logger.info("Database error during processing: {}", e.getMessage());
            getChannel().basicReject(envelope.getDeliveryTag(), false);
        } catch (Exception e) {
            logger.info("Unexpected error: {}", e.getMessage());
            getChannel().basicReject(envelope.getDeliveryTag(), false);
        }
    }
}

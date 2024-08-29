package com.bosch.coding.producers;

import com.bosch.coding.utils.InventoryItem;
import com.bosch.coding.utils.RabbitMQConnectionUtil;
import com.bosch.coding.utils.InventoryRequestEvent;
import com.bosch.coding.utils.InventoryRequestEventFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public interface Producer {

    public String getQueueType();

    public String getExchangeName();

    public String getRoutingKey();

    public void startProducing(InventoryItem inventoryItem);
}
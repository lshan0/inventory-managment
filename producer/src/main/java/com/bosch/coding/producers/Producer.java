package com.bosch.coding.producers;

import com.bosch.coding.entity.InventoryItem;

public interface Producer {

    public String getQueueType();

    public String getExchangeName();

    public String getRoutingKey();

    public void startProducing(InventoryItem inventoryItem);
}
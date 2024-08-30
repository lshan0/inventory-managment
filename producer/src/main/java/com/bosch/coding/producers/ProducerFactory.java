package com.bosch.coding.producers;

import com.bosch.coding.enums.InventoryItemType;

import java.util.HashMap;
import java.util.Map;

public class ProducerFactory {
    // Map to hold instances of each producer
    private static final Map<InventoryItemType, Producer> producerMap = new HashMap<>();

    public static synchronized Producer getProducer(final InventoryItemType itemType) {
        if (!producerMap.containsKey(itemType)) {
            final Producer producer = createProducer(itemType);
            producerMap.put(itemType, producer);
        }
        return producerMap.get(itemType);
    }

    // Method to create a producer based on item type
    private static Producer createProducer(final InventoryItemType itemType) {
        switch (itemType) {
            case FRUIT:
                return new FruitProducer();
            case VEGETABLE:
                return new VegetableProducer();
            default:
                throw new IllegalArgumentException("Unknown item type: " + itemType);
        }
    }
}

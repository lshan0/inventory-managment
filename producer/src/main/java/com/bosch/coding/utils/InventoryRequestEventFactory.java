package com.bosch.coding.utils;

import com.bosch.coding.entity.InventoryItem;
import com.bosch.coding.entity.InventoryRequestEvent;
import com.bosch.coding.enums.InventoryItemType;
import com.bosch.coding.enums.Update;

import java.util.*;

public class InventoryRequestEventFactory {
    private static final String[] fruits = {"Apples", "Bananas", "Coconuts", "Dates", "Elderberries", "Fig", "Grape", "Honeydew"};
    private static final String[] vegetables = {"Avocado", "Broccoli", "Carrot", "Daiki", "Eggplant", "Fennel", "Ginger", "Horseradish"};
    private static Random random = new Random();

    public static String[] getFruits() {
        return fruits;
    }

    public static String[] getVegetables() {
        return vegetables;
    }

    private static final Map<InventoryItemType, String[]> map = createInventoryMap();

    private static Map<InventoryItemType, String[]> createInventoryMap() {
        Map<InventoryItemType, String[]> inventoryMap = new HashMap<>();
        inventoryMap.put(InventoryItemType.FRUIT, fruits);
        inventoryMap.put(InventoryItemType.VEGETABLE, vegetables);
        return Collections.unmodifiableMap(inventoryMap); // Make the map unmodifiable
    }

    public static InventoryRequestEvent createEvent() {
        final InventoryRequestEvent event = new InventoryRequestEvent();

        //Randomly decide the number of items to add
        final int numOfItems = random.nextInt(5) + 1;

        for (int i = 0; i < numOfItems; i++) {
            final InventoryItemType itemType= getRandomItemType();

            final InventoryItem item = new InventoryItem.Builder()
                    .withProductName(getRandomItem(itemType))
                    .withType(itemType)
                    .withQuantity(random.nextInt(5) + 1)
                    .withCommand(Update.randomEnum())
                    .build();

            event.addItem(item);
        }

        return event;
    }

    private static InventoryItemType getRandomItemType() {
        return random.nextBoolean() ? InventoryItemType.FRUIT : InventoryItemType.VEGETABLE;
    }

    private static String getRandomItem(final InventoryItemType type) {
        final String[] items = map.get(type);
        return items[random.nextInt(items.length)];
    }
}
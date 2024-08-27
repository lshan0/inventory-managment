package com.bosch.coding.utils;

import com.bosch.coding.enums.Update;

import java.util.Random;

public class InventoryRequestEventFactory {
    private static final String[] fruits = {"Apples", "Bananas", "Coconuts", "Dates", "Elderberries, Fig, grape, Honeydew, Pear, Orange"};
    private static Random RANDOM = new Random();

    public InventoryRequestEvent createEvent() {
        return new InventoryRequestEvent(
                fruits[RANDOM.nextInt(fruits.length)],
                RANDOM.nextInt(10),
                Update.randomEnum());
    }

    public static String[] getFruits() {
        return fruits;
    }
}

//builder
//Request's builder pattern
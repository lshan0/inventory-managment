package com.bosch.coding.utils;

import com.bosch.coding.enums.Update;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryRequestEventFactoryTest {

    InventoryRequestEventFactory factory = new InventoryRequestEventFactory();

    @Test
    public void testWarehouseRequestEvent() {
        InventoryRequestEvent event = new InventoryRequestEvent("Apples", 5, Update.ADD);
        assertEquals("Apples", event.getProductName());
        assertEquals(5, event.getQuantity());
        assertEquals(Update.ADD, event.getCommand());
    }

    @Test
    public void testWarehouseRequestEventFactory() {
        InventoryRequestEvent event = factory.createEvent();
        assertNotNull(event);
        assertNotNull(event.getProductName());
        assertNotNull(event.getCommand());
    }

    @Test
    public void testProductNameValidity() {
        InventoryRequestEvent event = factory.createEvent();

        List<String> validFruits = Arrays.asList(InventoryRequestEventFactory.getFruits());
        assertTrue(validFruits.contains(event.getProductName()));
    }

    @Test
    public void testInRangeQuantities() {
        InventoryRequestEvent event = factory.createEvent();
        int quantity = event.getQuantity();

        assertTrue(quantity >= 0 && quantity <= 10);
    }
}

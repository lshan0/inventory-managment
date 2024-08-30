package com.bosch.coding;

import com.bosch.coding.enums.InventoryItemType;
import com.bosch.coding.enums.Update;
import com.bosch.coding.entity.InventoryItem;
import com.bosch.coding.entity.InventoryRequestEvent;
import com.bosch.coding.utils.InventoryRequestEventFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryRequestEventFactoryTest {

    @Test
    public void testWarehouseRequestEvent() {
        InventoryItem item = new InventoryItem.Builder()
                .withProductName("Apples")
                .withType(InventoryItemType.FRUIT)
                .withQuantity(5)
                .withCommand(Update.ADD)
                .build();
        InventoryRequestEvent event = new InventoryRequestEvent(List.of(item));
        assertEquals("Apples", event.getInventoryItems().get(0).getProductName());
        assertEquals(5, event.getInventoryItems().get(0).getQuantity());
        assertEquals(Update.ADD, event.getInventoryItems().get(0).getCommand());
    }

    @Test
    public void testWarehouseRequestEventFactory() {
        InventoryRequestEvent event = InventoryRequestEventFactory.createEvent();
        assertNotNull(event);
        assertNotNull(event.getInventoryItems().get(0).getProductName());
        assertNotNull(event.getInventoryItems().get(0).getCommand());
    }

    @Test
    public void testProductNameValidity() {
        InventoryRequestEvent event = InventoryRequestEventFactory.createEvent();
        String productName = event.getInventoryItems().get(0).getProductName();
        List<String> validFruits = Arrays.asList(InventoryRequestEventFactory.getFruits());
        List<String> validVegetables = Arrays.asList(InventoryRequestEventFactory.getVegetables());
        assertTrue(validFruits.contains(productName) || validVegetables.contains(productName));
    }

    @Test
    public void testInRangeQuantities() {
        InventoryRequestEvent event = InventoryRequestEventFactory.createEvent();
        InventoryItem item = event.getInventoryItems().get(0);
        int quantity = item.getQuantity();

        assertTrue(quantity >= 0 && quantity <= 10);
    }
}

package com.bosch.coding;

import com.bosch.coding.enums.InventoryItemType;
import com.bosch.coding.enums.Update;
import com.bosch.coding.entity.InventoryItem;
import com.bosch.coding.entity.InventoryRequestEvent;
import com.bosch.coding.utils.InventoryRequestEventFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryRequestEventFactoryTest {

    InventoryRequestEventFactory factory = new InventoryRequestEventFactory();

    @Test
    public void testWarehouseRequestEvent() {
        InventoryRequestEvent event = new InventoryRequestEvent(List.of(new InventoryItem("Apples", InventoryItemType.FRUIT, 5, Update.ADD)));
        assertEquals("Apples", event.getInventoryItems().get(0).getProductName());
        assertEquals(5, event.getInventoryItems().get(0).getQuantity());
        assertEquals(Update.ADD, event.getInventoryItems().get(0).getCommand());
    }

//    @Test
//    public void testWarehouseRequestEventFactory() {
//        InventoryRequestEvent event = factory.createEvent();
//        assertNotNull(event);
//        assertNotNull(event.getProductName());
//        assertNotNull(event.getCommand());
//    }
//
//    @Test
//    public void testProductNameValidity() {
//        InventoryRequestEvent event = factory.createEvent();
//
//        List<String> validFruits = Arrays.asList(InventoryRequestEventFactory.getFruits());
//        assertTrue(validFruits.contains(event.getProductName()));
//    }
//
//    @Test
//    public void testInRangeQuantities() {
//        InventoryRequestEvent event = factory.createEvent();
//        int quantity = event.getQuantity();
//
//        assertTrue(quantity >= 0 && quantity <= 10);
//    }
}

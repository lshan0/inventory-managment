package com.bosch.coding;

import com.bosch.coding.enums.Update;
import com.bosch.coding.utils.InventoryRequestEvent;
import com.bosch.coding.utils.InventoryRequestEventFactory;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ProducerTest {

    @Test
    public void testWarehouseRequestEvent() {
        InventoryRequestEvent event = new InventoryRequestEvent("apples", 5, Update.ADD);
        assertEquals("apples", event.getProductName());
        assertEquals(5, event.getQuantity());
        assertEquals(Update.ADD, event.getCommand());
    }

    @Test
    public void testWarehouseRequestEventFactory() {
        InventoryRequestEventFactory factory = new InventoryRequestEventFactory();
        InventoryRequestEvent event = factory.createEvent();
        assertNotNull(event);
        assertNotNull(event.getProductName());
        assertNotNull(event.getCommand());
        assertTrue(event.getQuantity() >= 0 && event.getQuantity() < 10);
    }
}
package com.bosch.coding.utils;

import java.util.ArrayList;
import java.util.List;

public final class InventoryRequestEvent {
    private final List<InventoryItem> inventoryItems;

    public InventoryRequestEvent(List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public InventoryRequestEvent() {
        this.inventoryItems = new ArrayList<>();
    }

    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void addItem(InventoryItem item) {
        inventoryItems.add(item);
    }
}
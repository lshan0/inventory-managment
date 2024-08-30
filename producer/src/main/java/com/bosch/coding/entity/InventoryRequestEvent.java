package com.bosch.coding.entity;

import java.util.ArrayList;
import java.util.List;

public final class InventoryRequestEvent {
    private final List<InventoryItem> inventoryItems;

    public InventoryRequestEvent(final List<InventoryItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }

    public InventoryRequestEvent() {
        this.inventoryItems = new ArrayList<>();
    }

    public List<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    public void addItem(final InventoryItem item) {
        inventoryItems.add(item);
    }
}
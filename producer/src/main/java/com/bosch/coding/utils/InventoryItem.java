package com.bosch.coding.utils;

import com.bosch.coding.enums.InventoryItemType;
import com.bosch.coding.enums.Update;

public class InventoryItem {
    private final String productName;
    private final InventoryItemType type;
    private final int quantity;
    private final Update command;

    public InventoryItem(String productName, InventoryItemType type, int quantity, Update command) {
        this.productName = productName;
        this.type = type;
        this.quantity = quantity;
        this.command = command;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public Update getCommand() {
        return command;
    }

    public InventoryItemType getType() {
        return type;
    }
}

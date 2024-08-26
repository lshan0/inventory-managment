package com.bosch.coding.utils;

import com.bosch.coding.enums.Update;

public final class InventoryRequestEvent {
    private final String productName;
    private final Integer quantity;
    private final Update command;

    public InventoryRequestEvent(String productName, Integer quantity, Update command) {
        this.productName = productName;
        this.quantity = quantity;
        this.command = command;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Update getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "InventoryRequestEvent{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", command='" + command + '\'' +
                '}';
    }
}

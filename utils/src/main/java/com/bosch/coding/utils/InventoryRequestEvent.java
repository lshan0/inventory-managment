package com.bosch.coding.utils;

import com.bosch.coding.enums.Update;

public final class InventoryRequestEvent {
    private final String fruit;
    private final Integer quantity;
    private final Update command;

    public InventoryRequestEvent(String fruit, Integer quantity, Update command) {
        this.fruit = fruit;
        this.quantity = quantity;
        this.command = command;
    }

    public String getFruit() {
        return fruit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Update getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "WarehouseRequestEvent{" +
                "fruit='" + fruit + '\'' +
                ", quantity=" + quantity +
                ", command='" + command + '\'' +
                '}';
    }
}

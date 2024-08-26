package com.bosch.coding.dto;

import com.bosch.coding.enums.Update;

public record InventoryRequest(String productName, Integer quantity, Update command) {

    @Override
    public String toString() {
        return "WarehouseRequestEvent{" +
                "fruit='" + productName + '\'' +
                ", quantity=" + quantity +
                ", command='" + command + '\'' +
                '}';
    }
}

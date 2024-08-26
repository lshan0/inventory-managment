package com.bosch.coding.dto;

public record InventoryResponse(String productName, int quantity) {

    @Override
    public String toString() {
        return "InventoryResponse{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}

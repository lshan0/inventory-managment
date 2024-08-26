package com.bosch.coding.dto;

public class InventoryResponse {
    private final String productName;
    private final int quantity;

    public InventoryResponse(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "InventoryResponse{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}

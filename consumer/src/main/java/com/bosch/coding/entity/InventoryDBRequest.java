package com.bosch.coding.entity;

import com.bosch.coding.enums.InventoryItemType;

public class InventoryDBRequest {

    private final String productName;
    private final InventoryItemType type;
    private final int quantity;
    private final int version;

    private InventoryDBRequest(Builder builder) {
        this.productName = builder.productName;
        this.type = builder.type;
        this.quantity = builder.quantity;
        this.version = builder.version;
    }

    public String getProductName() {
        return productName;
    }

    public InventoryItemType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getVersion() {
        return version;
    }

    public static class Builder {
        private String productName;
        private InventoryItemType type;
        private int quantity;
        private int version = 1;

        public Builder() {

        }

        public Builder withProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Builder withType(InventoryItemType type) {
            this.type = type;
            return this;
        }

        public Builder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder withVersion(int version) {
            this.version = version;
            return this;
        }

        // Builds the final InventoryRequest object
        public InventoryDBRequest build() {
            return new InventoryDBRequest(this);
        }
    }
}

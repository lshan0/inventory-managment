package com.bosch.coding.entity;

import com.bosch.coding.enums.InventoryItemType;
import com.bosch.coding.enums.Update;

public class InventoryItem {
    private final String productName;
    private final InventoryItemType type;
    private final int quantity;
    private final Update command;

    private InventoryItem(Builder builder) {
        this.productName = builder.productName;
        this.type = builder.type;
        this.quantity = builder.quantity;
        this.command = builder.command;
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

    public Update getCommand() {
        return command;
    }

    public static class Builder {
        private String productName;
        private InventoryItemType type;
        private int quantity;
        private Update command;

        public Builder() {

        }

        public Builder withProductName(final String productName) {
            this.productName = productName;
            return this;
        }

        public Builder withType(final InventoryItemType type) {
            this.type = type;
            return this;
        }

        public Builder withQuantity(final int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder withCommand(final Update command) {
            this.command = command;
            return this;
        }

        public InventoryItem build() {
            return new InventoryItem(this);
        }
    }
}


package com.bosch.coding.enums;

import java.util.Random;

public enum InventoryItemType {
    FRUIT,
    VEGETABLE;

    public static InventoryItemType randomEnum(){
        int x = new Random().nextInt(InventoryItemType.values().length);
        return InventoryItemType.values()[x];
    }
}

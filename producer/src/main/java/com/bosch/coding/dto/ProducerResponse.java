package com.bosch.coding.dto;

import com.bosch.coding.enums.InventoryItemType;
import com.bosch.coding.enums.Update;

public record ProducerResponse(String productName,
                               InventoryItemType type,
                               int quantity,
                               Update command) {
}

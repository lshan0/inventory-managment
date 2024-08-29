package com.bosch.coding.dto;

import com.bosch.coding.enums.InventoryItemType;

public record InventoryDBResponse(String productName,
                                  int quantity,
                                  InventoryItemType type,
                                  int version)
{
}

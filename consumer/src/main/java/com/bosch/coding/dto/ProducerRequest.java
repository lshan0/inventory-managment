package com.bosch.coding.dto;

import com.bosch.coding.enums.InventoryItemType;
import com.bosch.coding.enums.Update;

public record ProducerRequest(String productName,
                              InventoryItemType type,
                              Integer quantity,
                              Update command)
{ }

package com.bosch.coding.dto;

public record InventoryDBResponse(String productName,
                                  int quantity,
                                  int version)
{
}

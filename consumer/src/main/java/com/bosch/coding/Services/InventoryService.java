package com.bosch.coding.Services;

import com.bosch.coding.Repositories.InventoryRepository;
import com.bosch.coding.dto.InventoryRequest;
import com.bosch.coding.dto.InventoryResponse;
import com.bosch.coding.enums.Update;

import java.sql.SQLException;
import java.util.Optional;

public class InventoryService {

    InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void processRequest(InventoryRequest request) throws SQLException {
        if (request.productName() == null || request.productName().isEmpty()) {
            throw new RuntimeException("Invalid product name");
        }
        Update command = request.command();
        String productName = request.productName();
        int quantity = request.quantity();

        switch (command) {
            case ADD -> {
                Optional<InventoryResponse> currentStock = inventoryRepository.getInventoryByProductName(request);
                if (currentStock.isEmpty())
            }
            case REMOVE -> {

            }
            default -> throw new RuntimeException("Invalid Inventory Operation");
        }
    }
}

package com.bosch.coding.services;

import com.bosch.coding.repositories.InventoryRepository;
import com.bosch.coding.dto.InventoryRequest;
import com.bosch.coding.dto.InventoryResponse;
import com.bosch.coding.enums.Update;
import com.bosch.coding.exceptions.InsufficientStockException;
import com.bosch.coding.exceptions.InvalidInventoryRequestException;
import com.bosch.coding.exceptions.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

// singleton
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);
    InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void processRequest(InventoryRequest request) throws SQLException {
        Update command = request.command();
        String productName = request.productName();
        int quantity = request.quantity();

        if (productName == null || productName.isEmpty()) {
            logger.error("Invalid Product Name: {}", productName);
            throw new InvalidInventoryRequestException("Invalid product name");
        }

        try {
            switch (command) {
                case ADD -> handleAddCommand(productName, quantity);
                case REMOVE -> handleRemoveCommand(productName, quantity);
                default -> throw new InvalidInventoryRequestException("Operation does not exist");
            }
        } catch (SQLException e) {
            logger.error("Database error occured:");
            e.printStackTrace();
            throw e;
        }
    }

    private void handleAddCommand(String productName, int quantity) throws SQLException {
        Optional<InventoryResponse> currentStock = inventoryRepository.getInventoryByProductName(productName);
        if (currentStock.isEmpty()) {
            inventoryRepository.addInventory(new InventoryRequest(productName, quantity, Update.ADD));
        } else {
            int newStock = currentStock.get().quantity() + quantity;
            inventoryRepository.updateInventory(new InventoryRequest(productName, newStock, Update.ADD));
        }
    }

    private void handleRemoveCommand(String productName, int quantity) throws SQLException {
        Optional<InventoryResponse> currentStock = inventoryRepository.getInventoryByProductName(productName);
        if (currentStock.isEmpty()) {
            throw new ProductNotFoundException("Cannot remove " + productName + " .Product does not exist");
        }

        int newStock = currentStock.get().quantity() - quantity;
        if (newStock < 0) {
            throw new InsufficientStockException("Not enough stock available for product = " + productName + " quantity needed = " + quantity);
        }

        inventoryRepository.updateInventory(new InventoryRequest(productName, newStock, Update.REMOVE));
    }
}

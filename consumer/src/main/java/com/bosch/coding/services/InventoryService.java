package com.bosch.coding.services;

import com.bosch.coding.dto.ProducerRequest;
import com.bosch.coding.enums.InventoryItemType;
import com.bosch.coding.repositories.InventoryRepository;
import com.bosch.coding.entity.InventoryDBRequest;
import com.bosch.coding.dto.InventoryDBResponse;
import com.bosch.coding.enums.Update;
import com.bosch.coding.exceptions.InsufficientStockException;
import com.bosch.coding.exceptions.InvalidInventoryRequestException;
import com.bosch.coding.exceptions.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;

    public InventoryService(final InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public void processRequest(final ProducerRequest request) throws SQLException {
        final Update command = request.command();
        final String productName = request.productName();
        final int quantity = request.quantity();
        final InventoryItemType type = request.type();

        if (productName == null || productName.isEmpty()) {
            logger.error("Invalid Product Name: {}", productName);
            throw new InvalidInventoryRequestException("Invalid product name");
        }

        try {
            switch (command) {
                case ADD -> handleAddCommand(productName, type, quantity);
                case REMOVE -> handleRemoveCommand(productName, type, quantity);
                default -> throw new InvalidInventoryRequestException("Operation does not exist");
            }
        } catch (SQLException e) {
            logger.error("Database error occured: {}", e.getMessage());
            throw e;
        }
    }

    private void handleAddCommand(final String productName, final InventoryItemType type, final int quantity) throws SQLException {
        final Optional<InventoryDBResponse> currentStockOptional = inventoryRepository.getInventoryByProductName(productName);
        if (currentStockOptional.isEmpty()) {
            final InventoryDBRequest dbRequest = new InventoryDBRequest.Builder()
                    .withProductName(productName)
                    .withQuantity(quantity)
                    .withType(type)
                    .build();
            inventoryRepository.addInventory(dbRequest);
        } else {
            final InventoryDBResponse currentStock = currentStockOptional.get();

            final InventoryDBRequest dbRequest = new InventoryDBRequest.Builder()
                    .withProductName(productName)
                    .withQuantity(currentStock.quantity() + quantity)
                    .withType(type)
                    .withVersion(currentStock.version() + 1)
                    .build();
            inventoryRepository.updateInventory(dbRequest);
        }
    }

    private void handleRemoveCommand(final String productName, final InventoryItemType type, final int quantity) throws SQLException {
        final Optional<InventoryDBResponse> currentStockOptional = inventoryRepository.getInventoryByProductName(productName);
        if (currentStockOptional.isEmpty()) {
            throw new ProductNotFoundException("Cannot remove " + productName + " .Product does not exist");
        }

        final InventoryDBResponse currentStock = currentStockOptional.get();
        if (currentStock.quantity() - quantity < 0) {
            throw new InsufficientStockException("Not enough stock available for product = " + productName + " quantity needed = " + quantity);
        }

        final InventoryDBRequest dbRequest = new InventoryDBRequest.Builder()
                .withProductName(productName)
                .withQuantity(currentStock.quantity() - quantity)
                .withType(type)
                .withVersion(currentStock.version() + 1)
                .build();
        inventoryRepository.updateInventory(dbRequest);
    }
}

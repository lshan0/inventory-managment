package com.bosch.coding.repositories;

import com.bosch.coding.dto.InventoryDBRequest;
import com.bosch.coding.dto.InventoryDBResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    void addInventory(InventoryDBRequest request) throws SQLException;

    void updateInventory(InventoryDBRequest request) throws SQLException;

    Optional<InventoryDBResponse> getInventoryById(int id) throws SQLException;

    Optional<List<InventoryDBResponse>> getAllInventories() throws SQLException;

    Optional<InventoryDBResponse> getInventoryByProductName(String productName) throws SQLException;

    void deleteInventoryByProductName(String productName) throws SQLException;
}

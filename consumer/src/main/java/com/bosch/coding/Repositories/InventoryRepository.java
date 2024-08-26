package com.bosch.coding.Repositories;

import com.bosch.coding.dto.InventoryRequest;
import com.bosch.coding.dto.InventoryResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface InventoryRepository {

    void addInventory(InventoryRequest request) throws SQLException;

    void updateInventory(InventoryRequest request) throws SQLException;

    Optional<InventoryResponse> getInventoryById(int id) throws SQLException;

    Optional<List<InventoryResponse>> getAllInventories() throws SQLException;

    Optional<InventoryResponse> getInventoryByProductName(String productName) throws SQLException;

    void deleteInventoryByProductName(String productName) throws SQLException;
}

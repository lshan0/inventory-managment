package com.bosch.coding.repositories;

import com.bosch.coding.entity.InventoryDBRequest;
import com.bosch.coding.dto.InventoryDBResponse;
import com.bosch.coding.enums.InventoryItemType;
import com.bosch.coding.utils.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FruitInventoryRepository implements InventoryRepository {

    @Override
    public void addInventory(InventoryDBRequest inventoryRequest) throws SQLException {
        String sql = "INSERT INTO inventory (product_name, quantity, version) VALUES (?, ?, ?)";

        try (Connection connection = DBConnectionUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, inventoryRequest.getProductName());
            preparedStatement.setInt(2, inventoryRequest.getQuantity());
            preparedStatement.setInt(3, inventoryRequest.getVersion());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateInventory(InventoryDBRequest inventoryRequest) throws SQLException {
        String sql = "UPDATE inventory SET quantity = ?, version = ? WHERE product_name = ? AND version = ?";

        try (Connection connection = DBConnectionUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, inventoryRequest.getQuantity());
            preparedStatement.setInt(2, inventoryRequest.getVersion());
            preparedStatement.setString(3, inventoryRequest.getProductName());
            preparedStatement.setInt(4, inventoryRequest.getVersion() - 1);  // Check for the previous version

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update inventory: version mismatch.");
            }
        }
    }

    @Override
    public Optional<InventoryDBResponse> getInventoryById(int id) throws SQLException {
        String sql = "SELECT product_name, quantity, version FROM inventory WHERE id = ?";

        try (Connection connection = DBConnectionUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("product_name");
                    int quantity = resultSet.getInt("quantity");
                    int version = resultSet.getInt("version");
                    return Optional.of(new InventoryDBResponse(name, quantity, version));
                } else {
                    return Optional.empty(); // Record not found
                }
            }
        }
    }

    @Override
    public Optional<List<InventoryDBResponse>> getAllInventories() throws SQLException {
        String sql = "SELECT * FROM inventory";
        List<InventoryDBResponse> inventories = new ArrayList<>();

        try (Connection connection = DBConnectionUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                int version = resultSet.getInt("version");
                inventories.add(new InventoryDBResponse(name, quantity, version));
            }
            return Optional.of(inventories);
        }
    }

    @Override
    public Optional<InventoryDBResponse> getInventoryByProductName(String productName) throws SQLException {
        String sql = "SELECT product_name, quantity, version FROM inventory WHERE product_name = ?";

        try (Connection connection = DBConnectionUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, productName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("product_name");
                    int quantity = resultSet.getInt("quantity");
                    int version = resultSet.getInt("version");
                    return Optional.of(new InventoryDBResponse(name, quantity, version));
                } else {
                    return Optional.empty(); // Product not found
                }
            }
        }
    }

    @Override
    public void deleteInventoryByProductName(String productName) throws SQLException {
        String sql = "DELETE FROM inventory WHERE product_name = ?";

        try (Connection connection = DBConnectionUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, productName);
            preparedStatement.executeUpdate();
        }
    }
}

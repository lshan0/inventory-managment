package com.bosch.coding.Repositories;

import com.bosch.coding.dto.InventoryRequest;
import com.bosch.coding.dto.InventoryResponse;
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
    public void addInventory(InventoryRequest inventoryRequest) throws SQLException {
        String sql = "INSERT INTO inventory (product_name, quantity) VALUES (?, ?)";

        try (Connection connection = DBConnectionUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, inventoryRequest.productName());
            preparedStatement.setInt(2, inventoryRequest.quantity());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateInventory(InventoryRequest inventoryRequest) throws SQLException {
        String sql = "UPDATE inventory SET quantity = ? WHERE product_name = ?";

        try (Connection connection = DBConnectionUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, inventoryRequest.quantity());
            preparedStatement.setString(2, inventoryRequest.productName());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Optional<InventoryResponse> getInventoryById(int id) throws SQLException {
        String sql = "SELECT product_name, quantity FROM inventory WHERE id = ?";

        try (Connection connection = DBConnectionUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("product_name");
                    int quantity = resultSet.getInt("quantity");
                    return Optional.of(new InventoryResponse(name, quantity));
                } else {
                    return Optional.empty(); // Record not found
                }
            }
        }
    }

    @Override
    public Optional<List<InventoryResponse>> getAllInventories() throws SQLException {
        String sql = "SELECT product_name, quantity FROM inventory";
        List<InventoryResponse> inventories = new ArrayList<>();

        try (Connection connection = DBConnectionUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                inventories.add(new InventoryResponse(name, quantity));
            }
            return Optional.of(inventories);
        }
    }

    @Override
    public Optional<InventoryResponse> getInventoryByProductName(String productName) throws SQLException {
        String sql = "SELECT product_name, quantity FROM inventory WHERE product_name = ?";

        try (Connection connection = DBConnectionUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, productName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("product_name");
                    int quantity = resultSet.getInt("quantity");
                    return Optional.of(new InventoryResponse(name, quantity));
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

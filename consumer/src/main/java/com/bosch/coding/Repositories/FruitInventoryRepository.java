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

        try (Connection connection = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, inventoryRequest.productName());
            preparedStatement.setInt(2, inventoryRequest.quantity());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // Log the exception with details
            System.err.println("Failed to execute query: " + sql);
            e.printStackTrace(); // Print the stack trace for debugging
            throw e; // Rethrow exception to handle it in the service layer
        }
    }

    @Override
    public void updateInventory(InventoryRequest inventoryRequest) throws SQLException {
        String sql = "UPDATE inventory SET quantity = ? WHERE product_name = ?";

        try (Connection connection = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, inventoryRequest.quantity());
            preparedStatement.setString(2, inventoryRequest.productName());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                // Handle the case where no rows were updated (e.g., product not found)
                System.err.println("No inventory record found for product: " + inventoryRequest.productName());
            }

        } catch (SQLException e) {
            // Log the exception with details
            System.err.println("Failed to execute query: " + sql);
            e.printStackTrace(); // Print the stack trace for debugging
            throw e; // Rethrow exception to handle it in the service layer
        }
    }

    @Override
    public Optional<InventoryResponse> getInventoryById(int id) throws SQLException {
        String sql = "SELECT product_name, quantity FROM inventory WHERE id = ?";

        try (Connection connection = DBConnectionUtil.getInstance().getConnection();
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

        } catch (SQLException e) {
            // Log the exception with details
            System.err.println("Failed to execute query: " + sql);
            e.printStackTrace(); // Print the stack trace for debugging
            return Optional.empty(); // Return an empty Optional in case of an error
        }
    }

    @Override
    public Optional<List<InventoryResponse>> getAllInventories() throws SQLException {
        String sql = "SELECT product_name, quantity FROM inventory";
        List<InventoryResponse> inventories = new ArrayList<>();

        try (Connection connection = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");
                inventories.add(new InventoryResponse(name, quantity));
            }

            return Optional.of(inventories);

        } catch (SQLException e) {
            // Log the exception with details
            System.err.println("Failed to execute query: " + sql);
            e.printStackTrace(); // Print the stack trace for debugging
            return Optional.empty(); // Return an empty Optional in case of an error
        }
    }

    @Override
    public Optional<InventoryResponse> getInventoryByProductName(InventoryRequest request) throws SQLException {
        String sql = "SELECT product_name, quantity FROM inventory WHERE product_name = ?";

        try (Connection connection = DBConnectionUtil.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, request.productName());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("product_name");
                    int quantity = resultSet.getInt("quantity");
                    return Optional.of(new InventoryResponse(name, quantity));
                } else {
                    return Optional.empty(); // Product not found
                }
            }

        } catch (SQLException e) {
            // Log the exception with details
            System.err.println("Failed to execute query: " + sql);
            e.printStackTrace(); // Print the stack trace for debugging
            return Optional.empty();
        }
    }

    @Override
    public void deleteInventory(int id) throws SQLException {

    }
}

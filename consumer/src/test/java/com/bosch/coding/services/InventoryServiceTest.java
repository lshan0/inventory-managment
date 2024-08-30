package com.bosch.coding.services;

import com.bosch.coding.dto.ProducerRequest;
import com.bosch.coding.entity.InventoryDBRequest;
import com.bosch.coding.dto.InventoryDBResponse;
import com.bosch.coding.enums.InventoryItemType;
import com.bosch.coding.enums.Update;
import com.bosch.coding.exceptions.InsufficientStockException;
import com.bosch.coding.exceptions.InvalidInventoryRequestException;
import com.bosch.coding.exceptions.ProductNotFoundException;
import com.bosch.coding.repositories.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessRequestAddNewProduct() throws SQLException {
        ProducerRequest request = new ProducerRequest("Apples", InventoryItemType.FRUIT, 10, Update.ADD);
        when(inventoryRepository.getInventoryByProductName("Apples")).thenReturn(Optional.empty());

        inventoryService.processRequest(request);

        InventoryDBRequest dbRequest = new InventoryDBRequest.Builder()
                .withProductName("Apples")
                .withQuantity(10)
                .withType(InventoryItemType.FRUIT)
                .build();
        verify(inventoryRepository, times(1)).addInventory(dbRequest);
        verify(inventoryRepository, never()).updateInventory(any());
    }

    @Test
    public void testProcessRequestAddExistingProduct() throws SQLException {
        ProducerRequest request = new ProducerRequest("Apples", InventoryItemType.FRUIT, 10, Update.ADD);
        InventoryDBResponse existingResponse = new InventoryDBResponse("Apples", 5, 1); // Existing version is 1
        when(inventoryRepository.getInventoryByProductName("Apples")).thenReturn(Optional.of(existingResponse));

        inventoryService.processRequest(request);

        InventoryDBRequest updatedRequest = new InventoryDBRequest.Builder()
                .withProductName("Apples")
                .withQuantity(15)
                .withType(InventoryItemType.FRUIT)
                .withVersion(2)
                .build();
        verify(inventoryRepository, never()).addInventory(any());
        verify(inventoryRepository, times(1)).updateInventory(updatedRequest);
    }

    @Test
    public void testProcessRequestRemoveProductWithSufficientStock() throws SQLException {
        ProducerRequest request = new ProducerRequest("Oranges", InventoryItemType.FRUIT, 5, Update.REMOVE);
        InventoryDBResponse existingResponse = new InventoryDBResponse("Oranges", 10, 1);
        when(inventoryRepository.getInventoryByProductName("Oranges")).thenReturn(Optional.of(existingResponse));

        inventoryService.processRequest(request);

        InventoryDBRequest updatedRequest = new InventoryDBRequest.Builder()
                .withProductName("Oranges")
                .withQuantity(5)
                .withType(InventoryItemType.FRUIT)
                .withVersion(2)
                .build();
        verify(inventoryRepository, times(1)).updateInventory(updatedRequest);
    }

    @Test
    public void testProcessRequestRemoveProductWithInsufficientStock() throws SQLException {
        ProducerRequest request = new ProducerRequest("Bananas", InventoryItemType.FRUIT, 10, Update.REMOVE);
        InventoryDBResponse existingResponse = new InventoryDBResponse("Bananas", 5, 1);
        when(inventoryRepository.getInventoryByProductName("Bananas")).thenReturn(Optional.of(existingResponse));

        InsufficientStockException exception = assertThrows(InsufficientStockException.class, () -> {
            inventoryService.processRequest(request);
        });
        assertEquals("Not enough stock available for product = Bananas quantity needed = 10", exception.getMessage());
    }

    @Test
    public void testProcessRequestRemoveNonExistingProduct() throws SQLException {
        ProducerRequest request = new ProducerRequest("Grapes", InventoryItemType.FRUIT, 5, Update.REMOVE);
        when(inventoryRepository.getInventoryByProductName("Grapes")).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            inventoryService.processRequest(request);
        });
        assertEquals("Cannot remove Grapes .Product does not exist", exception.getMessage());
    }

    @Test
    public void testProcessRequestInvalidProductName() {
        ProducerRequest request = new ProducerRequest("", InventoryItemType.FRUIT, 10, Update.ADD);

        InvalidInventoryRequestException exception = assertThrows(InvalidInventoryRequestException.class, () -> {
            inventoryService.processRequest(request);
        });
        assertEquals("Invalid product name", exception.getMessage());
    }

    @Test
    public void testProcessRequestDatabaseError() throws SQLException {
        ProducerRequest request = new ProducerRequest("Apples", InventoryItemType.FRUIT, 10, Update.ADD);
        when(inventoryRepository.getInventoryByProductName("Apples")).thenThrow(SQLException.class);

        SQLException exception = assertThrows(SQLException.class, () -> {
            inventoryService.processRequest(request);
        });
        assertNotNull(exception);
    }
}

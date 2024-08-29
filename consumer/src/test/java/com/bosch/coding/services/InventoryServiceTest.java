//package com.bosch.coding.services;
//
//import com.bosch.coding.dto.InventoryRequest;
//import com.bosch.coding.dto.InventoryResponse;
//import com.bosch.coding.enums.Update;
//import com.bosch.coding.exceptions.InsufficientStockException;
//import com.bosch.coding.exceptions.InvalidInventoryRequestException;
//import com.bosch.coding.exceptions.ProductNotFoundException;
//import com.bosch.coding.repositories.InventoryRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.sql.SQLException;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class InventoryServiceTest {
//
//    @Mock
//    private InventoryRepository inventoryRepository;
//
//    @InjectMocks
//    private InventoryService inventoryService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testProcessRequestAddNewProduct() throws SQLException {
//        InventoryRequest request = new InventoryRequest("Apples", 10, Update.ADD);
//        when(inventoryRepository.getInventoryByProductName("Apples")).thenReturn(Optional.empty());
//
//        inventoryService.processRequest(request);
//
//        verify(inventoryRepository, times(1)).addInventory(request);
//        verify(inventoryRepository, never()).updateInventory(any());
//    }
//
//    @Test
//    public void testProcessRequestAddExistingProduct() throws SQLException {
//        InventoryRequest request = new InventoryRequest("Apples", 10, Update.ADD);
//        InventoryResponse existingResponse = new InventoryResponse("Apples", 5);
//        when(inventoryRepository.getInventoryByProductName("Apples")).thenReturn(Optional.of(existingResponse));
//
//        inventoryService.processRequest(request);
//
//        InventoryRequest updatedRequest = new InventoryRequest("Apples", 15, Update.ADD);
//        verify(inventoryRepository, never()).addInventory(any());
//        verify(inventoryRepository, times(1)).updateInventory(updatedRequest);
//    }
//
//    @Test
//    public void testProcessRequestRemoveProductWithSufficientStock() throws SQLException {
//        InventoryRequest request = new InventoryRequest("Oranges", 5, Update.REMOVE);
//        InventoryResponse existingResponse = new InventoryResponse("Oranges", 10);
//        when(inventoryRepository.getInventoryByProductName("Oranges")).thenReturn(Optional.of(existingResponse));
//
//        inventoryService.processRequest(request);
//
//        InventoryRequest updatedRequest = new InventoryRequest("Oranges", 5, Update.REMOVE);
//        verify(inventoryRepository, times(1)).updateInventory(updatedRequest);
//    }
//
//    @Test
//    public void testProcessRequestRemoveProductWithInsufficientStock() throws SQLException {
//        InventoryRequest request = new InventoryRequest("Bananas", 10, Update.REMOVE);
//        InventoryResponse existingResponse = new InventoryResponse("Bananas", 5);
//        when(inventoryRepository.getInventoryByProductName("Bananas")).thenReturn(Optional.of(existingResponse));
//
//        InsufficientStockException exception = assertThrows(InsufficientStockException.class, () -> {
//            inventoryService.processRequest(request);
//        });
//        assertEquals("Not enough stock available for product = Bananas quantity needed = 10", exception.getMessage());
//    }
//
//    @Test
//    public void testProcessRequestRemoveNonExistingProduct() throws SQLException {
//        InventoryRequest request = new InventoryRequest("Grapes", 5, Update.REMOVE);
//        when(inventoryRepository.getInventoryByProductName("Grapes")).thenReturn(Optional.empty());
//
//        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
//            inventoryService.processRequest(request);
//        });
//        assertEquals("Cannot remove Grapes .Product does not exist", exception.getMessage());
//    }
//
//    @Test
//    public void testProcessRequestInvalidProductName() {
//        InventoryRequest request = new InventoryRequest("", 10, Update.ADD);
//
//        InvalidInventoryRequestException exception = assertThrows(InvalidInventoryRequestException.class, () -> {
//            inventoryService.processRequest(request);
//        });
//        assertEquals("Invalid product name", exception.getMessage());
//    }
//
//    @Test
//    public void testProcessRequestDatabaseError() throws SQLException {
//        InventoryRequest request = new InventoryRequest("Apples", 10, Update.ADD);
//        when(inventoryRepository.getInventoryByProductName("Apples")).thenThrow(SQLException.class);
//
//        SQLException exception = assertThrows(SQLException.class, () -> {
//            inventoryService.processRequest(request);
//        });
//        assertNotNull(exception);
//    }
//}
//

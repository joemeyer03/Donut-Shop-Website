package com.estore.api.estoreapi;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.InventoryFileDAO;

@Tag("Persistence-tier")
public class InventoryFileDAOTest {
    InventoryFileDAO inventoryFileDAO;
    Product[] testProducts;
    ObjectMapper mockObjectMapper;
    
    @BeforeEach
    public void setupProductFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testProducts = new Product[3];
        testProducts[0] = new Product(1.5, "vanilla", 0, null);
        testProducts[1] = new Product(1.75, "chocolate", 2, null);
        testProducts[2] = new Product(2, "boston-cream", 1, null);

        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"),Product[].class))
            .thenReturn(testProducts);

        inventoryFileDAO = new InventoryFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetProducts() throws IOException {
        // invoke
        Product [] actualArray = inventoryFileDAO.getInventory();
        
        // test
        assertArrayEquals(testProducts, actualArray);
    }

    @Test
    public void testFindProducts() throws IOException {
        // setup
        Product [] vanillaExpectedArray = {testProducts[0]};
        Product [] emptyExpectedArray = {};

        // invoke
        Product [] vanillaActualArray = inventoryFileDAO.findProduct("vanilla");
        Product [] emptyActualArray = inventoryFileDAO.findProduct("cake");

        // test
        assertArrayEquals(vanillaExpectedArray, vanillaActualArray);
        assertArrayEquals(emptyExpectedArray, emptyActualArray);
    }

    @Test
    public void testGetProduct() throws IOException {
        // setup
        Product expectedVanilla = testProducts[0];
        Product expectedChocolate = testProducts[1];

        // invoke
        Product actualVanilla = inventoryFileDAO.getProduct("vanilla");
        Product actualChocolate = inventoryFileDAO.getProduct("chocolate");

        // test
        assertEquals(expectedVanilla, actualVanilla);
        assertEquals(expectedChocolate, actualChocolate);
    }

    @Test
    public void testDeleteProducts() throws IOException {
        boolean result = assertDoesNotThrow(()->inventoryFileDAO.deleteProduct("vanilla"), 
            "Unexpected exception thrown");
        assertEquals(result, true);

        assertEquals(inventoryFileDAO.getInventory().length, testProducts.length - 1);
    }

    @Test
    public void testCreateProduct() throws IOException {
        // setup 
        Product product = new Product(1.5, "glazed", 1, null);

        // invoke
        Product result = assertDoesNotThrow(()->inventoryFileDAO.createProduct(product), 
            "Unexpected exception thrown");
        
        // analyze
        assertNotNull(result);
        Product actual = inventoryFileDAO.getProduct("glazed");
        assertEquals(result, actual);
    }

    @Test
    public void testUpdateProduct() throws IOException {
        // setup 
        Product product = new Product(1.5, "vanilla", 2, null);

        // invoke
        Product result = assertDoesNotThrow(()->inventoryFileDAO.updateProduct(product.getName(), product), 
            "Unexpected exception thrown");
        
        // analyze
        assertNotNull(result);
        Product actual = inventoryFileDAO.getProduct("vanilla");
        assertEquals(result, actual);
    }

    @Test
    public void testSaveException() throws IOException {
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Product[].class));

        Product product = new Product(1.5, "glazed", 2, null);

        assertThrows(IOException.class,
                        () -> inventoryFileDAO.createProduct(product),
                        "IOException not thrown");
    }

    @Test
    public void testGetProductNotFound() throws IOException {
        // Invoke
        Product product = inventoryFileDAO.getProduct("glazed");

        // Analyze
        assertEquals(product,null);
    }

    @Test
    public void testDeleteProductNotFound() throws IOException {
        // Invoke
        boolean result = assertDoesNotThrow(() -> inventoryFileDAO.deleteProduct("glazed"),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(inventoryFileDAO.getInventory().length,testProducts.length);
    }

    @Test
    public void testUpdateProductNotFound() {
        // Setup
        Product product = new Product(1.5, "glazed", 25, null);

        // Invoke
        Product result = assertDoesNotThrow(() -> inventoryFileDAO.updateProduct(product.getName(), product),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testRemoveProductSpecific() throws IOException {
        // Setup
        Product product = new Product(2, "boston-cream", 1, null);
        Product product2 = new Product(2, "chocolate", 2, null);

        // Invoke
        assertDoesNotThrow(() -> inventoryFileDAO.removeProduct(product, 2), "Unexpected exception thrown");
        assertDoesNotThrow(() -> inventoryFileDAO.removeProduct(product2, 1), "Unexpected exception thrown");
        
        assertEquals(0, inventoryFileDAO.getProduct("boston-cream").getQuantity());
        assertEquals(1, inventoryFileDAO.getProduct("chocolate").getQuantity());
    }

    @Test
    public void testRemoveProductAll() throws IOException {
        // Setup
        Product product = new Product(2, "boston-cream", 1, null);

        // Invoke
        assertDoesNotThrow(() -> inventoryFileDAO.removeProduct(product), "Unexpected exception thrown");
        
        assertEquals(0, inventoryFileDAO.getProduct("boston-cream").getQuantity());
    }

    @Test
    public void testGetProductQuantity() throws IOException {
        // Setup
        Product product = new Product(2, "boston-cream", 1, null);

        // Invoke
        assertDoesNotThrow(() -> inventoryFileDAO.getProductQuantity(product), "Unexpected exception thrown");
        
        assertEquals(1, inventoryFileDAO.getProduct("boston-cream").getQuantity());
    }

    @Test
    public void testContainsProductWithAmount() throws IOException {
        // Setup
        Product product = new Product(1.5, "vanilla", 1, null);
        
        // Invoke
        assertDoesNotThrow(() -> inventoryFileDAO.containsProductWithAmt(product), "Unexpected exception thrown");
        
        assertFalse(inventoryFileDAO.containsProductWithAmt(product));
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Product[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new InventoryFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}

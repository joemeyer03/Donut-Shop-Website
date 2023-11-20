package com.estore.api.estoreapi;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.controller.ProductController;
import com.estore.api.estoreapi.persistence.InventoryFileDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

/*
 * @author Jason Berry, Qadira Moore
 */

@Tag("Controller-Tier")
class EstoreApiApplicationTests {

	ProductController controller;
	InventoryFileDAO mockDao;

	@BeforeEach
	public void setupProductController() {
		mockDao = mock(InventoryFileDAO.class);
		controller = new ProductController(mockDao);
	}

	@Test
	public void testCreateProduct() throws IOException {
		Product realProduct = new Product(1.5, "Real_Product", 1, null);
		Product unrealProduct = new Product(1.5, "Unreal_Product", 1, null);

		when(mockDao.createProduct(realProduct)).thenReturn(realProduct);
		when(mockDao.createProduct(unrealProduct)).thenReturn(unrealProduct);

		ResponseEntity<Product> firstResponse = controller.createProduct(realProduct);
		ResponseEntity<Product> secondResponse = controller.createProduct(unrealProduct);

		assertEquals(HttpStatus.CREATED, firstResponse.getStatusCode());
		assertEquals(realProduct, firstResponse.getBody());

		assertEquals(HttpStatus.CREATED, secondResponse.getStatusCode());
		assertEquals(unrealProduct, secondResponse.getBody());
	}

	@Test
    public void testCreateProductHandleException() throws IOException {  // createProduct may throw IOException
        // Setup
        Product product = new Product(1.5,"product", 1, null);

        // When createProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).createProduct(product);

        // Invoke
        ResponseEntity<Product> response = controller.createProduct(product);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

	@Test
	public void testGetProduct() throws IOException {

		Product realProduct = new Product(1.5, "Real_Product", 1, null);

		when(mockDao.getProduct(realProduct.getName())).thenReturn(realProduct);

		ResponseEntity<Product> secondResponse = controller.getProduct("Real_Product");

		assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
		assertEquals("Real_Product", secondResponse.getBody().getName());
	}

	@Test
    public void testGetProductNotFound() throws Exception { // createProduct may throw IOException
        // Setup
        String productName = "donut";
        // When the same id is passed in, our mock Product DAO will return null, simulating
        // no product found
        when(mockDao.getProduct("donut")).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = controller.getProduct(productName);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

	@Test
    public void testGetProductHandleException() throws Exception { // createProduct may throw IOException
        // Setup
        String productName = "donut";
        // When getProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).getProduct(productName);

        // Invoke
        ResponseEntity<Product> response = controller.getProduct(productName);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
	
	@Test
	public void testGetProducts() throws IOException {

		Product[] products = new Product[2];

		products[0] = new Product(1.5, "Real_Product", 1, null);
		products[1] = new Product(2.0, "Unreal_Product", 2, null);

		when(mockDao.getInventory()).thenReturn(products);

		ResponseEntity<Product[]> response = controller.getProducts();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(products, response.getBody());
	}

	@Test
    public void testGetProductsHandleFirstException() throws IOException { // getProducts may throw IOException
		// setup
		when(mockDao.getInventory()).thenReturn(null);
		
		// Invoke
        ResponseEntity<Product[]> newResponse = controller.getProducts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,newResponse.getStatusCode());
    }

	@Test
    public void testGetProductsHandleSecondException() throws IOException { // getProducts may throw IOException
        // Setup
        // When getProducts is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).getInventory();

        // Invoke
        ResponseEntity<Product[]> response = controller.getProducts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

	@Test
	public void testFindProduct() throws IOException{
		Product[] product = new Product[1];
		product[0] = new Product(1.5, "Real_Product", 1, null);

		when(mockDao.findProduct("Real_Product")).thenReturn(product);

		ResponseEntity<Product[]> response = controller.findProduct("Real_Product");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(product, response.getBody());
	}

	@Test
    public void testSearchProductsHandleException() throws IOException { // findProducts may throw IOException
        // Setup
        String searchString = "an";
        // When createHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).findProduct(searchString);

        // Invoke
        ResponseEntity<Product[]> response = controller.findProduct(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

	@Test
	public void testUpdateProduct() throws IOException {
		Product update = new Product(3, "Real_Product", 2, null);

		when(mockDao.updateProduct(update.getName(), update)).thenReturn(update);

		ResponseEntity<Product> response = controller.updateProduct(update.getName(), update);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(update, response.getBody());
	}

	@Test
    public void testUpdateProductFailed() throws IOException { // updateProduct may throw IOException
        // Setup
        Product product = new Product(1, "donut", 1, null);
        // when updateProduct is called, return true simulating successful
        // update and save
        when(mockDao.updateProduct(product.getName(), product)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = controller.updateProduct(product.getName(), product);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateProductHandleException() throws IOException { // updateProduct may throw IOException
        // Setup
        Product product = new Product(1, "donut", 1, null);
        // When updateHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).updateProduct(product.getName(), product);

        // Invoke
        ResponseEntity<Product> response = controller.updateProduct(product.getName(), product);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

	@Test
	public void testDeleteProduct() throws IOException {

		when(mockDao.deleteProduct("Unreal_Product")).thenReturn(true);

		ResponseEntity<Product> response = controller.deleteProduct("Unreal_Product");

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
    public void testDeleteHeroNotFound() throws IOException { // deleteProduct may throw IOException
        // Setup
        String productName = "donut";
        // when deleteProduct is called return false, simulating failed deletion
        when(mockDao.deleteProduct(productName)).thenReturn(false);

        // Invoke
        ResponseEntity<Product> response = controller.deleteProduct(productName);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

	@Test
    public void testDeleteProductHandleException() throws IOException { // deleteProduct may throw IOException
        // Setup
        String productName = "donut";
        // When deleteProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).deleteProduct(productName);

        // Invoke
        ResponseEntity<Product> response = controller.deleteProduct(productName);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}

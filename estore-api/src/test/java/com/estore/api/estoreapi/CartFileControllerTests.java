package com.estore.api.estoreapi;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.controller.CartController;
import com.estore.api.estoreapi.persistence.CartFileDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

/*
 * @author Joe Meyer
 */

@Tag("Controller-Tier")
class CartFileControllerTests {
    CartController controller;
	CartFileDAO mockDao;

    @BeforeEach
	public void setupCartController() {
		mockDao = mock(CartFileDAO.class);
		controller = new CartController(mockDao);
	}

    @Test
	public void testCreateUser() throws IOException {
		String realUser = "user1";
        Product[] realCart = {};
		String unrealUser = "user2";
        Product[] unrealCart = {new Product(0, "donut", 0, null)};

		when(mockDao.createUserCart(realUser)).thenReturn(realCart);
		when(mockDao.createUserCart(unrealUser)).thenReturn(unrealCart);

		ResponseEntity<Product[]> firstResponse = controller.createUserCart(realUser);
		ResponseEntity<Product[]> secondResponse = controller.createUserCart(unrealUser);

		assertEquals(HttpStatus.CREATED, firstResponse.getStatusCode());
		assertEquals(realCart, firstResponse.getBody());

		assertEquals(HttpStatus.CREATED, secondResponse.getStatusCode());
		assertEquals(unrealCart, secondResponse.getBody());
	}

    @Test
    public void testCreateUserHandleException() throws IOException {  // createProduct may throw IOException
        // Setup
        String realUser = "user1";

        // When createProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).createUserCart(realUser);

        // Invoke
        ResponseEntity<Product[]> response = controller.createUserCart(realUser);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetUserNotFound() throws Exception { // createuser may throw IOException
        // Setup
        String user = "user";
        // When the same id is passed in, our mock Cart DAO will return null, simulating
        // no product found
        when(mockDao.getUserCart("user")).thenReturn(null);

        // Invoke
        ResponseEntity<Product[]> response = controller.getUserCart("user");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetUserHandleException() throws Exception { // createuser may throw IOException
        // Setup
        String user = "user";
        // When getUser is called on the Mock Cart DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).getUserCart(user);

        // Invoke
        ResponseEntity<Product[]> response = controller.getUserCart(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
	public void testUpdateCart() throws IOException {
        String user = "user";
		Product[] update = {new Product(0, "donut", 0, null)};

		when(mockDao.updateUserCart(user, update)).thenReturn(update);

		ResponseEntity<Product[]> response = controller.updateUserCart(user, update);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(update, response.getBody());
	}

    @Test
    public void testUpdateCartFailed() throws IOException { // updateProduct may throw IOException
        // Setup
        String user = "user";
		Product[] update = {new Product(0, "donut", 0, null)};
        // when updateProduct is called, return true simulating successful
        // update and save
        when(mockDao.updateUserCart(user, update)).thenReturn(null);

        // Invoke
        ResponseEntity<Product[]> response = controller.updateUserCart(user, update);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateCartHandleException() throws IOException { // updateProduct may throw IOException
        // Setup
        String user = "user";
		Product[] update = {new Product(0, "donut", 0, null)};
        // When updateHero is called on the Mock Hero DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).updateUserCart(user, update);

        // Invoke
        ResponseEntity<Product[]> response = controller.updateUserCart(user, update);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
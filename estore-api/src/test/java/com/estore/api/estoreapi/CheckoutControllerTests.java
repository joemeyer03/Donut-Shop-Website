package com.estore.api.estoreapi;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.controller.CheckoutController;
import com.estore.api.estoreapi.persistence.CheckoutFileDAO;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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
class CheckoutControllerTests {

	CheckoutController controller;
	CheckoutFileDAO mockDao;

	@BeforeEach
	public void setupCheckoutController() {
		mockDao = mock(CheckoutFileDAO.class);
		controller = new CheckoutController(mockDao);
	}

    @Test
	public void testGetUserCheckouts() throws IOException {

		Product[][] checkouts = new Product[2][1];
        String user = "user";

        checkouts[0][0] = new Product(1.5, "Real_Product", 1, null);
        checkouts[1][0] = new Product(2.0, "Unreal_Product", 2, null);

		when(mockDao.getUserCheckouts(user)).thenReturn(checkouts);

		ResponseEntity<Product[][]> response = controller.getUserCheckouts(user);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertArrayEquals(checkouts, response.getBody());
	}

    @Test
    public void testGetUserCheckoutsHandleException() throws Exception { // createuser may throw IOException
        // Setup
        String user = "user";
        // When getUser is called on the Mock Cart DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).getUserCheckouts(user);

        // Invoke
        ResponseEntity<Product[][]> response = controller.getUserCheckouts(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
	public void testAddUserCheckout() throws IOException {
        String user = "user";
		Product[] update = {new Product(0, "donut", 0, null)};
        Product[][] updated = new Product[1][1];
        updated[0] = update;

		when(mockDao.addUserCheckout(user, update)).thenReturn(updated);

		ResponseEntity<Product[][]> response = controller.addUserCheckout(user, update);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertArrayEquals(updated, response.getBody());
	}

    @Test
    public void testAddUserCheckoutNotFound() throws Exception { // createuser may throw IOException
        // Setup
        Product[] test = {new Product(1, "donut", 1, null)};
        // When the same id is passed in, our mock Cart DAO will return null, simulating
        // no product found
        when(mockDao.addUserCheckout("user", test)).thenReturn(null);

        // Invoke
        ResponseEntity<Product[][]> response = controller.addUserCheckout("user", test);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testAddUserCheckoutHandleException() throws Exception { // createuser may throw IOException
        // Setup
        String user = "user";
        // When getUser is called on the Mock Cart DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).addUserCheckout(user, null);

        // Invoke
        ResponseEntity<Product[][]> response = controller.addUserCheckout(user, null);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}
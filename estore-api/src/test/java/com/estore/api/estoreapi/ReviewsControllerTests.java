package com.estore.api.estoreapi;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Reviews;
import com.estore.api.estoreapi.controller.ReviewsController;
import com.estore.api.estoreapi.persistence.InventoryFileDAO;
import com.estore.api.estoreapi.persistence.ReviewsDAO;

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
class ReviewsControllerTests {

	ReviewsController controller;
	ReviewsDAO mockDao;

	@BeforeEach
	public void setupReviewController() {
		mockDao = mock(ReviewsDAO.class);
		controller = new ReviewsController(mockDao);
	}

	@Test
	public void testGetReviews() throws IOException {

		Reviews realReviews = new Reviews("donut", null);

		when(mockDao.getReviews(realReviews.getName())).thenReturn(realReviews);

		ResponseEntity<Reviews> secondResponse = controller.getReviews("donut");

		assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
		assertEquals("donut", secondResponse.getBody().getName());
	}

    @Test
    public void testGetReviewsNotFound() throws Exception { // createProduct may throw IOException
        // Setup
        String reviewName = "donut";
        // When the same id is passed in, our mock Product DAO will return null, simulating
        // no product found
        when(mockDao.getReviews("donut")).thenReturn(null);

        // Invoke
        ResponseEntity<Reviews> response = controller.getReviews(reviewName);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("donut", response.getBody().getName());
    }

    @Test
    public void testGetReviewsHandleException() throws Exception { // createProduct may throw IOException
        // Setup
        String reviewsName = "donut";
        // When getProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).getReviews(reviewsName);

        // Invoke
        ResponseEntity<Reviews> response = controller.getReviews(reviewsName);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
	public void testGetAllReviews() throws IOException {

		Reviews[] reviews = new Reviews[2];

		reviews[0] = new Reviews("real_donut", null);
		reviews[1] = new Reviews("unreal_donut", null);

		when(mockDao.getAllReviews()).thenReturn(reviews);

		ResponseEntity<Reviews[]> response = controller.getAllReviews();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(reviews, response.getBody());
	}

	@Test
    public void testGetProductsHandleFirstException() throws IOException { // getProducts may throw IOException
		// setup
		when(mockDao.getAllReviews()).thenReturn(null);
		
		// Invoke
        ResponseEntity<Reviews[]> newResponse = controller.getAllReviews();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,newResponse.getStatusCode());
    }

	@Test
    public void testGetProductsHandleSecondException() throws IOException { // getProducts may throw IOException
        // Setup
        // When getProducts is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockDao).getAllReviews();

        // Invoke
        ResponseEntity<Reviews[]> response = controller.getAllReviews();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }
}

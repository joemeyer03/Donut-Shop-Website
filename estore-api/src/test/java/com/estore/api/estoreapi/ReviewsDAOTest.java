package com.estore.api.estoreapi;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.estore.api.estoreapi.model.Reviews;
import com.estore.api.estoreapi.persistence.InventoryFileDAO;
import com.estore.api.estoreapi.persistence.ReviewsDAO;

@Tag("Persistence-tier")
public class ReviewsDAOTest {
    ReviewsDAO reviewsDAO;
    Reviews[] testReview;
    ObjectMapper mockObjectMapper;
    
    @BeforeEach
    public void setupReviewsDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testReview = new Reviews[3];
        String[] reviewArray1 = {"good", "bad"};
        String[] reviewArray2 = {"great", "bad"};
        String[] reviewArray3 = {"awesome", "bad"};
        testReview[0] = new Reviews("glazed", reviewArray1);
        testReview[1] = new Reviews("old fashion", reviewArray2);
        testReview[2] = new Reviews("vanilla", reviewArray3);

        when(mockObjectMapper.readValue(new File("doesnt_matter.txt"),Reviews[].class))
            .thenReturn(testReview);

        reviewsDAO = new ReviewsDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetAllReviews() throws IOException {
        // invoke
        Reviews [] actualArray = reviewsDAO.getAllReviews();
        
        // test
        assertArrayEquals(testReview, actualArray);
    }

    @Test
    public void testGetReview() throws IOException {
        // setup
        Reviews expected1 = testReview[0];
        Reviews expected2 = testReview[1];

        // invoke
        Reviews actual1 = reviewsDAO.getReviews("glazed");
        Reviews actual2 = reviewsDAO.getReviews("old fashion");

        // test
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testCreateReviews() throws IOException {
        // setup 
        String[] reviewArray = {"awesome", "bad"};
        Reviews reviews = new Reviews("chocolate", reviewArray);

        // invoke
        Reviews result = assertDoesNotThrow(()->reviewsDAO.createReviews(reviews), 
            "Unexpected exception thrown");
        
        // analyze
        assertNotNull(result);
        Reviews actual = reviewsDAO.getReviews("chocolate");
        assertEquals(result, actual);
    }

    @Test
    public void testAddReviews() throws IOException {
        // setup 
        String[] reviewArray = {"awesome", "bad", "ok"};
        Reviews review = new Reviews("glazed", reviewArray);

        // invoke
        Reviews result = assertDoesNotThrow(()->reviewsDAO.addReviews(review), 
            "Unexpected exception thrown");
        
        // analyze
        assertNotNull(result);
        Reviews actual = reviewsDAO.getReviews("glazed");
        assertEquals(result, actual);
    }

    @Test
    public void testSaveException() throws IOException {
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Reviews[].class));

        String[] reviewArray = {"awesome", "bad"};
        Reviews review = new Reviews("lemon", reviewArray);

        assertThrows(IOException.class,
                        () -> reviewsDAO.createReviews(review),
                        "IOException not thrown");
    }

    @Test
    public void testGetProductNotFound() throws IOException {
        // Invoke
        Reviews review = reviewsDAO.getReviews("test");

        // Analyze
        assertEquals(review,null);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Reviews[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new ReviewsDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}

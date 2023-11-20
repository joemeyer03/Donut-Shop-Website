package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Reviews;

@Tag("Model-tier")
public class ReviewsTest {
   
    @Test
    public void testConstructor() {
        String[] reviewArray = {"good", "bad"};
        Reviews r = new Reviews("donut", reviewArray);
        assertEquals("donut", r.getName());
        assertArrayEquals(reviewArray, r.getReviews());
    }

    @Test
    public void testName() {
        // setup
        String name = "donut";
        String newName = "notDonut";
        String[] reviewArray = {"good", "bad"};
        Reviews r = new Reviews(name, reviewArray);

        // invoke get
        String actualName = r.getName();

        // test get
        assertEquals(name, actualName);

        // invoke set
        r.setName(newName);
        String actualNewName = r.getName();

        // test set
        assertEquals(newName, actualNewName);
    }

    @Test
    public void testReviews() {
        // setup
        String name = "donut";
        String[] reviewArray = {"good", "bad"};
        String[] newReviewArray = {"good", "bad", "ok"};
        Reviews r = new Reviews(name,reviewArray);

        // invoke get
        String[] actualReview = r.getReviews();

        // test get
        assertEquals(reviewArray, actualReview);

        // invoke set
        r.setReviews(newReviewArray);
        String[] actualNewReviews = r.getReviews();

        // test set
        assertArrayEquals(newReviewArray, actualNewReviews);
    }

    @Test
    public void testToString() {
        // setup
        String name = "donut";
        String[] reviewArray = {"good", "bad"};
        Reviews r = new Reviews(name, reviewArray);
        StringBuffer reviewsBuffer = new StringBuffer();
        for (int i = 1; i < r.getReviews().length; i++) {
            reviewsBuffer.append(r.getReviews()[i] + "\n");
        } 
        String reviewsString = reviewsBuffer.toString();
        String exepectedString = String.format("Product [name=%s, reviews=%s]",name,reviewsString);
        

        // invoke
        String actualString = r.toString();

        // test
        assertEquals(exepectedString, actualString);
        
    }
}

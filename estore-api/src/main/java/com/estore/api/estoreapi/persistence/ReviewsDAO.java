package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Reviews;

/**
 * Implements the functionality for JSON file-based peristance for Donut shop
 * 
 * @author JoesOsTeam
 */
@Component
public class ReviewsDAO {
    private static final Logger LOG = Logger.getLogger(ReviewsDAO.class.getName());
    // local cache of all the products. takes in the product name and gives out the reviews object
    Map<String,Reviews> reviews;
    // converts between product objects and JSON text format
    private ObjectMapper objectMapper; 
    // name to read and write to
    private String filename;

    /**
     * Constructor for reviews DAO
     * @param filename
     * @param objectMapper
     * @throws IOException
     */
    public ReviewsDAO(@Value("${reviews.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the reviews from the file
    }

    /**
     * Returns an array of all reviews for all products
     * @return array of all reviews
     * @throws IOException when file cannot be accessed or written to
     */
    public Reviews[] getAllReviews() throws IOException {
        ArrayList<Reviews> reviewsArrayList = new ArrayList<>();

        for (Reviews review : reviews.values()) {
            reviewsArrayList.add(review);
        }
        Reviews[] reviewsArray = new Reviews[reviewsArrayList.size()];
        reviewsArrayList.toArray(reviewsArray);
        return reviewsArray;
    }

    /**
     * Create a list of reviews for a product
     * @param review    the review to add
     * @return  the reviews object
     * @throws IOException
     */
    public Reviews createReviews(Reviews review) throws IOException {
        synchronized(reviews) {
            // We create a new product object
            Reviews newReviews = new Reviews(review.getName(), review.getReviews());
            reviews.put(newReviews.getName(),newReviews);
            save(); // may throw an IOException
            return newReviews;
        }
    }

    /**
     * Adds a review to the list 
     * @param review  the review to be added to
     * @return  returns the updated review object
     * @throws IOException
     */
    public Reviews addReviews(Reviews review) throws IOException {
        synchronized(reviews) {
            if (reviews.containsKey(review.getName()) == false)
                return null;  // product does not exist
            reviews.put(review.getName(),review);
            save(); // may throw an IOException
            return review;
        }
    }

    /**
     * Gets the list of reviews for a product
     * @param name  the name of a product
     * @return  the reviews object for that product
     * @throws IOException
     */
    public Reviews getReviews(String name) throws IOException {
        Reviews review = reviews.get(name);
        return review;
    }
    
    /**
     * Saves the products from the map into the file
     * @return true if written successfully
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Reviews[] ReviewsArray = getAllReviews();   // Gets all products

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),ReviewsArray);
        return true;
    }


    /**
     * Loads the products from the JSON file into the map
     * @return true if the file was read successfully
     * @throws IOException when file cannot be accessed
     */
    private boolean load() throws IOException {
        reviews = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of heroes
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Reviews[] ReviewsArray = objectMapper.readValue(new File(filename),Reviews[].class);

        // Add each hero to the tree map 
        for (Reviews review : ReviewsArray) {
            reviews.put(review.getName(),review);
        }
        return true;
    }
}
package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.estore.api.estoreapi.persistence.ReviewsDAO;

import com.estore.api.estoreapi.model.Reviews;

/**
 * Handles the REST API requests for the Reviews
 * @author Joe'sO'sTeam
 */

@RestController
@RequestMapping("reviews")
public class ReviewsController {
    private static final Logger LOG = Logger.getLogger(ReviewsController.class.getName());
    private ReviewsDAO reviewDao;

    /**
     * Creates a REST API controller
     * @param reviewDao The Object to perform CRUD operations
     */
    public ReviewsController(ReviewsDAO reviewDao) {
        this.reviewDao = reviewDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Reviews reviews} for the given name
     * 
     * @param name The name used to locate the {@link Reviews reviews}
     * 
     * @return ResponseEntity with reviews object and HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{name}")
    public ResponseEntity<Reviews> getReviews(@PathVariable String name) {
        LOG.info("GET /reviews/" + name);
        try {
            Reviews reviews = reviewDao.getReviews(name);
            Reviews newReview;
            if (reviews != null)
                return new ResponseEntity<Reviews>(reviews,HttpStatus.OK);
            else
                newReview = new Reviews(name,new String[0]);
                return new ResponseEntity<Reviews>(newReview, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Review review}
     * 
     * @return ResponseEntity with array of {@link Review review} objects (may be empty) and
     * HTTP status of OK
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Reviews[]> getAllReviews() {
        LOG.info("GET /reviews");

        try {
            Reviews[] reviewsArrayList = reviewDao.getAllReviews();
            if (reviewsArrayList != null)
                return new ResponseEntity<Reviews[]>(reviewsArrayList,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a review with the provided {@linkplain Review review}
     * 
     * @param {@link Review review} to create
     * 
     * @return ResponseEntity with created new review and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CREATED if hero object already exists and quantity is increased<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Reviews> addReview(@RequestBody Reviews reviews) {
        LOG.info("POST /reviews " + reviews);

        try {
            // if the review does not already exist
            if (reviewDao.getReviews(reviews.getName()) == null) {
                // then create a new one
                Reviews r = reviewDao.createReviews(reviews);
                return new ResponseEntity<Reviews>(r,HttpStatus.CREATED);
            }
            else { // if the review does exist
                // then update the list of reviews
                Reviews r = reviewDao.addReviews(reviews);
                return new ResponseEntity<Reviews>(r,HttpStatus.CREATED);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

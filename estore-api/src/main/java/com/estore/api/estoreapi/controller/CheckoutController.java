package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;

import com.estore.api.estoreapi.persistence.CheckoutFileDAO;
import com.estore.api.estoreapi.model.Product;

/**
 * Handles the REST API requests for the checkouts
 * @author Joe'sO'sTeam
 */

@RestController
@RequestMapping("checkout")
public class CheckoutController {
    private static final Logger LOG = Logger.getLogger(CheckoutController.class.getName());
    private CheckoutFileDAO checkoutFileDao;

    /**
     * Creates a REST API controller
     * @param cartFileDao The Object to perform CRUD operations
     */
    public CheckoutController(CheckoutFileDAO checkoutFileDao) {
        this.checkoutFileDao = checkoutFileDao;
    }

    /**
     * Gets a list of users checkouts
     * @param username the user's name
     * @return  a list of checkouts which is a list of products
     * @throws IOException
     */
    @GetMapping("/{username}")
    public ResponseEntity<Product[][]> getUserCheckouts(@PathVariable String username) throws IOException {
        LOG.info("GET /checkout/" + username);
        try {
            Product[][] checkouts = checkoutFileDao.getUserCheckouts(username);
            return new ResponseEntity<Product[][]>(checkouts, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Adds a new order to the users list of checkouts
     * @param username the user's name
     * @param newCheckout the new order to add
     * @return  the user's new list of checkouts
     */
    @PutMapping("/{username}")
    public ResponseEntity<Product[][]> addUserCheckout(@PathVariable String username, @RequestBody Product[] newCheckout) {
        LOG.info("PUT /checkouts/" + username);
        try {
            Product[][] curCheckouts = checkoutFileDao.addUserCheckout(username, newCheckout);
            
            if (curCheckouts != null) return new ResponseEntity<Product[][]>(curCheckouts, HttpStatus.OK);
            else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

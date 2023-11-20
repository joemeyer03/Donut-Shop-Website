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

import com.estore.api.estoreapi.persistence.CartFileDAO;

import com.estore.api.estoreapi.model.Product;

/**
 * Handles the REST API requests for the carts
 * @author Joe'sO'sTeam
 */

@RestController
@RequestMapping("login")
public class CartController {
    private static final Logger LOG = Logger.getLogger(CartController.class.getName());
    private CartFileDAO cartFileDao;

    /**
     * Creates a REST API controller
     * @param cartFileDao The Object to perform CRUD operations
     */
    public CartController(CartFileDAO cartFileDao) {
        this.cartFileDao = cartFileDao;
    }
    
    /**
     * Responds to the GET request for a Product[] for the given name
     * 
     * @param username The name used to locate the Product[]
     * 
     * @return ResponseEntity with Product[] and HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{username}")
    public ResponseEntity<Product[]> getUserCart(@PathVariable String username) throws IOException{
        LOG.info("GET /login/" + username);
        try {
            Product[] cart = cartFileDao.getUserCart(username);
            if (cart != null)
                return new ResponseEntity<Product[]>(cart, HttpStatus.OK);
            else {
                LOG.info("Couldn't get");
                createUserCart(username);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
                
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates an empty
     * 
     * @param username to link the user cart to
     * 
     * @return ResponseEntity with created Product[] and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CREATED if hero object already exists and quantity is increased<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("/{username}")
    public ResponseEntity<Product[]> createUserCart(@PathVariable String username) {
        LOG.info("POST /login/" + username);
        try {
            Product[] cart = cartFileDao.createUserCart(username);
            if (cart != null) {
                return new ResponseEntity<Product[]>(cart,HttpStatus.CREATED);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the cart with the new Product[]
     * 
     * @param username to link the cart to
     * @param newCart  replaces old cart linked to username
     * 
     * @return ResponseEntity with updated Product[] and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{username}")
    public ResponseEntity<Product[]> updateUserCart(@PathVariable String username, @RequestBody Product[] newCart) {
        LOG.info("PUT /login/" + username);
        try {
            Product[] cart = cartFileDao.updateUserCart(username, newCart);
            if (cart != null) {
                return new ResponseEntity<Product[]>(cart, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

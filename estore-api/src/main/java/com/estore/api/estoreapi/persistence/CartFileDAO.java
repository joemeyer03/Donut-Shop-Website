package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.estore.api.estoreapi.persistence.InventoryDAO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Product;

/**
 * Implements the functionality for JSON file-based peristance for Donut shop
 * 
 * @author JoesOsTeam
 */
@Component
public class CartFileDAO {
    private static final Logger LOG = Logger.getLogger(CartFileDAO.class.getName());
    // local cache of all the products. takes in the name and gives out the product object
    // converts between product objects and JSON text format
    private ObjectMapper objectMapper; 
    // name to read and write to
    private String filename;

    Map<String, Product[]> userCarts;

    /**
     * Constructor for inventory DAO
     * @param filename
     * @param objectMapper
     * @throws IOException
     */
    public CartFileDAO(@Value("${carts.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the products from the file
    }

    

    /**
     * Gets the user's cart
     * @param username the user's name linked to the cart
     * @return  a list of products that represent the cart
     * @throws IOException
     */
    public Product[] getUserCart(String username) throws IOException {
        if (userCarts.containsKey(username)) {
            return userCarts.get(username);
        }
        return null;
    }

    /**
     * Creates an empty cart for a new user
     * @param username  the new username
     * @return  the cart that was created
     * @throws IOException
     */
    public Product[] createUserCart(String username) throws IOException {
        if (userCarts.containsKey(username)) return null;
        LOG.info("creating user cart...");
        userCarts.put(username, new Product[0]);
        LOG.info("added to user carts");
        save(); 
        return new Product[0];
    }

    /**
     * Updates the user's cart with a new cart
     * @param username the user's name
     * @param newCart the cart to replace the old one
     * @return  the updated cart
     * @throws IOException
     */
    public Product[] updateUserCart(String username, Product[] newCart) throws IOException {
        if (userCarts.containsKey(username)) {
            userCarts.put(username, newCart);
            save();
            return getUserCart(username);
        }
        return null;
    }

    /**
     * Saves the products from the map into the file
     * @return true if written successfully
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        objectMapper.writeValue(new File(filename),userCarts);
        return true;
    }

    /**
     * Loads the products from the JSON file into the map
     * @return true if the file was read successfully
     * @throws IOException when file cannot be accessed
     */
    private boolean load() throws IOException {
        TypeReference<Map<String,Product[]>> typeRef = new TypeReference<Map<String,Product[]>>() {};
        userCarts = objectMapper.readValue(new File(filename), typeRef);
        return true;
    }
}
package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import com.estore.api.estoreapi.model.Product;

/**
 * Defines the interface for Product object persistence
 * 
 * @author BRYAN LEE
 */
public interface InventoryDAO {
    /**
     * Retrieves all {@linkplain Product products}
     * 
     * @return An array of all {@linkplain Product products) objects (can be empty)
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product[] getInventory() throws IOException;
    
    /**
     * Finds all {@linkplain Product products} whose names contains the given string 
     * 
     * @param search String to find in product names
     * 
     * @return An array of {@link Product product} whose names contains the given string (can be empty)
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product[] findProduct(String search) throws IOException;

    /**
     * Retrieves a {@linkplain Product product} with the given id
     * 
     * @param name The name of the {@linkplain Product product} to get
     * 
     * @return a {@link Product product} object with the matching id; null if no {@link Product product} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product getProduct(String name) throws IOException;

    /**
     * Creates and adds a new {@linkplain Product product}. If the product already exists the quantity is increased
     * 
     * @param product {@linkplain Product product} object to be created and added; unique ID is assigned
     * 
     * @return new {@link Product product} if successful, false otherwise
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product createProduct(Product product) throws IOException;

    /**
     * Updates {@linkplain Product product} 
     * 
     * @param product {@link Product product} object to be updated
     * 
     * @return updated {@link Product product} if successful, null if {@link Product product} cannot be found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Product updateProduct(String name, Product product) throws IOException;

    /**
     * Deletes a {@linkplain Product product} with  given id
     * 
     * @param name The name of the {@link Product product}
     * 
     * @return true if {@link Product product} was deleted; false if product with given name does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteProduct(String name) throws IOException;
}

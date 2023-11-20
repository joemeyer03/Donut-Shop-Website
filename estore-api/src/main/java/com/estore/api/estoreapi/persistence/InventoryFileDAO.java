package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.estore.api.estoreapi.persistence.InventoryDAO;
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
public class InventoryFileDAO implements InventoryDAO {
    private static final Logger LOG = Logger.getLogger(InventoryFileDAO.class.getName());
    // local cache of all the products. takes in the name and gives out the product object
    Map<String,Product> inventory;
    // converts between product objects and JSON text format
    private ObjectMapper objectMapper; 
    // name to read and write to
    private String filename;

    /**
     * Constructor for inventory DAO
     * @param filename
     * @param objectMapper
     * @throws IOException
     */
    public InventoryFileDAO(@Value("${products.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the products from the file
    }

    /**
     * Returns an array of all products
     * @return array of all products
     * @throws IOException when file cannot be accessed or written to
     */
    @Override
    public Product[] getInventory() throws IOException {
        ArrayList<Product> productArrayList = new ArrayList<>();

        for (Product product : inventory.values()) {
            productArrayList.add(product);
        }
        Collections.reverse(productArrayList);
        Product[] productArray = new Product[productArrayList.size()];
        productArrayList.toArray(productArray);
        return productArray;
    }

    /**
     * Returns array of products containing the name
     * @param name products with name will be returned
     * @return array of products with specified name, or contain specified name
     * @throws IOException when file cannot be accessed or written to
    */
    @Override
    public Product[] findProduct(String name) throws IOException {
        if (name == null) return null;
        ArrayList<Product> ProductsArrayList = new ArrayList<>();

        for (Product product : inventory.values()) {
            if (product.getName().contains(name)) {
                ProductsArrayList.add(product);
            }
        }

        Product[] ProductsArray = new Product[ProductsArrayList.size()];
        ProductsArrayList.toArray(ProductsArray);
        return ProductsArray;
    }

    /**
     ** {@inheritDoc}}
     */
    @Override
    public Product createProduct(Product product) throws IOException {
        synchronized(inventory) {
            // We create a new product object
            Product newProduct = new Product(product.getPrice(),product.getName(),product.getQuantity(), product.getImage_url());
            inventory.put(newProduct.getName(),newProduct);
            save(); // may throw an IOException
            return newProduct;
        }
    }

    /**
     ** {@inheritDoc}}
     */
    @Override
    public Product getProduct(String name) throws IOException {
        Product product = inventory.get(name);
        return product;
    }
    
    /**
     * Saves the products from the map into the file
     * @return true if written successfully
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Product[] ProductsArray = getInventory();   // Gets all products

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),ProductsArray);
        return true;
    }


    /**
     * Loads the products from the JSON file into the map
     * @return true if the file was read successfully
     * @throws IOException when file cannot be accessed
     */
    private boolean load() throws IOException {
        inventory = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of heroes
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Product[] ProductsArray = objectMapper.readValue(new File(filename),Product[].class);

        // Add each hero to the tree map 
        for (Product product : ProductsArray) {
            inventory.put(product.getName(),product);
        }
        return true;
    }

    /**
     * Deletes a product with given name
     * @return true if product exists and is deleted; false if product was not found with given name
     * @throws IOException when file cannot be accessed
     */
    @Override
    public boolean deleteProduct(String name) throws IOException {
        synchronized(inventory) {
            if (inventory.containsKey(name)) {
                inventory.remove(name);
                return save();
            }
            else return false;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Product updateProduct(String name, Product product) throws IOException {
        synchronized(inventory) {
            if (inventory.containsKey(name) == false)
                return null;  // product does not exist
            inventory.remove(name);
            inventory.put(product.getName(),product);
            save(); // may throw an IOException
            return product;
        }
    }

    //Return true if the product is in inventory, and it's quantity is greater or equal to the amt
    public boolean containsProductWithAmt(Product product) {
        if (inventory.containsKey(product.getName())) {
            Product curProduct = inventory.get(product.getName());
            if (curProduct.getQuantity() >= product.getQuantity()) return true;
        }
        return false;
    }

    //Return the quantity of that product in the inventory
    public int getProductQuantity(Product product) {
        if (inventory.containsKey(product.getName())) {
            Product curProduct = inventory.get(product.getName());
            return curProduct.getQuantity();
        }
        return 0;
    }

    //Remove specified amount from product in inventory
    public void removeProduct(Product product, int amt) throws IOException {
        if (inventory.containsKey(product.getName())) {
            Product curProduct = inventory.get(product.getName());
            int curAmt = curProduct.getQuantity();
            if (curAmt > amt) {
                //Shouldn't need to remove and add to inv since this is reference type right?
                curProduct.setQuantity(curAmt - amt);
            }
            else {
                //Should the product's quantity be set to 0, or should it be removed from the inventory map?
                //Probably set to 0, so the img_url is kept in case it gets restocked
                curProduct.setQuantity(0);
            }
            save();
        }
    }

    //Remove the product from the inventory (actually sets quantity to zero, so the img is retained)
    public void removeProduct(Product product) throws IOException{
        if (inventory.containsKey(product.getName())) {
            Product curProduct = inventory.get(product.getName());
            curProduct.setQuantity(0);
            save();
        }
    }
}
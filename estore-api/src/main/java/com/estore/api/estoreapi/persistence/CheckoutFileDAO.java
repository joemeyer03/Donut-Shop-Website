package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Product;

/**
 * Implements the functionality for JSON file-based peristance for Donut shop
 * 
 * @author JoesOsTeam
 */
@Component
public class CheckoutFileDAO {
    // converts between product objects and JSON text format
    private ObjectMapper objectMapper; 
    // name to read and write to
    private String filename;

    InventoryFileDAO inventoryDao;

    //User checkouts is an array of carts (so an array of arrays)
    Map<String, Product[][]> userCheckouts;

    /**
     * Constructor for checkout DAO
     * @param filename
     * @param objectMapper
     * @throws IOException
     */
    public CheckoutFileDAO(@Value("${checkout.file}") String filename,ObjectMapper objectMapper, InventoryFileDAO inventoryDao) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.inventoryDao = inventoryDao;
        load();  // load the products from the file
    }

    /**
     * Gets the list of user orders
     * @param username  the user's name
     * @return  the list of list of products they have ordered
     * @throws IOException
     */
    public Product[][] getUserCheckouts(String username) throws IOException {
        if (userCheckouts.containsKey(username)) {
            return userCheckouts.get(username);
        }
        return null;
    }

    /**
     * Adds a checkout to the list of user orders
     * @param username  the user's name
     * @param newCheckout   the checkout to be added to the list
     * @return  the list of checkouts
     * @throws IOException
     */
    public Product[][] addUserCheckout(String username, Product[] newCheckout) throws IOException {
        if (!userCheckouts.containsKey(username)) userCheckouts.put(username, new Product[0][]);

        if (newCheckout != null && userCheckouts.containsKey(username)) {
            //Make a list containing the actual products and quantities that can be sold
            
            LinkedList<Product> productList = new LinkedList<Product>();

            //Loop over every product in the new order
            for(Product curProduct : newCheckout) {
                //If the inventory has enough of that product, add it to the list, and remove from inventory.
                if (inventoryDao.containsProductWithAmt(curProduct)) {
                    productList.add(curProduct);
                    inventoryDao.removeProduct(curProduct, curProduct.getQuantity());
                }
                else {
                    //If the inventory has some of the product, add that amount to the list, and remove from inventory.
                    int productAmt = inventoryDao.getProductQuantity(curProduct);

                    if (productAmt > 0) {
                        Product addProduct = new Product(curProduct.getPrice(), curProduct.getName(), productAmt, curProduct.getImage_url());
                        productList.add(addProduct);
                        inventoryDao.removeProduct(curProduct);
                    }
                }
            }

            //Make new array with one size larger, containing all older orders.
            
            Product[][] curCheckouts = userCheckouts.get(username);
            Product[][] newCheckouts = new Product[curCheckouts.length + 1][];

            for (int i = 0; i < curCheckouts.length; i++) {
                newCheckouts[i] = curCheckouts[i];
            }

            //Set the furthest index to the new order
            //Have to cast to Product[]

            Product[] newCheckoutArray = new Product[productList.size()];

            int index = 0;
            //Casting from Object[] to Product[] doesn't work so have to do this garbage.
            for(Product curProduct : productList) {
                newCheckoutArray[index] = curProduct;
                index++;
            }

            //newCheckouts[curCheckouts.length] = (Product[])productList.toArray(); -- Doesn't work! Thanks java!
            newCheckouts[curCheckouts.length] = newCheckoutArray;

            //Update user's orders (Don't know if doing remove is neccessary with java)

            userCheckouts.remove(username);
            userCheckouts.put(username, newCheckouts);

            save();

            return userCheckouts.get(username);
        }
        return null;
    }

    /**
     * Saves the products from the map into the file
     * @return true if written successfully
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        objectMapper.writeValue(new File(filename),userCheckouts);
        return true;
    }

    /**
     * Loads the products from the JSON file into the map
     * @return true if the file was read successfully
     * @throws IOException when file cannot be accessed
     */
    private boolean load() throws IOException {
        TypeReference<Map<String,Product[][]>> typeRef = new TypeReference<Map<String,Product[][]>>() {};
        userCheckouts = objectMapper.readValue(new File(filename), typeRef);
        return true;
    }
}
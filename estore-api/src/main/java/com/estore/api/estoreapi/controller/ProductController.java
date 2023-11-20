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

import com.estore.api.estoreapi.persistence.InventoryDAO;

import com.estore.api.estoreapi.model.Product;

/**
 * Handles the REST API requests for the Products
 * @author Joe'sO'sTeam
 */

@RestController
@RequestMapping("products")
public class ProductController {
    private static final Logger LOG = Logger.getLogger(ProductController.class.getName());
    private InventoryDAO inventoryDao;

    /**
     * Creates a REST API controller
     * @param inventoryDao The Object to perform CRUD operations
     */
    public ProductController(InventoryDAO inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Product product} for the given name
     * 
     * @param name The name used to locate the {@link Product product}
     * 
     * @return ResponseEntity with {@link Product product} object and HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{name}")
    public ResponseEntity<Product> getProduct(@PathVariable String name) {
        LOG.info("GET /products/" + name);
        try {
            Product product = inventoryDao.getProduct(name);
            if (product != null)
                return new ResponseEntity<Product>(product,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Product products}
     * 
     * @return ResponseEntity with array of {@link Product products} objects (may be empty) and
     * HTTP status of OK
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Product[]> getProducts() {
        LOG.info("GET /products");

        try {
            Product[] productArrayList = inventoryDao.getInventory();
            if (productArrayList != null)
                return new ResponseEntity<Product[]>(productArrayList,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Product products} whose name contains
     * the text in name
     * @param name The name parameter which contains the text used to find the {@link Product products}
     * @return ResponseEntity with array of {@link Product product} objects (may be empty) and
     * HTTP status of OK
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * Example: Find all products that contain the text "ma"
     * GET http://localhost:8080/products/?name=ma
     */
    @GetMapping("/")
    public ResponseEntity<Product[]> findProduct(@RequestParam String name) {
        LOG.info("GET /products/?name=" + name);
        try {
            Product[] products = inventoryDao.findProduct(name);
            return new ResponseEntity<Product[]>(products,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a product with the provided product object
     * 
     * @param product the product to create
     * 
     * @return ResponseEntity with created new product and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CREATED if product object already exists and quantity is increased<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        LOG.info("POST /product " + product);

        try {
            // if the product does not already exist
            if (inventoryDao.getProduct(product.getName()) == null) {
                // then create a new one
                Product p = inventoryDao.createProduct(product);
                return new ResponseEntity<Product>(p,HttpStatus.CREATED);
            }
            else { // if the product does exist
                // then increase its quantity
                Product p = inventoryDao.getProduct(product.getName());
                p.increaseQuantity();
                return new ResponseEntity<Product>(p,HttpStatus.CREATED);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the product with the provided product object, if it exists
     * 
     * @param product The product to update
     * 
     * @return ResponseEntity with updated product object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{name}")
    public ResponseEntity<Product> updateProduct(@PathVariable String name, @RequestBody Product product) {
        LOG.info("PUT /products/" + name + "/" + product);

        try {
            Product p = inventoryDao.updateProduct(name, product);
            if (p != null)
                return new ResponseEntity<Product>(p,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Product product} with the given name
     * 
     * @param name The name of the {@link Product product} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Product> deleteProduct(@PathVariable String name) {
        LOG.info("DELETE /product/" + name);
        try {
            if (inventoryDao.deleteProduct(name)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Product;

@Tag("Model-tier")
public class ProductTest {
   
    @Test
    public void testConstructor() {
        Product p = new Product(1.5, "donut", 1, null);
        assertEquals(1.5, p.getPrice());
        assertEquals("donut", p.getName());
        assertEquals(1, p.getQuantity());
    }

    @Test
    public void testPrice() {
        // setup
        double price = 1.5;
        double newPrice = 2;
        String name = "donut";
        int quantity = 1;
        Product p = new Product(price, name, quantity, null);

        // invoke get
        double actualPrice = p.getPrice();

        // test get
        assertEquals(price, actualPrice);

        // invoke set
        p.setPrice(newPrice);
        double actualNewPrice = p.getPrice();

        // test set
        assertEquals(newPrice, actualNewPrice);
    }

    @Test
    public void testName() {
        // setup
        double price = 1.5;
        String name = "donut";
        String newName = "notDonut";
        int quantity = 1;
        Product p = new Product(price, name, quantity, null);

        // invoke get
        String actualName = p.getName();

        // test get
        assertEquals(name, actualName);

        // invoke set
        p.setName(newName);
        String actualNewName = p.getName();

        // test set
        assertEquals(newName, actualNewName);
    }

    @Test
    public void testGetQuantity() {
        // setup
        double price = 1.5;
        String name = "donut";
        int quantity = 1;
        int newQuantity = 2;
        Product p = new Product(price, name, quantity, null);

        // invoke get
        double actualQuantity = p.getQuantity();

        // test get
        assertEquals(quantity, actualQuantity);

        // invoke set
        p.increaseQuantity();
        int actualNewQuantity = p.getQuantity();

        // test set
        assertEquals(newQuantity, actualNewQuantity);
    }

    @Test
    public void testToString() {
        // setup
        double price = 1.5;
        String name = "donut";
        int quantity = 1;
        Product p = new Product(price, name, quantity, null);
        String exepectedString = String.format("Product [price=%f, name=%s, quantity=%d]",price,name,quantity);

        // invoke
        String actualString = p.toString();

        // test
        assertEquals(exepectedString, actualString);
        
    }
}

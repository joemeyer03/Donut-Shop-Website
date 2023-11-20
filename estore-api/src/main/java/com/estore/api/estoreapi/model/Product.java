package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Product class that stores the name, price, and quantity of a product
 */
public class Product {
    /*
     * The Price, Name and Quantity of the product
     */
    @JsonProperty("price") private double price;
    @JsonProperty("name") private String name;
    @JsonProperty("quantity") private int quantity = 0;
    @JsonProperty("image") private String image_url;

    /**
     * Constructor for a product
     * @param price the price of the product
     * @param name the name of the product    
     */
    public Product (@JsonProperty("price") double price, @JsonProperty("name") String name, @JsonProperty("quantity") int quantity , @JsonProperty("image") String image_url ) {
        this.price = price;
        this.name = name;
        this.quantity = quantity;
        this.image_url = image_url;
    }

    /**
     * Gets the price of this product
     * @return the price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the name of the product
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the quantity of the product
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }
    /**
     * Gets the image url of the product
     * @return the quantity
     */
    public String getImage_url() {
        return image_url;
    }

    /**
     * Sets the price
     * @param price the new price to set to
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the name
     * @param name the new name to set to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the quantity
     * @param quantity the new quantity to set to
     */
    public void increaseQuantity() {
        this.quantity++;
    }

    public void setQuantity(int set) {
        this.quantity = set;
    }
    
    /**
     * Returns the product in a string format
     * @return a string representation of the product
     */
    @Override
    public String toString() {
        return String.format("Product [price=%f, name=%s, quantity=%d]",price,name,quantity);
    }
}

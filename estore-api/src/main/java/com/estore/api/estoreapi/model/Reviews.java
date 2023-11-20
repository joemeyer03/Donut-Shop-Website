package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Review class that stores the name and list of reviews of a product
 */
public class Reviews {
    /*
     * The Name and list of reviews of the product
     */
    @JsonProperty("name") private String name;
    @JsonProperty("reviews") private String[] reviews;

    /**
     * Constructor for a reviews
     * @param name the name of the product  
     * @param reviews the reviews of the product  
     */
    public Reviews (@JsonProperty("name") String name, @JsonProperty("reviews") String[] reviews) {
        this.name = name;
        this.reviews = reviews;
    }

    /**
     * Gets the name of the product
     * @return the product's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the reviews of the product
     * @return   a list of strings that represent the reviews
     */
    public String[] getReviews() {
        return reviews;
    }

    /**
     * Sets the product's name
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the reviews
     * @param reviews the new list of reviews
     */
    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }

    /**
     * A string representation of the object
     */
    @Override
    public String toString() {
        StringBuffer reviewsBuffer = new StringBuffer();
        for (int i = 1; i < reviews.length; i++) {
            reviewsBuffer.append(reviews[i] + "\n");
        } 
        String reviewsString = reviewsBuffer.toString();
        return String.format("Product [name=%s, reviews=%s]",name,reviewsString);
    }
}
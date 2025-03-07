package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Product;

import java.util.List;

public interface ProductDAO {

    /**
     * Returns a List of all products in the file of products.
     *
     * @return List containing all products from file.
     */
    List<Product> getAllProducts() throws PersistenceException;

    /**
     * Returns a Product of the provided product type.
     *
     * @param productType  string describing the type of product to get
     * @return Product object of the provided type
     */
    Product getProduct(String productType) throws PersistenceException;

    /**
     * Returns a boolean depending on if the type of product exists in the products fild
     *
     * @param productType  string describing the type of product to check
     * @return boolean literal stating whether the type exists or not
     */
    boolean productExists(String productType) throws PersistenceException;
}

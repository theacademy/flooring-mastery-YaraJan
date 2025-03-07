package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Product;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProductDaoFileImpl implements ProductDAO{

    private Map<String, Product> products = new HashMap<>();
    private final String PRODUCT_FILE;
    public static final String DELIMITER = ",";

    public ProductDaoFileImpl() {
        PRODUCT_FILE = "Products.txt";
    }

    public ProductDaoFileImpl(String productTextFile){
        PRODUCT_FILE = productTextFile;
    }

    @Override
    public List<Product> getAllProducts() throws PersistenceException {
        loadProductData();

        return products.values().stream().toList();
    }

    @Override
    public Product getProduct(String productType) throws PersistenceException {
        loadProductData();

        return products.get(productType);
    }

    @Override
    public boolean productExists(String productType) throws PersistenceException {
        return getProduct(productType) != null;
    }

    private void loadProductData() throws PersistenceException{
        Scanner scanner;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Could not load Product data into memory.", e);
        }

        String currentLine;
        Product currentProduct;

        //skips past header text line
        if(scanner.hasNextLine()) {
            scanner.nextLine();
        }

        //loops through an unmarshalls all the text from the products file
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);

            products.put(currentProduct.getProductType().toLowerCase(), currentProduct);
        }

        scanner.close();
    }

    private Product unmarshallProduct(String productText) {
        String[] productTokens = productText.split(DELIMITER);
        Product newProduct = new Product();
        newProduct.setProductType(productTokens[0]);
        newProduct.setCostPerSqFt(new BigDecimal(productTokens[1]));
        newProduct.setLaborCostPerSqFt(new BigDecimal(productTokens[2]));

        return newProduct;
    }
}

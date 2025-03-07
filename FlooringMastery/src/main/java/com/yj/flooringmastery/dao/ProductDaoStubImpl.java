package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoStubImpl implements ProductDAO{

    public Product onlyProduct;

    public ProductDaoStubImpl() {
        onlyProduct = new Product();
        onlyProduct.setProductType("carpet");
        onlyProduct.setLaborCostPerSqFt(new BigDecimal("2.10"));
        onlyProduct.setCostPerSqFt(new BigDecimal("2.25"));
    }

    @Override
    public List<Product> getAllProducts() throws PersistenceException {
        List<Product> products = new ArrayList<>();
        products.add(onlyProduct);
        return products;
    }

    @Override
    public Product getProduct(String productType) throws PersistenceException {
        if(onlyProduct.getProductType().equals(productType)) {
            return onlyProduct;
        }
        return null;
    }

    @Override
    public boolean productExists(String productType) throws PersistenceException {
        if(onlyProduct.getProductType().equals(productType)) {
            return true;
        }
        return false;
    }
}

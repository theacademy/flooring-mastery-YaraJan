package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoFileImplTest {

    private ProductDAO testDao;

    public ProductDaoFileImplTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testDao = ctx.getBean("productDao", ProductDAO.class);
    }

    @BeforeEach
    void setUp() throws Exception{
        PrintWriter out;
        String testFile = "testProducts.txt";
        // Use the FileWriter to quickly blank and fill the file
        out = new PrintWriter(new FileWriter(testFile));
        out.println("ProductType,CostPerSquareFoot,LaborCostPerSquareFoot");
        out.flush();
        out.println("Carpet,2.25,2.10");
        out.flush();
        out.println("Laminate,1.75,2.10");
        out.flush();
        out.println("Tile,3.50,4.15");
        out.flush();
        out.println("Wood,5.15,4.75");
        out.flush();
        out.close();
    }

    @AfterEach
    void afterEach() throws Exception {
        File testFile = new File("testProducts.txt");
        testFile.delete();
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = testDao.getAllProducts();

        assertNotNull(products, "The list of products must not null");
        assertEquals(4, products.size(),"List of products should have 4 products.");

        Product checkProduct = new Product();
        checkProduct.setProductType("Wood");
        checkProduct.setCostPerSqFt(new BigDecimal("5.15"));
        checkProduct.setLaborCostPerSqFt(new BigDecimal("4.75"));
        assertTrue(products.contains(checkProduct),
                "The list of products should include wood.");
    }

    @Test
    void testGetProduct() {
        Product product = testDao.getProduct("laminate");
        Product fakeProduct = testDao.getProduct("Block");

        assertNotNull(product, "checking retrieved product exists.");
        assertNull(fakeProduct, "checking retrieved product does not exist.");
        assertEquals(product.getProductType(), "Laminate",
                     "checking product type.");
        assertEquals(product.getCostPerSqFt(), new BigDecimal("1.75"),
                        "checking cost per square foot.");
        assertEquals(product.getLaborCostPerSqFt(), new BigDecimal("2.10"),
                "checking labor cost per square foot.");
    }

    @Test
    void testProductExists() {
        assertTrue(testDao.productExists("wood"), "checking wood exists.");
        assertTrue(testDao.productExists("laminate"), "checking laminate exists.");
        assertTrue(testDao.productExists("carpet"), "checking carpet exists.");
        assertTrue(testDao.productExists("tile"), "checking tile exists.");
        assertFalse(testDao.productExists("block"), "checking block does not exist.");
    }
}
package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Order;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderDaoFileImpTest {
    OrderDAO testDao;

    public OrderDaoFileImpTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testDao = ctx.getBean("orderDao", OrderDaoFileImp.class);
    }

    @BeforeEach
    void setUp() throws Exception{
        // Use the FileWriter to quickly blank the file
        new FileWriter("Orders_03062025.txt");
    }

    @AfterAll
    static void afterAll() throws Exception {
        File testFile = new File("Orders_03062025.txt");
        testFile.delete();
    }

    @Test
    void testAddGetAllOrders() throws Exception{
        //create method test input orders
        Order firstOrder = new Order(1, "Bob Dylan", "Tile", new BigDecimal("2"));
        firstOrder.setState("TX");
        firstOrder.setTaxRate(new BigDecimal("25"));
        firstOrder.setCostPerSquareFoot(new BigDecimal("3.5"));
        firstOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        firstOrder.setMaterialCost(new BigDecimal("30"));
        firstOrder.setLaborCost(new BigDecimal("1033.35"));
        firstOrder.setTax(new BigDecimal("476.21"));
        firstOrder.setTotal(new BigDecimal("2381.00"));

        Order secondOrder = new Order(2, "Tom James", "Tile", new BigDecimal("3.5"));
        secondOrder.setState("TX");
        secondOrder.setTaxRate(new BigDecimal("25"));
        secondOrder.setCostPerSquareFoot(new BigDecimal("13.5"));
        secondOrder.setLaborCostPerSquareFoot(new BigDecimal("14.15"));
        secondOrder.setMaterialCost(new BigDecimal("20"));
        secondOrder.setLaborCost(new BigDecimal("2033.35"));
        secondOrder.setTax(new BigDecimal("276.00"));
        secondOrder.setTotal(new BigDecimal("1500.00"));

        //add orders to dao
        testDao.addOrder(LocalDate.parse("2025-03-06"), firstOrder);
        testDao.addOrder(LocalDate.parse("2025-03-06"), secondOrder);

        // Retrieve the list of all orders within the DAO
        List<Order> allOrders = testDao.getAllOrders(LocalDate.parse("2025-03-06"));

        //test certain qualities of the list and if it contains the correct objects
        assertNotNull(allOrders, "The list of orders must not null");
        assertEquals(2, allOrders.size(),"List of orders should have 2 orders.");

        assertTrue(testDao.getAllOrders(LocalDate.parse("2025-03-06")).contains(firstOrder),
                "The list of DVDs should include firstOrder.");
        assertTrue(testDao.getAllOrders(LocalDate.parse("2025-03-06")).contains(secondOrder),
                "The list of DVDs should include secondOrder.");
    }

    @Test
    void testRemoveGetOrder() throws Exception {
        //create method test input orders
        Order firstOrder = new Order(1, "Bob Dylan", "Tile", new BigDecimal("2"));
        firstOrder.setState("TX");
        firstOrder.setTaxRate(new BigDecimal("25"));
        firstOrder.setCostPerSquareFoot(new BigDecimal("3.5"));
        firstOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        firstOrder.setMaterialCost(new BigDecimal("30"));
        firstOrder.setLaborCost(new BigDecimal("1033.35"));
        firstOrder.setTax(new BigDecimal("476.21"));
        firstOrder.setTotal(new BigDecimal("2381.00"));

        Order secondOrder = new Order(2, "Tom James", "Tile", new BigDecimal("3.5"));
        secondOrder.setState("TX");
        secondOrder.setTaxRate(new BigDecimal("25"));
        secondOrder.setCostPerSquareFoot(new BigDecimal("13.5"));
        secondOrder.setLaborCostPerSquareFoot(new BigDecimal("14.15"));
        secondOrder.setMaterialCost(new BigDecimal("20"));
        secondOrder.setLaborCost(new BigDecimal("2033.35"));
        secondOrder.setTax(new BigDecimal("276.00"));
        secondOrder.setTotal(new BigDecimal("1500.00"));

        //add orders to dao
        testDao.addOrder(LocalDate.parse("2025-03-06"), firstOrder);
        testDao.addOrder(LocalDate.parse("2025-03-06"), secondOrder);

        //check get order is equal to the first order
        Order retrievedOrder = testDao.getOrder(1, LocalDate.parse("2025-03-06"));
        assertNotNull(retrievedOrder, "Order retrieved should not be null");
        assertEquals(retrievedOrder.getOrderNumber(), 1, "Check order number is the same.");
        assertEquals(retrievedOrder.getState(), "TX", "Check order state is the same.");
        assertEquals(retrievedOrder.getTaxRate(), new BigDecimal("25"), "Check order tax rate is the same.");
        assertEquals(retrievedOrder.getCostPerSquareFoot(), new BigDecimal("3.5"), "Check order cost per sqft is the same.");
        assertEquals(retrievedOrder.getLaborCostPerSquareFoot(), new BigDecimal("4.15"), "Check order labor cost per sqft is the same.");
        assertEquals(retrievedOrder.getMaterialCost(), new BigDecimal("30"), "Check order material cost is the same.");
        assertEquals(retrievedOrder.getLaborCost(), new BigDecimal("1033.35"), "Check order labor cost is the same.");
        assertEquals(retrievedOrder.getTax(), new BigDecimal("476.21"), "Check order tax cost is the same.");
        assertEquals(retrievedOrder.getTotal(), new BigDecimal("2381.00"), "Check order total is the same.");

        // remove the first order - order number 1
        Order removedOrder = testDao.removeOrder(1, LocalDate.parse("2025-03-06"));

        // Check that the correct object was removed.
        assertEquals(removedOrder, firstOrder, "The removed order should be fisrtOrder.");

        List<Order> allOrders = testDao.getAllOrders(LocalDate.parse("2025-03-06"));

        assertNotNull(allOrders, "The list of orders must not null");
        assertEquals(1, allOrders.size(),"List of orders should have 1 order.");

        assertFalse(allOrders.contains(firstOrder), "All orders should NOT include firstOrder.");
        assertTrue(allOrders.contains(secondOrder), "All orders should include secondOrder.");

        //remove second order - order number 2
        removedOrder =  testDao.removeOrder(2, LocalDate.parse("2025-03-06"));
        assertEquals(removedOrder, secondOrder, "The removed order should be secondOrder.");

        allOrders = testDao.getAllOrders(LocalDate.parse("2025-03-06"));

        assertTrue(allOrders.isEmpty(), "The retrieved list of orders should be empty.");
    }

    @Test
    void testEditOrder() throws Exception {
        //create method test input orders
        Order firstOrder = new Order(1, "Bob Dylan", "Tile", new BigDecimal("2"));
        firstOrder.setState("TX");
        firstOrder.setTaxRate(new BigDecimal("25"));
        firstOrder.setCostPerSquareFoot(new BigDecimal("3.5"));
        firstOrder.setLaborCostPerSquareFoot(new BigDecimal("4.15"));
        firstOrder.setMaterialCost(new BigDecimal("30"));
        firstOrder.setLaborCost(new BigDecimal("1033.35"));
        firstOrder.setTax(new BigDecimal("476.21"));
        firstOrder.setTotal(new BigDecimal("2381.00"));

        Order secondOrder = new Order(1, "Tom James", "Tile", new BigDecimal("3.5"));
        secondOrder.setState("TX");
        secondOrder.setTaxRate(new BigDecimal("25"));
        secondOrder.setCostPerSquareFoot(new BigDecimal("13.5"));
        secondOrder.setLaborCostPerSquareFoot(new BigDecimal("14.15"));
        secondOrder.setMaterialCost(new BigDecimal("20"));
        secondOrder.setLaborCost(new BigDecimal("2033.35"));
        secondOrder.setTax(new BigDecimal("276.00"));
        secondOrder.setTotal(new BigDecimal("1500.00"));

        testDao.addOrder(LocalDate.parse("2025-03-06"), firstOrder);
        Order editedOrder = testDao.editOrder(LocalDate.parse("2025-03-06"), secondOrder);

        assertEquals(editedOrder, firstOrder, "The edited order should be firstOrder");
        List<Order> allOrders = testDao.getAllOrders(LocalDate.parse("2025-03-06"));

        assertNotNull(allOrders, "The list of orders must not null");
        assertEquals(1, allOrders.size(),"List of orders should have 1 order.");
        assertFalse(allOrders.contains(firstOrder), "All orders should NOT include firstOrder.");
        assertTrue(allOrders.contains(secondOrder), "All orders should include secondOrder.");
    }
}
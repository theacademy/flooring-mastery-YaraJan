package com.yj.flooringmastery.service;

import com.yj.flooringmastery.dao.PersistenceException;
import com.yj.flooringmastery.model.Order;
import com.yj.flooringmastery.model.Product;
import com.yj.flooringmastery.model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceLayerImplTest {

    private OrderServiceLayer service;

    public OrderServiceLayerImplTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = ctx.getBean("service", OrderServiceLayer.class);
    }

    @BeforeEach
    void setUp() throws Exception{
        // Use the FileWriter to quickly blank the file
        new FileWriter("Orders_03062025.txt");
    }

    @Test
    void testAddEditGetOrder() {
        LocalDate date = LocalDate.parse("2025-03-06");
        //Valid order
        Order order = new Order(1, "John Doe", "Tile", new BigDecimal("102"));
        order.setState("TX");
        order.setLaborCostPerSquareFoot(new BigDecimal("25"));
        order.setTax(new BigDecimal("25"));
        order.setCostPerSquareFoot(new BigDecimal("25"));
        order.setTaxRate(new BigDecimal("25"));
        order.setTotal(new BigDecimal("25"));
        order.setMaterialCost(new BigDecimal("25"));
        order.setLaborCost(new BigDecimal("25"));

        try {
            service.addOrder(date, order);
        } catch (PersistenceException
                    | NoSuchOrderException
                    | InvalidOrderDataException e) {
            fail("Order was valid. No exception should have been thrown.");
        }

        service.editOrder(date, order);
        Order editedOrder = service.getOrder(1, date);
        assertEquals(order, editedOrder,"Order should equal the edited order.");
    }

    @Test
    void editOrderCalc() {
        LocalDate date = LocalDate.parse("2025-03-06");

        Order oldOrder = new Order(1, "John Doe", "Tile", new BigDecimal("102"));
        oldOrder.setState("TX");
        oldOrder.setLaborCostPerSquareFoot(new BigDecimal("25"));
        oldOrder.setTax(new BigDecimal("25"));
        oldOrder.setCostPerSquareFoot(new BigDecimal("25"));
        oldOrder.setTaxRate(new BigDecimal("25"));
        oldOrder.setTotal(new BigDecimal("25"));
        oldOrder.setMaterialCost(new BigDecimal("25"));
        oldOrder.setLaborCost(new BigDecimal("25"));

        Order newOrder = service.editOrderCalc(1, oldOrder, date, "", "", "", "");
        assertNotNull(newOrder, "New order should not be null.");
        assertEquals(newOrder, oldOrder, "New order should equal old order.");

        Order newNameOrder = service.editOrderCalc(1, newOrder, date, "Jo", "", "", "");
        assertNotNull(newNameOrder, "New order should not be null.");
        assertNotEquals(newNameOrder, oldOrder, "New order should not equal old order.");
    }

    @Test
    void removeOrder() {
        LocalDate date = LocalDate.parse("2025-03-06");
        //Valid order
        Order order = new Order(1, "John Doe", "Tile", new BigDecimal("102"));
        order.setState("TX");
        order.setLaborCostPerSquareFoot(new BigDecimal("25"));
        order.setTax(new BigDecimal("25"));
        order.setCostPerSquareFoot(new BigDecimal("25"));
        order.setTaxRate(new BigDecimal("25"));
        order.setTotal(new BigDecimal("25"));
        order.setMaterialCost(new BigDecimal("25"));
        order.setLaborCost(new BigDecimal("25"));

        try {
            service.removeOrder(order.getOrderNumber(), date);
        } catch (PersistenceException
                 | NoSuchOrderException
                 | InvalidOrderDataException e) {
            fail("Order was valid. No exception should have been thrown.");
        }
    }

    @Test
    void getAllOrders() {
        LocalDate date = LocalDate.parse("2025-03-06");

        Order order = new Order(1, "John Doe", "Tile", new BigDecimal("102"));
        order.setState("TX");
        order.setLaborCostPerSquareFoot(new BigDecimal("25"));
        order.setTax(new BigDecimal("25"));
        order.setCostPerSquareFoot(new BigDecimal("25"));
        order.setTaxRate(new BigDecimal("25"));
        order.setTotal(new BigDecimal("25"));
        order.setMaterialCost(new BigDecimal("25"));
        order.setLaborCost(new BigDecimal("25"));

        assertEquals(1, service.getAllOrders(date).size(), "Should have one order.");
        assertTrue(service.getAllOrders(date).contains(order));
    }

    @Test
    void getAllProducts() {
        Product product = new Product();
        product.setProductType("carpet");
        product.setLaborCostPerSqFt(new BigDecimal("2.10"));
        product.setCostPerSqFt(new BigDecimal("2.25"));

        List<Product> retrievedProduct = service.getAllProducts();
        assertNotNull(retrievedProduct, "Retrieved list should not be null.");
        assertEquals(1, retrievedProduct.size(), "Retrieved list should have size of 1.");
        assertTrue(retrievedProduct.contains(product), "List should have product in it.");
    }

    @Test
    void getAllTaxes() {
        Tax tax = new Tax();
        tax.setTaxRate(new BigDecimal("6"));
        tax.setStateName("kentucky");
        tax.setStateAbbreviation("KY");

        List<Tax> taxes = service.getAllTaxes();
        assertNotNull(taxes, "Retrieved list should not be null.");
        assertEquals(1, taxes.size(), "Retrieved list should have size of 1.");
        assertTrue(taxes.contains(tax), "List should have tax in it.");
    }

    @Test
    void calculateOrderValues() {
        Order calculatedOrder = service.calculateOrderValues(1, "Einstein", "KY", "carpet", new BigDecimal("217.00"));

        assertEquals(new BigDecimal("488.25"), calculatedOrder.getMaterialCost(), "Material cost should be 488.25.");
        assertEquals(new BigDecimal("455.70"), calculatedOrder.getLaborCost(), "Labor cost should be 455.70.");
        assertEquals(new BigDecimal("56.64"), calculatedOrder.getTax(), "Tax cost should be 56.64.");
        assertEquals(new BigDecimal("1000.59"), calculatedOrder.getTotal(), "Total cost should be 1000.59.");
    }

    @Test
    void testValidateData() {

        //testDate
        try {
            service.validateDate(LocalDate.parse("2003-01-01"));
        } catch (InvalidOrderDataException e) {
        }
        catch (Exception e) {
            fail("Should have caught InvalidOrderDataException");
        }
        assertTrue(service.validateDate(LocalDate.parse("2033-01-01")), "Date should be after today.");

        //test name
        try {
            service.validateName("");
        } catch (InvalidOrderDataException e) {
        }
        catch (Exception e) {
            fail("Should have caught InvalidOrderDataException");
        }
        try {
            service.validateName("39-r-399&&");
        } catch (InvalidOrderDataException e) {
        }
        catch (Exception e) {
            fail("Should have caught InvalidOrderDataException");
        }
        assertTrue(service.validateName("Bob8, the builder."), "Name should only contain alphanumerics and periods and commas.");

        //test state
        try {
            service.validateState("ken");
        } catch (InvalidOrderDataException e) {
        }
        catch (Exception e) {
            fail("Should have caught InvalidOrderDataException");
        }
        try {
            service.validateState("CA");
        } catch (InvalidOrderDataException e) {
        }
        catch (Exception e) {
            fail("Should have caught InvalidOrderDataException");
        }
        assertTrue(service.validateState("kentucky"), "Kentucky should be validated as a state.");
        assertTrue(service.validateState("KY"), "KY should be validated as a state.");

        //check area
        try {
            service.validateArea(new BigDecimal("98"));
        } catch (InvalidOrderDataException e) {
        }
        catch (Exception e) {
            fail("Should have caught InvalidOrderDataException");
        }
        assertTrue(service.validateArea(new BigDecimal("101")), "Area should be greater than 100.");
        assertTrue(service.validateArea(new BigDecimal("100")), "Area should be greater than or at least 100.");
    }
}
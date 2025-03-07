package com.yj.flooringmastery.service;

import com.yj.flooringmastery.dao.PersistenceException;
import com.yj.flooringmastery.model.Order;
import com.yj.flooringmastery.model.Product;
import com.yj.flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderServiceLayer {
    /**
     * Adds the given Order to the order file by calling the underlying DAO object
     * throws persistence Exception if cannot read or write to the file
     *
     * @param date  date that the order will be placed
     * @param order      order to be added to the file
     */
    void addOrder(LocalDate date, Order order) throws
            PersistenceException;

    /**
     * Edits from the correct orders file the order associated
     * with the given order number by calling the underlying order DAO
     * Throws persistence Exception if cannot load or write to the file
     *
     * @param date date of order to be edited
     * @param order new order object that has been edited
     */
    void editOrder(LocalDate date, Order order) throws
            PersistenceException;

    /**
     * Gets the values of the new edited order and calculates the rest of the order information
     * based on those values
     * @param orderNum number of order to edit
     * @param oldOrder old order object before it was edited
     * @param date date of the order to be edited
     * @param name new name information user entered
     * @param state new state information user entered
     * @param productType new product type information user entered
     * @param area new area information user entered
     * @return order object representing the edited order with all the changed values
     * @throws PersistenceException if cannot load or write the new information
     */
    Order editOrderCalc(int orderNum, Order oldOrder, LocalDate date, String name, String state, String productType, String area) throws PersistenceException;

    /**
     * Removes from the correct orders file the order associated
     * with the given order number by calling the underlying order DAO
     * Throws persistence Exception if cannot load or write to the file
     *
     * @param date date of order to be removed
     * @param orderNum number of order to be removed
     */
    void removeOrder(int orderNum, LocalDate date) throws PersistenceException;

    /**
     * Retrieves order based on order number and date from user using order DAO object
     * @param orderNum number of order to retrieve
     * @param date date of order to retrieve
     * @return returns order retrieved by function based on order number and date
     * @throws PersistenceException throws error if cannot load from the file of orders
     */
    Order getOrder(int orderNum, LocalDate date) throws PersistenceException;

    /**
     * Returns list of all orders on a given date
     * @param date date of orders that user wants to retrieve
     * @return list of orders retrieved
     * @throws PersistenceException throws exception if cannot load form the given date file of orders
     */
    List<Order> getAllOrders(LocalDate date) throws PersistenceException;

    /**
     * Retrieves list of all products from the products file in memory
     * @return returns List of products retrieved
     * @throws PersistenceException throws exception if products file is not found
     */
    List<Product> getAllProducts() throws PersistenceException;

    /**
     * Retrieves list of all taxes from the products file in memory
     * @return returns List of Taxes retrieved
     * @throws PersistenceException throws exception if tax file is not found
     */
    List<Tax> getAllTaxes() throws PersistenceException;

    /**
     * validates entered date to make sure it is greater than the current date
     * or in other words, in the future
     * @param date date that function validates
     * @return true or false depending on whether date is valid
     */
    boolean validateDate(LocalDate date);

    /**
     * validates name to make sure it is not blank and does not contain special characters other than . and ,
     * @param name name user entered
     * @return true or false depending on whether name is valid
     */
    boolean validateName(String name);

    /**
     * validates whether given state exists in the tax folder of given states
     * checks whether entered state is valid even when abbreviation is entered by user
     * @param state state entered by user
     * @return true or false depending on whether state is valid
     */
    boolean validateState(String state);

    /**
     * validates whether given product type exists in the products folder of given products
     * @param productType type entered by user
     * @return true or false depending on whether product is valid
     */
    boolean validateProductType(String productType);

    /**
     * validates whether entered area is at least 100.00
     * @param area area entered by user
     * @return true or false depending on whether area is valid
     */
    boolean validateArea(BigDecimal area);

    /**
     * calculates order values based on changed params entered by user and already validated
     * @param orderNum order number of new order to be made with changed values
     * @param name name of new order to be made with changed values
     * @param state state of new order to be made with changed values
     * @param productType product type of new order to be made with changed values
     * @param area area of new order to be made with changed values
     * @return new order that has all the other fields of the order calculated based
     * on inputted new values that are allowed to change
     */
    Order calculateOrderValues(int orderNum, String name, String state, String productType, BigDecimal area);

    /**
     * gets the current order number from the order number file in memory in order
     * to calculate which order number each new created order receives
     * @return the order number in memory
     * @throws PersistenceException throws error if it cannot access the file in memory with the order number
     */
    int getOrderNumber() throws PersistenceException;

    /**
     * Adds one to the order number and updates it in memory
     * this allows new orders to constantly have the correct order number when they
     * are being created
     * @throws PersistenceException throws error if order number file cannot be read from or written to
     */
    void addToOrderNumber() throws PersistenceException;
}

package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDAO {

    /**
     * Adds the given Order to the order file and associates it with the given
     * order number. If there is already an order associated with the given
     * order number it will return that order object, otherwise it will
     * return null.
     *
     * @param date  date that the order will be placed
     * @param order      order to be added to the file
     * @return the Order object previously associated with the given
     * order number if it exists, null otherwise
     */
    Order addOrder(LocalDate date, Order order) throws PersistenceException;

    /**
     * Returns a List of all orders in the file of that date.
     *
     * @param date  date from which all orders will be pulled from
     * @return List containing all orders for that date.
     */
    List<Order> getAllOrders(LocalDate date) throws PersistenceException;

    /**
     * Edits from the correct orders file the order associated
     * with the given order number.
     * Returns the order object that is being edited or null if
     * there is no order associated with the given order number
     *
     * @param date date of order to be edited
     * @param order new order object that has been edited
     * @return order object that was editted or null if no order
     * was associated with the given order number
     */
    Order editOrder(LocalDate date, Order order) throws PersistenceException;

    /**
     * Removes from the correct date file the order associated with the given order number.
     * Returns the order object that is being removed or null if
     * there is no order associated with the given number and date
     *
     * @param orderNum order number to be removed
     * @param date date that order to be removed is coming from
     * @return Order object that was removed or null if no order
     * was associated with the given order number and date
     */
    Order removeOrder(int orderNum, LocalDate date) throws PersistenceException;

    /**
     * Returns from the correct date file the order associated with the given order number.
     * Returns the order object that is being retrieved or null if
     * there is no order associated with the given number and date
     *
     * @param orderNumber order number to be retrieved
     * @param date date that order to be retrieved is coming from
     * @return Order object that was retrieved or null if no order
     * was associated with the given order number and date
     */
    Order getOrder(int orderNumber, LocalDate date) throws PersistenceException;
}

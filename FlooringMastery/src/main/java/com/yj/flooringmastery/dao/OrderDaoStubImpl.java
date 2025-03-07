package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Order;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoStubImpl implements OrderDAO{
    public Order onlyOrder;
    public LocalDate onlyDate;

    public OrderDaoStubImpl() {
        onlyOrder = new Order(1, "John Doe", "Tile", new BigDecimal("102"));
        onlyOrder.setState("TX");
        onlyOrder.setLaborCostPerSquareFoot(new BigDecimal("25"));
        onlyOrder.setTax(new BigDecimal("25"));
        onlyOrder.setCostPerSquareFoot(new BigDecimal("25"));
        onlyOrder.setTaxRate(new BigDecimal("25"));
        onlyOrder.setTotal(new BigDecimal("25"));
        onlyOrder.setMaterialCost(new BigDecimal("25"));
        onlyOrder.setLaborCost(new BigDecimal("25"));
        onlyDate = LocalDate.parse("2025-03-06");
    }

    @Override
    public Order addOrder(LocalDate date, Order order) throws PersistenceException {
        if(order.getOrderNumber() == onlyOrder.getOrderNumber() && date.isEqual(onlyDate)) {
            return onlyOrder;
        }
        return null;
    }

    @Override
    public List<Order> getAllOrders(LocalDate date) throws PersistenceException {
        List<Order> orders = new ArrayList<>();
        orders.add(onlyOrder);
        return orders;
    }

    @Override
    public Order editOrder(LocalDate date, Order order) throws PersistenceException {
        if(order.getOrderNumber() == onlyOrder.getOrderNumber() && date.isEqual((ChronoLocalDate) onlyDate)) {
            return onlyOrder;
        }
        return null;
    }

    @Override
    public Order removeOrder(int orderNum, LocalDate date) throws PersistenceException {
        if(orderNum == onlyOrder.getOrderNumber() && date.isEqual((ChronoLocalDate) onlyDate)) {
            return onlyOrder;
        }
        return null;
    }

    @Override
    public Order getOrder(int orderNumber, LocalDate date) throws PersistenceException {
        if(orderNumber == onlyOrder.getOrderNumber() && date.isEqual((ChronoLocalDate) onlyDate)) {
            return onlyOrder;
        }
        return null;
    }
}

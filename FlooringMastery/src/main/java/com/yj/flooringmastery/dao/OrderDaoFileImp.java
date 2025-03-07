package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Order;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OrderDaoFileImp implements OrderDAO{

    private Map<Integer, Order> orders = new HashMap<>();
    private String orderFile;
    private static final String DELIMITER = "::";

    public OrderDaoFileImp() {
    }

    public OrderDaoFileImp(String orderFile){
        this.orderFile = orderFile;
    }

    @Override
    public Order addOrder(LocalDate date, Order order) throws PersistenceException {
        if(dateFileExists(date)) {
            loadOrders(date);
        }
        else {  //if date file does not already exist wipe the map to represent a new date
            orders = new HashMap<>();
        }
        Order newOrder = orders.put(order.getOrderNumber(), order);
        writeOrders(date);
        return newOrder;
    }

    @Override
    public List<Order> getAllOrders(LocalDate date) throws PersistenceException {
        loadOrders(date);

        return orders.values().stream()
                .toList();
    }

    @Override
    public Order editOrder(LocalDate date, Order order) throws PersistenceException {
        loadOrders(date);
        Order editedOrder = orders.put(order.getOrderNumber(), order);
        writeOrders(date);
        return editedOrder;
    }

    @Override
    public Order removeOrder(int orderNum, LocalDate date) throws PersistenceException {
        loadOrders(date);
        Order removedOrder = orders.remove(orderNum);
        writeOrders(date);
        return removedOrder;
    }

    @Override
    public Order getOrder(int orderNumber, LocalDate date) throws PersistenceException {
        loadOrders(date);
        return orders.get(orderNumber);
    }

    /*
    Takes in parameter of type date
    reads all the information from the corresponding orders file
    initializes the orders map with data from that file
    returns nothing
     */
    private void loadOrders(LocalDate date){
        Scanner scanner;
        orders = new HashMap<>(); //resets any orders previously in map from other dates

        String dateFormatted = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        orderFile = "Orders_" + dateFormatted + ".txt";

        //tries to open scanner to read in the date file for the date entered, throws exception if can't
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(orderFile)));
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Could not load data.", e);
        }

        String currentLine; //discards header line
        Order currentOrder;

        //discards header line if exists
        if(scanner.hasNextLine()){
            scanner.nextLine();
        }

        //reads each line of the file in order to get order information and store it in map
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentOrder = unMarshallOrder(currentLine);

            orders.put(currentOrder.getOrderNumber(), currentOrder);
        }

        scanner.close();
    }

    /*
    Takes in parameter of type date
    writes all the orders currently in the hashmap to an orders file
    returns nothing
     */
    private void writeOrders(LocalDate date) {
        String headerText = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,"
                + "CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
        PrintWriter out;

        String dateFormatted = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        orderFile = "Orders_" + dateFormatted + ".txt";

        try {
            out = new PrintWriter(new FileWriter(orderFile));
        } catch (IOException e) {
            throw new PersistenceException("Could not save order data.", e);
        }

        out.println(headerText);
        out.flush();

        String orderText;

        //goes through all the orders in the list and prints to the file
        for (Order currentOrder : orders.values()) {
            orderText = marshallOrder(currentOrder);
            out.println(orderText);
            out.flush();
        }

        out.close();
    }

    /*
    Takes in parameter of type string and returns that string in an Order object
     */
    private Order unMarshallOrder(String orderText) {
        String[] orderTokens = orderText.split(DELIMITER);

        int orderNumber = Integer.parseInt(orderTokens[0]);
        String customerName = orderTokens[1];
        String state = orderTokens[2];
        BigDecimal taxRate = new BigDecimal(orderTokens[3]);
        String productType = orderTokens[4];
        BigDecimal area = new BigDecimal(orderTokens[5]);
        BigDecimal costPerSqFt = new BigDecimal(orderTokens[6]);
        BigDecimal laborCostPerSqFt = new BigDecimal(orderTokens[7]);
        BigDecimal materialCost = new BigDecimal(orderTokens[8]);
        BigDecimal laborCost = new BigDecimal(orderTokens[9]);
        BigDecimal tax = new BigDecimal(orderTokens[10]);
        BigDecimal total = new BigDecimal(orderTokens[11]);

        Order order = new Order(orderNumber, customerName, productType, area);
        order.setState(state);
        order.setTaxRate(taxRate);
        order.setCostPerSquareFoot(costPerSqFt);
        order.setLaborCostPerSquareFoot(laborCostPerSqFt);
        order.setMaterialCost(materialCost);
        order.setLaborCost(laborCost);
        order.setTax(tax);
        order.setTotal(total);

        return order;
    }

    /*
    Takes in parameter of type Order and returns that order in a String object
     */
    private String marshallOrder(Order order) {
        String orderText = order.getOrderNumber() + DELIMITER;
        orderText += order.getCustomerName() + DELIMITER;
        orderText += order.getState() + DELIMITER;
        orderText += order.getTaxRate() + DELIMITER;
        orderText += order.getProductType() + DELIMITER;
        orderText += order.getArea() + DELIMITER;
        orderText += order.getCostPerSquareFoot() + DELIMITER;
        orderText += order.getLaborCostPerSquareFoot() + DELIMITER;
        orderText += order.getMaterialCost() + DELIMITER;
        orderText += order.getLaborCost() + DELIMITER;
        orderText += order.getTax() + DELIMITER;
        orderText += order.getTotal() + DELIMITER;

        return orderText;
    }

    /*
    Checks when adding orders if an order file already exists for that date
    takes in date value and checks if file exits
    returns if exists or not
     */
    private boolean dateFileExists(LocalDate date) {
        Scanner scanner;

        String dateFormatted = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        orderFile = "Orders_" + dateFormatted + ".txt";

        //tries to open scanner to read in the date file for the date entered, throws exception if can't
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(orderFile)));
        } catch (FileNotFoundException e) {
            return false;
        }

        return true;
    }
}

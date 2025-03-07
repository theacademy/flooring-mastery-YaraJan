package com.yj.flooringmastery.service;

import com.yj.flooringmastery.dao.OrderDAO;
import com.yj.flooringmastery.dao.PersistenceException;
import com.yj.flooringmastery.dao.ProductDAO;
import com.yj.flooringmastery.dao.TaxDAO;
import com.yj.flooringmastery.model.Order;
import com.yj.flooringmastery.model.Product;
import com.yj.flooringmastery.model.Tax;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class OrderServiceLayerImpl implements OrderServiceLayer{

    private OrderDAO orderDao;
    private TaxDAO taxDao;
    private ProductDAO productDao;
    private final String ORDER_NUMBER_FILE;

    public OrderServiceLayerImpl(OrderDAO orderDao, TaxDAO taxDao, ProductDAO productDao) {
        this.orderDao = orderDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
        this.ORDER_NUMBER_FILE = "OrderNumber.txt";
    }

    public OrderServiceLayerImpl(OrderDAO orderDao, TaxDAO taxDao, ProductDAO productDao, String orderNumFile) {
        this.orderDao = orderDao;
        this.taxDao = taxDao;
        this.productDao = productDao;
        this.ORDER_NUMBER_FILE = orderNumFile;
    }

    @Override
    public void addOrder(LocalDate date, Order order) throws PersistenceException {
        orderDao.addOrder(date, order);
    }

    @Override
    public void editOrder(LocalDate date, Order order) throws PersistenceException {
        orderDao.editOrder(date, order);
    }


    @Override
    public Order editOrderCalc(int orderNum, Order oldOrder, LocalDate date, String name, String state, String productType, String area) throws PersistenceException {
        boolean needRecalculate = false;

        //create deep copy of old order
        Order newOrder = new Order(orderNum, oldOrder.getCustomerName(), oldOrder.getProductType(), oldOrder.getArea());
        newOrder.setTotal(oldOrder.getTotal());
        newOrder.setTax(oldOrder.getTax());
        newOrder.setTaxRate(oldOrder.getTaxRate());
        newOrder.setLaborCost(oldOrder.getLaborCost());
        newOrder.setMaterialCost(oldOrder.getMaterialCost());
        newOrder.setCostPerSquareFoot(oldOrder.getCostPerSquareFoot());
        newOrder.setLaborCostPerSquareFoot(oldOrder.getLaborCostPerSquareFoot());
        newOrder.setState(oldOrder.getState());

        if(!name.isEmpty()){
            newOrder.setCustomerName(name);
        }

        if(!state.isEmpty()) {
            newOrder.setState(state);
            needRecalculate = true;
        }
        if(!productType.isEmpty()) {
            newOrder.setProductType(productType);
            needRecalculate = true;
        }
        if(!area.isEmpty()) {
            newOrder.setArea(new BigDecimal(area));
            needRecalculate = true;
        }

        if(needRecalculate) {
            return calculateOrderValues(orderNum, newOrder.getCustomerName(), newOrder.getState(), newOrder.getProductType(), newOrder.getArea());
        }

        return newOrder;
    }

    @Override
    public void removeOrder(int orderNum, LocalDate date) throws PersistenceException {
        orderDao.removeOrder(orderNum, date);
    }

    @Override
    public Order getOrder(int orderNum, LocalDate date) throws PersistenceException {
        Order order = orderDao.getOrder(orderNum, date);
        if(order == null) {
            throw new NoSuchOrderException("There is no order with that order number. ");
        }
        return order;
    }

    @Override
    public List<Order> getAllOrders(LocalDate date) throws PersistenceException{
        return orderDao.getAllOrders(date);
    }

    @Override
    public List<Product> getAllProducts() throws PersistenceException {
        return productDao.getAllProducts();
    }

    @Override
    public List<Tax> getAllTaxes() throws PersistenceException {
        return taxDao.getAllTaxes();
    }

    @Override
    public Order calculateOrderValues(int orderNum, String name, String state, String productType, BigDecimal area) {
        Order newOrder = new Order(orderNum, name, productType, area);

        //get Tax information for state and tax rate
        List<Tax> taxes = getAllTaxes();
        Tax tax = taxDao.getTaxByState(state);
        //get state in case user enters abbreviation
        if(tax == null){
            for(Tax t : taxes) {
                if(t.getStateAbbreviation().equalsIgnoreCase(state)){
                    state = t.getStateAbbreviation();
                    tax = t;
                    break;
                }
            }
        }
        state = tax.getStateAbbreviation();
        newOrder.setState(state);
        newOrder.setTaxRate(tax.getTaxRate());

        //get Product Information for cost and labor cost
        Product product = productDao.getProduct(productType.toLowerCase());
        newOrder.setCostPerSquareFoot(product.getCostPerSqFt());
        newOrder.setLaborCostPerSquareFoot(product.getLaborCostPerSqFt());

        //calculate material cost = Area * costPerSquareFoot
        BigDecimal materialCost = area.multiply(newOrder.getCostPerSquareFoot());
        materialCost = materialCost.setScale(2, RoundingMode.HALF_UP);
        newOrder.setMaterialCost(materialCost);

        //calculate Labor cost = Area * LaborCostPerSqFt
        BigDecimal laborCost = area.multiply(newOrder.getLaborCostPerSquareFoot());
        laborCost = laborCost.setScale(2, RoundingMode.HALF_UP);
        newOrder.setLaborCost(laborCost);

        //calculate tax = (material cost + labor cost) * (taxRate/100)
        BigDecimal taxCost = materialCost.add(laborCost);
        BigDecimal taxRate = newOrder.getTaxRate().divide(new BigDecimal("100"));
        taxCost = taxCost.multiply(taxRate);
        taxCost = taxCost.setScale(2, RoundingMode.HALF_UP);
        newOrder.setTax(taxCost);

        //calculate total = materialCost + laborCost + taxCost
        BigDecimal total = materialCost.add(laborCost);
        total = total.add(taxCost);
        total = total.setScale(2, RoundingMode.HALF_UP);
        newOrder.setTotal(total);

        return newOrder;
    }

    @Override
    public int getOrderNumber() throws PersistenceException {
        Scanner scanner;
        int orderNum;

        //tries to open file with order number inside
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(ORDER_NUMBER_FILE)));
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Could not load order number.");
        }

        //tries to grab order number from order file
        try {
            orderNum = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException | NoSuchElementException e) {
            throw new PersistenceException("Could not load order number.");
        }

        return orderNum;
    }

    @Override
    public void addToOrderNumber() throws PersistenceException {
        PrintWriter out;
        int orderNum = getOrderNumber() + 1;

        try {
            out = new PrintWriter((new FileWriter(ORDER_NUMBER_FILE)));
        } catch (IOException e) {
            throw new PersistenceException("Could not save order number.");
        }

        //prints current order number plus one to the file
        out.println(orderNum);
        out.flush();

        out.close();
    }

    public boolean validateDate(LocalDate date) {
        if(date.isAfter(LocalDate.now())) {
            return true;
        }
        throw new InvalidOrderDataException("That is not a valid date. Please enter a future date");
    }

    @Override
    public boolean validateName(String name) throws InvalidOrderDataException{
        if(name.isEmpty()){
            throw new InvalidOrderDataException("Not a valid name.");
        }

        if(!name.matches("^[a-zA-Z0-9., ]+$")) {    //allows characters, numbers, periods, commas, and whitespaces
            throw new InvalidOrderDataException("Not a valid name.");
        }

        return true;
    }

    @Override
    public boolean validateState(String state) throws InvalidOrderDataException {
        //If the user typed in the full name of the state instead of the abbreviation
        if(state.length() > 2) {
            if (taxDao.getTaxByState(state.toLowerCase()) != null) {
                return true;
            }
            throw new InvalidOrderDataException("State not found.");
        }

        //Check the abbreviations
        List<Tax> allTaxes = taxDao.getAllTaxes();
        List<Tax> retrievedTax = allTaxes.stream()
                .filter((t) -> t.getStateAbbreviation().equals(state.toUpperCase()))
                .toList(); //filters all tax objects to get the state abbreviation and test if equals entered state
        if(retrievedTax.isEmpty()) {
            throw new InvalidOrderDataException("State not found.");
        }

        return true;
    }

    @Override
    public boolean validateProductType(String productType) {
        List<Product> products = productDao.getAllProducts();

        //filters product to get inputed type
        products = products.stream()
                .filter((p) -> p.getProductType().equalsIgnoreCase(productType))
                .toList();

        if(products.isEmpty()) {
            throw new InvalidOrderDataException("That is not a valid product type.");
        }

        return true;
    }

    @Override
    public boolean validateArea(BigDecimal area) {
        if(area.compareTo(new BigDecimal("100")) < 0) {
            throw new InvalidOrderDataException("Please enter a positive decimal 100 sq ft or over.");
        }
        return true;
    }
}

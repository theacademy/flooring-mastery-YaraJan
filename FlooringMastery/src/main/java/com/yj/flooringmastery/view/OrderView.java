package com.yj.flooringmastery.view;

import com.yj.flooringmastery.model.Order;
import com.yj.flooringmastery.model.Product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderView {

    private UserIO io;

    public OrderView(){
    }

    public OrderView(UserIO io) {
        this.io = io;
    }

    public int displayMenuGetSelection(){
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Quit");

        return io.readInt("Please select from the"
                + " above choices.", 1, 6);
    }

    public void displayExitBanner() {
        io.print("Good Bye!!!");
    }

    public void displayUnknownCommandBanner() {
        io.print("Unknown Command!!!");
    }

    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
    }

    public void displayOrderList(List<Order> orders) {
        for(Order order : orders) {
            System.out.print(order.getOrderNumber() + ". ");
            System.out.println(order.getCustomerName());
            System.out.println("State: " + order.getState());
            System.out.println("Tax Rate: " + order.getTaxRate());
            System.out.println("Product Type: " + order.getProductType());
            System.out.println("Area: " + order.getArea());
            System.out.println("Cost Per Square Foot: " + order.getCostPerSquareFoot());
            System.out.println("Labor Cost Per Square Foot: " + order.getLaborCostPerSquareFoot());
            System.out.println("Material Cost: " + order.getMaterialCost());
            System.out.println("Labor Cost: " + order.getLaborCost());
            System.out.println("Tax: " + order.getTax());
            System.out.println("Total: " + order.getTotal());
            System.out.println();
        }
    }

    public void displayOrdersBanner() {
        io.print("=== Display All Orders ===");
    }

    public LocalDate getDate() {
        return io.readLocalDate("Please enter date: ");
    }

    public String getName() {
        return io.readString("Please enter name for order:");
    }

    public String getState() {
        return io.readString("Please enter state for order:");
    }

    public void displayAddOrderBanner() {
        io.print("=== Add Order ===");
    }

    public void displayProductList(List<Product> products) {
        for(Product p : products) {
            System.out.print("Type: " + p.getProductType() + " ");
            System.out.print("Cost Per Sq Ft: " + p.getCostPerSqFt() + " ");
            System.out.println("Labor Cost Per Sq Ft: " + p.getLaborCostPerSqFt() + " ");
        }
    }

    public String getProductChoice() {
        return io.readString("Please choose a product from above: ");
    }

    public BigDecimal getArea() {
        return io.readBigDecimal("Please enter a desired area for the order: ");
    }

    public void displayOrderAddSuccess() {
        io.print("=== Order Added Successfully ===");
    }

    public void displayEditOrderBanner() {
        io.print("=== Edit Order ===");
    }

    public void displayOrderEditSuccess() {
        io.print("=== Order Edited Successfully ===");
    }

    public void displayOrderRemoveSuccess() {
        io.print("=== Order Removed Successfully ===");
    }

    public int getOrderNum() {
        return io.readInt("Please enter order number: ");
    }

    public void displaySummaryBanner() {
        io.print("=== Order Summary ===");
    }

    public void displayOrder(Order order) {
        System.out.print(order.getOrderNumber() + ". ");
        System.out.println(order.getCustomerName());
        System.out.println("State: " + order.getState());
        System.out.println("Tax Rate: " + order.getTaxRate());
        System.out.println("Product Type: " + order.getProductType());
        System.out.println("Area: " + order.getArea());
        System.out.println("Cost Per Square Foot: " + order.getCostPerSquareFoot());
        System.out.println("Labor Cost Per Square Foot: " + order.getLaborCostPerSquareFoot());
        System.out.println("Material Cost: " + order.getMaterialCost());
        System.out.println("Labor Cost: " + order.getLaborCost());
        System.out.println("Tax: " + order.getTax());
        System.out.println("Total: " + order.getTotal());
        System.out.println();
    }

    public boolean getConfirmationAdd() {
        return io.readYesNo("Are you sure you want to place this order? (Y/N)");
    }

    public boolean getConfirmationEdit() {
        return io.readYesNo("Are you sure you want to edit this order? (Y/N)");
    }

    public boolean getConfirmationRemove() {
        return io.readYesNo("Are you sure you want to remove this order? (Y/N)");
    }

    public String getEditName(String customerName) {
        return io.readString("Enter customer name (" + customerName + ") :");
    }

    public String getEditState(String state) {
        return io.readString("Enter state (" + state + ") :");
    }

    public String getEditProductChoice(String productType, List<Product> products) {
        displayProductList(products);
        return io.readString("Enter product type (" + productType + "):");
    }

    public String getEditArea(BigDecimal area) {
        return io.readString("Enter area (" + area + "):");
    }

    public void displayRemoveOrderBanner() {
        io.print("=== Remove Order ===");
    }
}

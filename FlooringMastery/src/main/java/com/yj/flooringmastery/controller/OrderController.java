package com.yj.flooringmastery.controller;

import com.yj.flooringmastery.dao.PersistenceException;
import com.yj.flooringmastery.model.Order;
import com.yj.flooringmastery.model.Product;
import com.yj.flooringmastery.service.InvalidOrderDataException;
import com.yj.flooringmastery.service.NoSuchOrderException;
import com.yj.flooringmastery.service.OrderServiceLayer;
import com.yj.flooringmastery.view.OrderView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderController {

    private OrderView view;
    private OrderServiceLayer service;

    public OrderController() {
    }

    public OrderController(OrderServiceLayer s, OrderView v) {
        view = v;
        service = s;
    }

    //Runs the code by showing the menu until user decides to quit
    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;

        while (keepGoing) {
            try {
                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrdersByDate();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            } catch (PersistenceException | NoSuchOrderException e) {
                view.displayErrorMessage(e.getMessage());
            }
        }
        exitMessage();
    }

    private int getMenuSelection() {
        return view.displayMenuGetSelection();
    }

    private void displayOrdersByDate() {
        LocalDate date = view.getDate();
        List<Order> orders = service.getAllOrders(date);
        view.displayOrdersBanner();
        view.displayOrderList(orders);
    }

    private void addOrder() {
        boolean isValid = false;
        LocalDate date = LocalDate.now();
        String name = "";
        String state = "";
        String productType = "";
        BigDecimal area = new BigDecimal("0");
        int orderNumber;

        view.displayAddOrderBanner();
        //gets and validates date
        do {
            try {
                date = view.getDate();
                isValid = service.validateDate(date);
            } catch (InvalidOrderDataException e) {
                view.displayErrorMessage(e.getMessage());
            }
        } while (!isValid);
        isValid = false;

        //gets and validates name for order
        do {
            try {
                name = view.getName();
                isValid = service.validateName(name);
            } catch (InvalidOrderDataException e) {
                view.displayErrorMessage(e.getMessage());
            }
        } while (!isValid);
        isValid = false;

        //gets and validates state for order
        do {
            try {
                state = view.getState();
                isValid = service.validateState(state);
            } catch (InvalidOrderDataException e) {
                view.displayErrorMessage(e.getMessage());
            }
        } while (!isValid);
        isValid = false;

        //gets list of available products
        List<Product> products = service.getAllProducts();
        do {
            view.displayProductList(products);
            productType = view.getProductChoice();
            try {
                isValid = service.validateProductType(productType);
            } catch (InvalidOrderDataException e) {
                view.displayErrorMessage(e.getMessage());
            }
        } while(!isValid);
        isValid = false;

        //get and validate area for order
        do {
            try {
                area = view.getArea();
                isValid = service.validateArea(area);
            } catch (InvalidOrderDataException e) {
                view.displayErrorMessage(e.getMessage());
            }
        } while (!isValid);

        //Get order number
        orderNumber = service.getOrderNumber();

        //calculate the rest of the order values and return it in new Order object
        Order newOrder = service.calculateOrderValues(orderNumber, name, state, productType, area);

        //confirm with user
        view.displaySummaryBanner();
        view.displayOrder(newOrder);
        boolean isConfirmed = view.getConfirmationAdd();

        //add the order to memory if user confirms
        if (isConfirmed) {
            service.addToOrderNumber();
            service.addOrder(date, newOrder);
            view.displayOrderAddSuccess();
        }

    }

    private void editOrder() {
        LocalDate date = LocalDate.now();
        int orderNum = 0;
        boolean isValid = false;
        String name = "";
        String state = "";
        String productType = "";
        String area = "";

        view.displayEditOrderBanner();
        //gets and validates date
        do {
            try {
                date = view.getDate();
                isValid = service.validateDate(date);
            } catch (InvalidOrderDataException e) {
                view.displayErrorMessage(e.getMessage());
            }
        } while (!isValid);
        isValid = false;

        orderNum = view.getOrderNum();
        Order order = service.getOrder(orderNum, date);

        do {
            name = view.getEditName(order.getCustomerName());
            if(!name.isEmpty()) {
                try {
                    isValid = service.validateName(name);
                    continue;
                } catch (InvalidOrderDataException e) {
                    view.displayErrorMessage(e.getMessage());
                }
            }
            isValid = true;
        } while(!isValid);
        isValid = false;

        do {
            state = view.getEditState(order.getState());
            if(!state.isEmpty()) {
                try {
                    isValid = service.validateState(state);
                    continue;
                } catch (InvalidOrderDataException e) {
                    view.displayErrorMessage(e.getMessage());
                }
            }
            isValid = true;
        }while(!isValid);
        isValid = false;

        do {
            List<Product> products = service.getAllProducts();
            productType = view.getEditProductChoice(order.getProductType(), products);
            if(products.isEmpty()) {
                try {
                    isValid = service.validateProductType(productType);
                    continue;
                } catch (InvalidOrderDataException e) {
                    view.displayErrorMessage(e.getMessage());
                }
            }
            isValid = true;
        }while(!isValid);
        isValid = false;

        do {
            area = view.getEditArea(order.getArea());
            if(!area.isEmpty()) {
                BigDecimal areaTest;
                try {
                    areaTest = new BigDecimal(area);
                    isValid = service.validateArea(areaTest);
                    continue;
                } catch (NumberFormatException e) {
                    view.displayErrorMessage("Please either enter a valid decimal or press enter.");
                    continue;
                } catch (InvalidOrderDataException e) {
                    view.displayErrorMessage(e.getMessage());
                }
            }
            isValid = true;
        }while(!isValid);

        Order editedOrder = service.editOrderCalc(orderNum, order, date, name, state, productType, area);
        view.displaySummaryBanner();
        view.displayOrder(editedOrder);
        boolean isConfirmed = view.getConfirmationEdit();

        if(isConfirmed) {
            service.editOrder(date, editedOrder);
            view.displayOrderEditSuccess();
        }

    }

    private void removeOrder() {
        LocalDate date = LocalDate.now();
        boolean isValid = false;

        view.displayRemoveOrderBanner();

        //gets and validates date
        do {
            try {
                date = view.getDate();
                isValid = service.validateDate(date);
            } catch (InvalidOrderDataException e) {
                view.displayErrorMessage(e.getMessage());
            }
        } while (!isValid);

        int orderNum = view.getOrderNum();
        Order removeOrder = service.getOrder(orderNum, date);

        view.displaySummaryBanner();
        view.displayOrder(removeOrder);

        boolean isConfirmed = view.getConfirmationRemove();

        if(isConfirmed) {
            service.removeOrder(orderNum, date);
            view.displayOrderRemoveSuccess();
        }
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}

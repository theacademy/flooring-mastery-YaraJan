package com.yj.flooringmastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserIO {

    //prints message to console
    void print(String msg);

    //prints a prompt to console and returns the user input in valid int format
    int readInt(String prompt);

    //prints a prompt to console and returns the user input in valid int format between min and max
    int readInt(String prompt, int min, int max);

    //prints a prompt to console and returns the user input in valid double format
    double readDouble(String prompt);

    //prints a prompt to console and returns the user input in valid double format between min and max
    double readDouble(String prompt, double min, double max);

    //prints a prompt to console and returns the user input in valid Big Decimal format
    BigDecimal readBigDecimal(String prompt);

    //prints a prompt to console and returns the user input in valid Big Decimal format between min and max
    BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max);

    //prints a prompt to console and returns the user input in valid String format
    String readString(String prompt);

    //prints a prompt to console and returns the user input in valid LocalDate format
    LocalDate readLocalDate(String prompt);

    //prints a prompt to console and returns the user input in valid Boolean format
    Boolean readYesNo(String prompt);
}

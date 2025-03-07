package com.yj.flooringmastery.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {
    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        String response;
        Scanner scr = new Scanner(System.in);

        System.out.println(prompt);
        response = scr.nextLine();

        return response;
    }

    @Override
    public int readInt(String prompt) {
        int userNum = 0;
        boolean hasError;
        Scanner scr = new Scanner(System.in);

        //runs until user inputs valid data to create int
        do {
            System.out.println(prompt);
            try {
                userNum = Integer.parseInt(scr.nextLine());
                hasError = false;
            } catch (NumberFormatException e) {
                hasError = true;
                System.out.println("Please enter a number.");
            }
        } while (hasError);

        return userNum;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int userNum = 0;
        boolean hasError;
        Scanner scr = new Scanner(System.in);

        //runs until user inputs valid data to create int
        do {
            System.out.println(prompt);
            try {
                userNum = Integer.parseInt(scr.nextLine());
                hasError = false;
            } catch (NumberFormatException e) {
                hasError = true;
                System.out.println("Please enter a number.");
            }
        } while (hasError || userNum < min || userNum > max);

        return userNum;
    }

    @Override
    public double readDouble(String prompt) {
        double userNum = 0;
        boolean hasError;

        Scanner scr = new Scanner(System.in);

        //runs until user inputs valid data to create double
        do {
            try {
                System.out.println(prompt);
                userNum = Double.parseDouble(scr.nextLine());
                hasError = false;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid double value.");
                hasError = true;
            }
        } while (hasError);

        return userNum;
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double userNum = 0;
        boolean hasError;

        Scanner scr = new Scanner(System.in);

        //runs until user inputs valid data to create double between min and max
        do {
            try {
                System.out.println(prompt);
                userNum = Double.parseDouble(scr.nextLine());
                hasError = false;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid double value.");
                hasError = true;
            }
        } while (hasError || userNum < min || userNum > max);

        return userNum;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        BigDecimal userBigDecimal = new BigDecimal("0");
        Scanner scr = new Scanner(System.in);
        boolean hasError;

        //runs until user inputs valid data to create Big Decimal object
        do {
            try {
                System.out.println(prompt);
                userBigDecimal = new BigDecimal(scr.nextLine());
                hasError = false;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid Big Decimal value.");
                hasError = true;
            }
        } while (hasError);

        return userBigDecimal;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt, BigDecimal min, BigDecimal max) {
        BigDecimal userBigDecimal = new BigDecimal("0");
        Scanner scr = new Scanner(System.in);
        boolean hasError;

        //runs until user inputs valid data to create Big Decimal object between min and max
        do {
            try {
                System.out.println(prompt);
                userBigDecimal = new BigDecimal(scr.nextLine());
                hasError = false;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid Big Decimal value.");
                hasError = true;
            }
        } while (hasError || userBigDecimal.compareTo(min) < 0 || userBigDecimal.compareTo(max) > 0);

        return userBigDecimal;
    }

    public LocalDate readLocalDate(String prompt) {
        LocalDate userDate = LocalDate.now();
        boolean hasError;
        Scanner scr = new Scanner(System.in);

        //runs until user inputs valid date in correct format
        do {
            try {
                System.out.println(prompt);
                userDate = LocalDate.parse(scr.nextLine());
                hasError = false;
            } catch (DateTimeParseException e) {
                System.out.println("Please enter a valid date is YYYY-MM-DD format.");
                hasError = true;
            }
        } while (hasError);

        return userDate;
    }

    @Override
    public Boolean readYesNo(String prompt) {
        boolean isYes = false;
        boolean hasError;
        Scanner scr = new Scanner(System.in);

        do {
            System.out.println(prompt);
            String userInput = scr.nextLine();
            if (userInput.equalsIgnoreCase("y")){
                hasError = false;
                isYes = true;
            }
            else if (userInput.equalsIgnoreCase("n")) {
                hasError = false;
            }
            else {
                System.out.println("Please only enter 'y' for yes and 'n' for no.");
                hasError = true;
            }
        } while (hasError);

        return isYes;
    }

}

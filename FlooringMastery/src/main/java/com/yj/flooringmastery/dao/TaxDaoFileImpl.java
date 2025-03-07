package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TaxDaoFileImpl implements TaxDAO{

    private Map<String, Tax> taxes = new HashMap<>();
    private final String TAX_FILE;
    public static final String DELIMITER = ",";

    public TaxDaoFileImpl() {
        TAX_FILE = "Taxes.txt";
    }

    public TaxDaoFileImpl(String taxFile){
        TAX_FILE = taxFile;
    }

    @Override
    public List<Tax> getAllTaxes() throws PersistenceException {
        loadTaxData();
        return taxes.values().stream().toList();
    }

    @Override
    public Tax getTaxByState(String state) throws PersistenceException {
        loadTaxData();
        return taxes.get(state);
    }

    private void loadTaxData() throws PersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAX_FILE)));
        } catch (FileNotFoundException e) {
            throw new PersistenceException("Could not load Tax data into memory.", e);
        }

        String currentLine;
        Tax currentTax;

        //skips past header text line
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        //loops through an unmarshalls all the text from the tax file
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTax = unmarshallTax(currentLine);

            taxes.put(currentTax.getStateName().toLowerCase(), currentTax);
        }
    }

    private Tax unmarshallTax(String taxText) {
        String[] taxTokens = taxText.split(DELIMITER);
        Tax newTax = new Tax();
        newTax.setStateAbbreviation(taxTokens[0]);
        newTax.setStateName(taxTokens[1].toLowerCase());
        newTax.setTaxRate(new BigDecimal(taxTokens[2]));

        return newTax;
    }
}

package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Tax;

import java.util.List;

public interface TaxDAO {

    /**
     * Returns a List of all tax objects in the file of taxes.
     *
     * @return List containing all taxes from file.
     */
    List<Tax> getAllTaxes() throws PersistenceException;

    /**
     * Returns a Tax object of the provided state.
     *
     * @param state  string describing the state trying to find tax information on, full name not abbreviation
     * @return Tax object of the provided state in
     */
    Tax getTaxByState(String state) throws PersistenceException;

}

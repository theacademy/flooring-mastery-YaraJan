package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaxDaoStubImpl implements TaxDAO{

    private Tax onlyTax;

    public TaxDaoStubImpl() {
        onlyTax = new Tax();
        onlyTax.setTaxRate(new BigDecimal("6"));
        onlyTax.setStateName("kentucky");
        onlyTax.setStateAbbreviation("KY");
    }

    @Override
    public List<Tax> getAllTaxes() throws PersistenceException {
        List<Tax> taxes = new ArrayList<>();
        taxes.add(onlyTax);
        return taxes;
    }

    @Override
    public Tax getTaxByState(String state) throws PersistenceException {
        if(state.equals(onlyTax.getStateName())) {
            return onlyTax;
        }
        return null;
    }
}

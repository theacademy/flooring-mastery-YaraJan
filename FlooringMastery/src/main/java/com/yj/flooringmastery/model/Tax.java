package com.yj.flooringmastery.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Tax {
    private String stateName;
    private String stateAbbreviation;
    private BigDecimal taxRate;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tax tax = (Tax) o;
        return Objects.equals(stateName, tax.stateName) && Objects.equals(stateAbbreviation, tax.stateAbbreviation) && Objects.equals(taxRate, tax.taxRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateName, stateAbbreviation, taxRate);
    }

    @Override
    public String toString() {
        return "Tax{" +
                "stateName='" + stateName + '\'' +
                ", stateAbbreviation='" + stateAbbreviation + '\'' +
                ", taxRate=" + taxRate +
                '}';
    }
}

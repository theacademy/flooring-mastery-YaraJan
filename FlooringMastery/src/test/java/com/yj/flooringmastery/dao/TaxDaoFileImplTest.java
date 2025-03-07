package com.yj.flooringmastery.dao;

import com.yj.flooringmastery.model.Tax;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaxDaoFileImplTest {

    private TaxDAO testDao;

    public TaxDaoFileImplTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        testDao = ctx.getBean("taxDao", TaxDAO.class);
    }

    @BeforeEach
    void setUp() throws Exception{
        PrintWriter out;
        String testFile = "testTax.txt";
        // Use the FileWriter to quickly blank and fill the file
        out = new PrintWriter(new FileWriter(testFile));
        out.println("State,StateName,TaxRate");
        out.flush();
        out.println("TX,Texas,4.45");
        out.flush();
        out.println("WA,Washington,9.25");
        out.flush();
        out.println("KY,Kentucky,6.00");
        out.flush();
        out.println("CA,Calfornia,25.00");
        out.flush();
        out.close();
    }

    @AfterEach
    void afterEach() throws Exception {
        File testFile = new File("testTax.txt");
        testFile.delete();
    }

    @Test
    void getAllTaxes() {
        List<Tax> allTaxes = testDao.getAllTaxes();

        assertNotNull(allTaxes, "The list of taxes must not null");
        assertEquals(4, allTaxes.size(),"List of taxes should have 4 taxes.");

        Tax checkTax = new Tax();
        checkTax.setStateName("texas");
        checkTax.setStateAbbreviation("TX");
        checkTax.setTaxRate(new BigDecimal("4.45"));
        assertTrue(allTaxes.contains(checkTax),
                "The list of taxes should include Texas tax.");

        checkTax.setStateName("alabama");
        assertFalse(allTaxes.contains(checkTax),
                "The list of taxes should not include Alabama.");
    }

    @Test
    void getTaxByState() {
        Tax retrievedTax = testDao.getTaxByState("texas");
        Tax fakeTax = testDao.getTaxByState("alabama");

        assertNotNull(retrievedTax, "checking that Texas does exist in tax file");
        assertNull(fakeTax, "checking that Alabama does not exist in tax file");

        assertEquals(retrievedTax.getStateName(), "texas", "Checking state name matches");
        assertEquals(retrievedTax.getStateAbbreviation(), "TX", "Checking state name abbreviation matches");
        assertEquals(retrievedTax.getTaxRate(), new BigDecimal("4.45"), "Checking tax rate matches");
    }
}
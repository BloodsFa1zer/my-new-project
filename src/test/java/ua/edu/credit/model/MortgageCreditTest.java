package ua.edu.credit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MortgageCreditTest {

    private Bank testBank;
    private MortgageCredit mortgageCredit;

    @BeforeEach
    void setUp() {
        testBank = new Bank("TestBank", "TB001", 4.5);
        mortgageCredit = new MortgageCredit("M001", testBank, new BigDecimal("500000"),
                new BigDecimal("12.5"), 240, true, false,
                new BigDecimal("600000"), new BigDecimal("100000"), "APARTMENT");
    }

    @Test
    void testMortgageCreditCreation() {
        assertEquals("M001", mortgageCredit.getId());
        assertEquals("MORTGAGE", mortgageCredit.getCreditType());
        assertEquals(new BigDecimal("500000"), mortgageCredit.getAmount());
        assertEquals(new BigDecimal("600000"), mortgageCredit.getPropertyValue());
        assertEquals(new BigDecimal("100000"), mortgageCredit.getDownPayment());
        assertEquals("APARTMENT", mortgageCredit.getPropertyType());
    }

    @Test
    void testMortgageCreditDefaultConstructor() {
        MortgageCredit credit = new MortgageCredit();
        assertEquals("MORTGAGE", credit.getCreditType());
        assertNull(credit.getPropertyValue());
    }

    @Test
    void testCalculateLoanToValue() {
        BigDecimal ltv = mortgageCredit.calculateLoanToValue();
        assertNotNull(ltv);
        assertTrue(ltv.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(ltv.compareTo(BigDecimal.valueOf(100)) < 0);
    }

    @Test
    void testCalculateLoanToValueWithZeroPropertyValue() {
        MortgageCredit credit = new MortgageCredit();
        credit.setPropertyValue(BigDecimal.ZERO);
        BigDecimal ltv = credit.calculateLoanToValue();
        assertEquals(BigDecimal.ZERO, ltv);
    }

    @Test
    void testCalculateLoanToValueWithNullPropertyValue() {
        MortgageCredit credit = new MortgageCredit();
        credit.setPropertyValue(null);
        BigDecimal ltv = credit.calculateLoanToValue();
        assertEquals(BigDecimal.ZERO, ltv);
    }

    @Test
    void testMortgageCreditSetters() {
        MortgageCredit credit = new MortgageCredit();
        credit.setPropertyValue(new BigDecimal("800000"));
        credit.setDownPayment(new BigDecimal("200000"));
        credit.setPropertyType("HOUSE");

        assertEquals(new BigDecimal("800000"), credit.getPropertyValue());
        assertEquals(new BigDecimal("200000"), credit.getDownPayment());
        assertEquals("HOUSE", credit.getPropertyType());
    }

    @Test
    void testLoanToValueCalculation() {
        BigDecimal propertyValue = new BigDecimal("1000000");
        BigDecimal loanAmount = new BigDecimal("800000");
        mortgageCredit.setPropertyValue(propertyValue);
        mortgageCredit.setAmount(loanAmount);

        BigDecimal ltv = mortgageCredit.calculateLoanToValue();
        assertEquals(new BigDecimal("80.0000"), ltv);
    }

    @Test
    void testMortgageCreditInheritance() {
        assertTrue(mortgageCredit instanceof Credit);
        assertNotNull(mortgageCredit.getBank());
        assertNotNull(mortgageCredit.getInterestRate());
    }

    @Test
    void testMortgageCreditWithDifferentPropertyTypes() {
        mortgageCredit.setPropertyType("HOUSE");
        assertEquals("HOUSE", mortgageCredit.getPropertyType());

        mortgageCredit.setPropertyType("APARTMENT");
        assertEquals("APARTMENT", mortgageCredit.getPropertyType());

        mortgageCredit.setPropertyType("COMMERCIAL");
        assertEquals("COMMERCIAL", mortgageCredit.getPropertyType());
    }
}


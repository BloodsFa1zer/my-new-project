package credit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CreditTest {

    private Bank testBank;
    private Credit credit;

    @BeforeEach
    void setUp() {
        testBank = new Bank("TestBank", "TB001", 4.5);
        credit = new Credit("CR001", testBank, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false);
    }

    @Test
    void testCreditCreation() {
        assertEquals("CR001", credit.getId());
        assertEquals(testBank, credit.getBank());
        assertEquals("CONSUMER", credit.getCreditType());
        assertEquals(new BigDecimal("100000"), credit.getAmount());
        assertEquals(new BigDecimal("15.0"), credit.getInterestRate());
        assertEquals(60, credit.getTermMonths());
        assertTrue(credit.isEarlyRepaymentAllowed());
        assertFalse(credit.isCreditLineIncreaseAllowed());
    }

    @Test
    void testCreditDefaultConstructor() {
        Credit credit = new Credit();
        assertNull(credit.getId());
        assertNull(credit.getBank());
    }

    @Test
    void testCreditSetters() {
        Credit credit = new Credit();
        credit.setId("CR002");
        credit.setBank(testBank);
        credit.setCreditType("MORTGAGE");
        credit.setAmount(new BigDecimal("500000"));
        credit.setInterestRate(new BigDecimal("12.0"));
        credit.setTermMonths(240);
        credit.setEarlyRepaymentAllowed(false);
        credit.setCreditLineIncreaseAllowed(true);

        assertEquals("CR002", credit.getId());
        assertEquals(testBank, credit.getBank());
        assertEquals("MORTGAGE", credit.getCreditType());
        assertEquals(new BigDecimal("500000"), credit.getAmount());
        assertEquals(new BigDecimal("12.0"), credit.getInterestRate());
        assertEquals(240, credit.getTermMonths());
        assertFalse(credit.isEarlyRepaymentAllowed());
        assertTrue(credit.isCreditLineIncreaseAllowed());
    }

    @Test
    void testCalculateMonthlyPayment() {
        BigDecimal monthlyPayment = credit.calculateMonthlyPayment();
        assertNotNull(monthlyPayment);
        assertTrue(monthlyPayment.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testCalculateTotalPayment() {
        BigDecimal totalPayment = credit.calculateTotalPayment();
        assertNotNull(totalPayment);
        assertTrue(totalPayment.compareTo(credit.getAmount()) > 0);
    }

    @Test
    void testStartDateInitialization() {
        assertNotNull(credit.getStartDate());
        assertEquals(LocalDate.now(), credit.getStartDate());
    }

    @Test
    void testStartDateSetter() {
        LocalDate customDate = LocalDate.of(2024, 1, 1);
        credit.setStartDate(customDate);
        assertEquals(customDate, credit.getStartDate());
    }

    @Test
    void testCreditEquality() {
        Credit credit1 = new Credit("CR001", testBank, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false);
        Credit credit2 = new Credit("CR001", testBank, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false);

        assertEquals(credit1, credit2);
    }

    @Test
    void testCreditInequality() {
        Credit credit1 = new Credit("CR001", testBank, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false);
        Credit credit2 = new Credit("CR002", testBank, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false);

        assertNotEquals(credit1, credit2);
    }

    @Test
    void testCreditHashCode() {
        Credit credit1 = new Credit("CR001", testBank, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false);
        Credit credit2 = new Credit("CR001", testBank, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false);

        assertEquals(credit1.hashCode(), credit2.hashCode());
    }

    @Test
    void testCreditToString() {
        String toString = credit.toString();
        assertTrue(toString.contains("CR001"));
        assertTrue(toString.contains("CONSUMER"));
    }

    @Test
    void testMonthlyPaymentLessThanTotal() {
        BigDecimal monthlyPayment = credit.calculateMonthlyPayment();
        BigDecimal totalPayment = credit.calculateTotalPayment();
        assertTrue(monthlyPayment.multiply(BigDecimal.valueOf(credit.getTermMonths()))
                .compareTo(totalPayment) >= 0);
    }

    @Test
    void testCreditWithZeroInterest() {
        Credit zeroInterestCredit = new Credit("CR003", testBank, "TEST", new BigDecimal("100000"),
                BigDecimal.ZERO, 60, true, false);
        BigDecimal monthlyPayment = zeroInterestCredit.calculateMonthlyPayment();
        assertTrue(monthlyPayment.compareTo(BigDecimal.ZERO) >= 0);
    }
}


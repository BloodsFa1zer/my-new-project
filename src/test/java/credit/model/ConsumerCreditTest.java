package credit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ConsumerCreditTest {

    private Bank testBank;
    private ConsumerCredit consumerCredit;

    @BeforeEach
    void setUp() {
        testBank = new Bank("TestBank", "TB001", 4.5);
        consumerCredit = new ConsumerCredit("C001", testBank, new BigDecimal("50000"),
                new BigDecimal("18.5"), 60, true, true,
                "HOME_RENOVATION", false, new BigDecimal("200000"));
    }

    @Test
    void testConsumerCreditCreation() {
        assertEquals("C001", consumerCredit.getId());
        assertEquals("CONSUMER", consumerCredit.getCreditType());
        assertEquals(new BigDecimal("50000"), consumerCredit.getAmount());
        assertEquals("HOME_RENOVATION", consumerCredit.getPurpose());
        assertFalse(consumerCredit.isRequiresCollateral());
        assertEquals(new BigDecimal("200000"), consumerCredit.getMaxAmount());
    }

    @Test
    void testConsumerCreditDefaultConstructor() {
        ConsumerCredit credit = new ConsumerCredit();
        assertEquals("CONSUMER", credit.getCreditType());
        assertNull(credit.getPurpose());
    }

    @Test
    void testIsAmountWithinLimit() {
        assertTrue(consumerCredit.isAmountWithinLimit());
    }

    @Test
    void testIsAmountWithinLimitExceeds() {
        consumerCredit.setAmount(new BigDecimal("250000"));
        assertFalse(consumerCredit.isAmountWithinLimit());
    }

    @Test
    void testIsAmountWithinLimitEquals() {
        consumerCredit.setAmount(new BigDecimal("200000"));
        assertTrue(consumerCredit.isAmountWithinLimit());
    }

    @Test
    void testIsAmountWithinLimitWithNullMaxAmount() {
        consumerCredit.setMaxAmount(null);
        assertFalse(consumerCredit.isAmountWithinLimit());
    }

    @Test
    void testConsumerCreditSetters() {
        ConsumerCredit credit = new ConsumerCredit();
        credit.setPurpose("EDUCATION");
        credit.setRequiresCollateral(true);
        credit.setMaxAmount(new BigDecimal("100000"));

        assertEquals("EDUCATION", credit.getPurpose());
        assertTrue(credit.isRequiresCollateral());
        assertEquals(new BigDecimal("100000"), credit.getMaxAmount());
    }

    @Test
    void testConsumerCreditInheritance() {
        assertTrue(consumerCredit instanceof Credit);
        assertNotNull(consumerCredit.getBank());
        assertNotNull(consumerCredit.getInterestRate());
    }

    @Test
    void testConsumerCreditWithDifferentPurposes() {
        consumerCredit.setPurpose("EDUCATION");
        assertEquals("EDUCATION", consumerCredit.getPurpose());

        consumerCredit.setPurpose("MEDICAL");
        assertEquals("MEDICAL", consumerCredit.getPurpose());

        consumerCredit.setPurpose("TRAVEL");
        assertEquals("TRAVEL", consumerCredit.getPurpose());
    }

    @Test
    void testConsumerCreditWithCollateral() {
        consumerCredit.setRequiresCollateral(true);
        assertTrue(consumerCredit.isRequiresCollateral());
    }

    @Test
    void testConsumerCreditWithoutCollateral() {
        consumerCredit.setRequiresCollateral(false);
        assertFalse(consumerCredit.isRequiresCollateral());
    }
}


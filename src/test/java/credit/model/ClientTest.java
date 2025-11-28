package credit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client("CLI001", "John", "Doe", "john@example.com",
                new BigDecimal("50000"), 750, false);
    }

    @Test
    void testClientCreation() {
        assertEquals("CLI001", client.getId());
        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals("john@example.com", client.getEmail());
        assertEquals(new BigDecimal("50000"), client.getMonthlyIncome());
        assertEquals(750, client.getCreditScore());
        assertFalse(client.isHasExistingCredits());
    }

    @Test
    void testClientDefaultConstructor() {
        Client client = new Client();
        assertNull(client.getId());
        assertNull(client.getFirstName());
    }

    @Test
    void testClientSetters() {
        Client client = new Client();
        client.setId("CLI002");
        client.setFirstName("Jane");
        client.setLastName("Smith");
        client.setEmail("jane@example.com");
        client.setMonthlyIncome(new BigDecimal("60000"));
        client.setCreditScore(800);
        client.setHasExistingCredits(true);

        assertEquals("CLI002", client.getId());
        assertEquals("Jane", client.getFirstName());
        assertEquals("Smith", client.getLastName());
        assertEquals("jane@example.com", client.getEmail());
        assertEquals(new BigDecimal("60000"), client.getMonthlyIncome());
        assertEquals(800, client.getCreditScore());
        assertTrue(client.isHasExistingCredits());
    }

    @Test
    void testIsEligibleForCredit() {
        BigDecimal requestedAmount = new BigDecimal("200000");
        BigDecimal monthlyPayment = new BigDecimal("15000");

        assertTrue(client.isEligibleForCredit(requestedAmount, monthlyPayment));
    }

    @Test
    void testIsEligibleForCreditExceedsIncome() {
        BigDecimal requestedAmount = new BigDecimal("200000");
        BigDecimal monthlyPayment = new BigDecimal("25000");

        assertFalse(client.isEligibleForCredit(requestedAmount, monthlyPayment));
    }

    @Test
    void testIsEligibleForCreditLowScore() {
        client.setCreditScore(500);
        BigDecimal requestedAmount = new BigDecimal("200000");
        BigDecimal monthlyPayment = new BigDecimal("15000");

        assertFalse(client.isEligibleForCredit(requestedAmount, monthlyPayment));
    }

    @Test
    void testIsEligibleForCreditExactLimit() {
        BigDecimal requestedAmount = new BigDecimal("200000");
        BigDecimal monthlyPayment = new BigDecimal("20000");

        assertTrue(client.isEligibleForCredit(requestedAmount, monthlyPayment));
    }

    @Test
    void testIsEligibleForCreditWithZeroIncome() {
        client.setMonthlyIncome(BigDecimal.ZERO);
        BigDecimal requestedAmount = new BigDecimal("200000");
        BigDecimal monthlyPayment = new BigDecimal("10000");

        assertFalse(client.isEligibleForCredit(requestedAmount, monthlyPayment));
    }

    @Test
    void testIsEligibleForCreditWithNullIncome() {
        client.setMonthlyIncome(null);
        BigDecimal requestedAmount = new BigDecimal("200000");
        BigDecimal monthlyPayment = new BigDecimal("10000");

        assertFalse(client.isEligibleForCredit(requestedAmount, monthlyPayment));
    }

    @Test
    void testIsEligibleForCreditWithNegativeIncome() {
        client.setMonthlyIncome(new BigDecimal("-1000"));
        BigDecimal requestedAmount = new BigDecimal("200000");
        BigDecimal monthlyPayment = new BigDecimal("10000");

        assertFalse(client.isEligibleForCredit(requestedAmount, monthlyPayment));
    }

    @Test
    void testIsEligibleForCreditMinimumScore() {
        client.setCreditScore(600);
        BigDecimal requestedAmount = new BigDecimal("200000");
        BigDecimal monthlyPayment = new BigDecimal("15000");

        assertTrue(client.isEligibleForCredit(requestedAmount, monthlyPayment));
    }

    @Test
    void testClientEquality() {
        Client client1 = new Client("CLI001", "John", "Doe", "john@example.com",
                new BigDecimal("50000"), 750, false);
        Client client2 = new Client("CLI001", "John", "Doe", "john@example.com",
                new BigDecimal("50000"), 750, false);

        assertEquals(client1, client2);
    }

    @Test
    void testClientInequality() {
        Client client1 = new Client("CLI001", "John", "Doe", "john@example.com",
                new BigDecimal("50000"), 750, false);
        Client client2 = new Client("CLI002", "John", "Doe", "john@example.com",
                new BigDecimal("50000"), 750, false);

        assertNotEquals(client1, client2);
    }

    @Test
    void testClientHashCode() {
        Client client1 = new Client("CLI001", "John", "Doe", "john@example.com",
                new BigDecimal("50000"), 750, false);
        Client client2 = new Client("CLI001", "John", "Doe", "john@example.com",
                new BigDecimal("50000"), 750, false);

        assertEquals(client1.hashCode(), client2.hashCode());
    }

    @Test
    void testClientToString() {
        String toString = client.toString();
        assertTrue(toString.contains("CLI001"));
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
    }
}


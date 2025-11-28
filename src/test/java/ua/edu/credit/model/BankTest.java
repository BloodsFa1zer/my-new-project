package ua.edu.credit.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    @Test
    void testBankCreation() {
        Bank bank = new Bank("TestBank", "TB001", 4.5);
        assertEquals("TestBank", bank.getName());
        assertEquals("TB001", bank.getLicenseNumber());
        assertEquals(4.5, bank.getRating());
    }

    @Test
    void testBankDefaultConstructor() {
        Bank bank = new Bank();
        assertNull(bank.getName());
        assertNull(bank.getLicenseNumber());
        assertEquals(0.0, bank.getRating());
    }

    @Test
    void testBankSetters() {
        Bank bank = new Bank();
        bank.setName("NewBank");
        bank.setLicenseNumber("NB002");
        bank.setRating(4.8);

        assertEquals("NewBank", bank.getName());
        assertEquals("NB002", bank.getLicenseNumber());
        assertEquals(4.8, bank.getRating());
    }

    @Test
    void testBankEquality() {
        Bank bank1 = new Bank("Bank", "LIC001", 4.5);
        Bank bank2 = new Bank("Bank", "LIC001", 4.5);
        Bank bank3 = new Bank("Bank", "LIC002", 4.5);

        assertEquals(bank1, bank2);
        assertNotEquals(bank1, bank3);
    }

    @Test
    void testBankHashCode() {
        Bank bank1 = new Bank("Bank", "LIC001", 4.5);
        Bank bank2 = new Bank("Bank", "LIC001", 4.5);

        assertEquals(bank1.hashCode(), bank2.hashCode());
    }

    @Test
    void testBankToString() {
        Bank bank = new Bank("TestBank", "TB001", 4.5);
        String toString = bank.toString();
        assertTrue(toString.contains("TestBank"));
        assertTrue(toString.contains("TB001"));
        assertTrue(toString.contains("4.5"));
    }

    @Test
    void testBankNotEqualsNull() {
        Bank bank = new Bank("Bank", "LIC001", 4.5);
        assertNotEquals(bank, null);
    }

    @Test
    void testBankNotEqualsDifferentClass() {
        Bank bank = new Bank("Bank", "LIC001", 4.5);
        assertNotEquals(bank, "Not a bank");
    }
}


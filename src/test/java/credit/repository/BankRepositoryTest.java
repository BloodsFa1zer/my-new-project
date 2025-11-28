package credit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import credit.model.Bank;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BankRepositoryTest {

    private BankRepository repository;

    @BeforeEach
    void setUp() {
        repository = new BankRepository();
    }

    @Test
    void testAddBank() {
        Bank bank = new Bank("TestBank", "TB001", 4.5);
        repository.addBank(bank);

        assertEquals(1, repository.size());
    }

    @Test
    void testAddNullBank() {
        repository.addBank(null);
        assertEquals(0, repository.size());
    }

    @Test
    void testAddBankWithNullName() {
        Bank bank = new Bank();
        bank.setLicenseNumber("LIC001");
        repository.addBank(bank);
        assertEquals(0, repository.size());
    }

    @Test
    void testFindByName() {
        Bank bank = new Bank("TestBank", "TB001", 4.5);
        repository.addBank(bank);

        Optional<Bank> found = repository.findByName("TestBank");
        assertTrue(found.isPresent());
        assertEquals("TestBank", found.get().getName());
    }

    @Test
    void testFindByNameCaseInsensitive() {
        Bank bank = new Bank("TestBank", "TB001", 4.5);
        repository.addBank(bank);

        Optional<Bank> found = repository.findByName("testbank");
        assertTrue(found.isPresent());
    }

    @Test
    void testFindByNameNotFound() {
        Optional<Bank> found = repository.findByName("NonExistent");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByLicenseNumber() {
        Bank bank = new Bank("TestBank", "TB001", 4.5);
        repository.addBank(bank);

        Optional<Bank> found = repository.findByLicenseNumber("TB001");
        assertTrue(found.isPresent());
        assertEquals("TB001", found.get().getLicenseNumber());
    }

    @Test
    void testFindByLicenseNumberNotFound() {
        Optional<Bank> found = repository.findByLicenseNumber("NONEXISTENT");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        repository.addBank(new Bank("Bank1", "B001", 4.5));
        repository.addBank(new Bank("Bank2", "B002", 4.8));

        List<Bank> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testFindAllEmpty() {
        List<Bank> all = repository.findAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void testFindByMinRating() {
        repository.addBank(new Bank("Bank1", "B001", 4.5));
        repository.addBank(new Bank("Bank2", "B002", 4.8));
        repository.addBank(new Bank("Bank3", "B003", 4.2));

        List<Bank> banks = repository.findByMinRating(4.5);
        assertEquals(2, banks.size());
    }

    @Test
    void testFindByMinRatingExact() {
        repository.addBank(new Bank("Bank1", "B001", 4.5));
        repository.addBank(new Bank("Bank2", "B002", 4.5));

        List<Bank> banks = repository.findByMinRating(4.5);
        assertEquals(2, banks.size());
    }

    @Test
    void testFindByMinRatingNoMatches() {
        repository.addBank(new Bank("Bank1", "B001", 4.5));
        repository.addBank(new Bank("Bank2", "B002", 4.8));

        List<Bank> banks = repository.findByMinRating(5.0);
        assertTrue(banks.isEmpty());
    }

    @Test
    void testRemoveBank() {
        repository.addBank(new Bank("TestBank", "TB001", 4.5));
        repository.removeBank("TestBank");

        assertEquals(0, repository.size());
        assertFalse(repository.findByName("TestBank").isPresent());
    }

    @Test
    void testRemoveBankCaseInsensitive() {
        repository.addBank(new Bank("TestBank", "TB001", 4.5));
        repository.removeBank("testbank");

        assertEquals(0, repository.size());
    }

    @Test
    void testRemoveNonExistentBank() {
        repository.addBank(new Bank("TestBank", "TB001", 4.5));
        repository.removeBank("NonExistent");

        assertEquals(1, repository.size());
    }

    @Test
    void testClear() {
        repository.addBank(new Bank("Bank1", "B001", 4.5));
        repository.addBank(new Bank("Bank2", "B002", 4.8));
        repository.clear();

        assertEquals(0, repository.size());
    }

    @Test
    void testFindAllReturnsCopy() {
        repository.addBank(new Bank("Bank1", "B001", 4.5));
        List<Bank> all1 = repository.findAll();
        List<Bank> all2 = repository.findAll();

        assertNotSame(all1, all2);
        assertEquals(all1, all2);
    }
}


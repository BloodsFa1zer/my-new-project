package credit.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import credit.model.Bank;
import credit.model.CarCredit;
import credit.model.ConsumerCredit;
import credit.model.Credit;
import credit.model.MortgageCredit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CreditRepositoryTest {

    private CreditRepository repository;
    private Bank testBank1;
    private Bank testBank2;

    @BeforeEach
    void setUp() {
        repository = new CreditRepository();
        testBank1 = new Bank("Bank1", "B001", 4.5);
        testBank2 = new Bank("Bank2", "B002", 4.8);
    }

    @Test
    void testAddCredit() {
        Credit credit = new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false);
        repository.addCredit(credit);

        assertEquals(1, repository.size());
    }

    @Test
    void testAddNullCredit() {
        repository.addCredit(null);
        assertEquals(0, repository.size());
    }

    @Test
    void testAddCreditWithNullId() {
        Credit credit = new Credit();
        credit.setBank(testBank1);
        repository.addCredit(credit);
        assertEquals(0, repository.size());
    }

    @Test
    void testFindById() {
        Credit credit = new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false);
        repository.addCredit(credit);

        Optional<Credit> found = repository.findById("CR001");
        assertTrue(found.isPresent());
        assertEquals("CR001", found.get().getId());
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Credit> found = repository.findById("NONEXISTENT");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindAll() {
        repository.addCredit(new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));
        repository.addCredit(new Credit("CR002", testBank2, "MORTGAGE", new BigDecimal("500000"),
                new BigDecimal("12.0"), 240, true, true));

        List<Credit> all = repository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testFindAllEmpty() {
        List<Credit> all = repository.findAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void testFindByBankName() {
        repository.addCredit(new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));
        repository.addCredit(new Credit("CR002", testBank2, "MORTGAGE", new BigDecimal("500000"),
                new BigDecimal("12.0"), 240, true, true));

        List<Credit> bank1Credits = repository.findByBankName("Bank1");
        assertEquals(1, bank1Credits.size());
        assertEquals("Bank1", bank1Credits.get(0).getBank().getName());
    }

    @Test
    void testFindByBankNameCaseInsensitive() {
        repository.addCredit(new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));

        List<Credit> credits = repository.findByBankName("bank1");
        assertEquals(1, credits.size());
    }

    @Test
    void testFindByCreditType() {
        repository.addCredit(new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));
        repository.addCredit(new Credit("CR002", testBank2, "MORTGAGE", new BigDecimal("500000"),
                new BigDecimal("12.0"), 240, true, true));

        List<Credit> consumerCredits = repository.findByCreditType("CONSUMER");
        assertEquals(1, consumerCredits.size());
        assertEquals("CONSUMER", consumerCredits.get(0).getCreditType());
    }

    @Test
    void testFindByAmountRange() {
        repository.addCredit(new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));
        repository.addCredit(new Credit("CR002", testBank2, "MORTGAGE", new BigDecimal("500000"),
                new BigDecimal("12.0"), 240, true, true));
        repository.addCredit(new Credit("CR003", testBank1, "CAR", new BigDecimal("200000"),
                new BigDecimal("16.0"), 48, true, false));

        List<Credit> credits = repository.findByAmountRange(new BigDecimal("150000"), new BigDecimal("300000"));
        assertEquals(1, credits.size());
        assertEquals("CR003", credits.get(0).getId());
    }

    @Test
    void testFindByAmountRangeBoundary() {
        repository.addCredit(new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));

        List<Credit> credits = repository.findByAmountRange(new BigDecimal("100000"), new BigDecimal("100000"));
        assertEquals(1, credits.size());
    }

    @Test
    void testFindWithEarlyRepayment() {
        repository.addCredit(new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));
        repository.addCredit(new Credit("CR002", testBank2, "MORTGAGE", new BigDecimal("500000"),
                new BigDecimal("12.0"), 240, false, true));

        List<Credit> credits = repository.findWithEarlyRepayment();
        assertEquals(1, credits.size());
        assertTrue(credits.get(0).isEarlyRepaymentAllowed());
    }

    @Test
    void testFindWithCreditLineIncrease() {
        repository.addCredit(new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));
        repository.addCredit(new Credit("CR002", testBank2, "MORTGAGE", new BigDecimal("500000"),
                new BigDecimal("12.0"), 240, true, true));

        List<Credit> credits = repository.findWithCreditLineIncrease();
        assertEquals(1, credits.size());
        assertTrue(credits.get(0).isCreditLineIncreaseAllowed());
    }

    @Test
    void testRemoveCredit() {
        repository.addCredit(new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));
        repository.removeCredit("CR001");

        assertEquals(0, repository.size());
        assertFalse(repository.findById("CR001").isPresent());
    }

    @Test
    void testRemoveNonExistentCredit() {
        repository.addCredit(new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));
        repository.removeCredit("NONEXISTENT");

        assertEquals(1, repository.size());
    }

    @Test
    void testClear() {
        repository.addCredit(new Credit("CR001", testBank1, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));
        repository.clear();

        assertEquals(0, repository.size());
    }

    @Test
    void testAddDifferentCreditTypes() {
        repository.addCredit(new MortgageCredit("M001", testBank1, new BigDecimal("500000"),
                new BigDecimal("12.5"), 240, true, false,
                new BigDecimal("600000"), new BigDecimal("100000"), "APARTMENT"));
        repository.addCredit(new ConsumerCredit("C001", testBank2, new BigDecimal("50000"),
                new BigDecimal("18.5"), 60, true, true,
                "HOME_RENOVATION", false, new BigDecimal("200000")));
        repository.addCredit(new CarCredit("CAR001", testBank1, new BigDecimal("200000"),
                new BigDecimal("15.5"), 60, true, true,
                "Toyota", "Camry", 2023, new BigDecimal("250000"), true));

        assertEquals(3, repository.size());
    }
}


package ua.edu.credit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.credit.model.Bank;
import ua.edu.credit.model.Client;
import ua.edu.credit.model.ConsumerCredit;
import ua.edu.credit.model.Credit;
import ua.edu.credit.repository.CreditRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CreditSelectionServiceTest {

    private CreditRepository creditRepository;
    private CreditSearchService searchService;
    private CreditSelectionService selectionService;
    private Bank testBank1;
    private Bank testBank2;
    private Client testClient;

    @BeforeEach
    void setUp() {
        creditRepository = new CreditRepository();
        searchService = new CreditSearchService(creditRepository);
        selectionService = new CreditSelectionService(creditRepository, searchService);
        testBank1 = new Bank("Bank1", "B001", 4.5);
        testBank2 = new Bank("Bank2", "B002", 4.8);
        testClient = new Client("CLI001", "John", "Doe", "john@example.com",
                new BigDecimal("50000"), 750, false);

        creditRepository.addCredit(new Credit("CR001", testBank1, "CONSUMER",
                new BigDecimal("100000"), new BigDecimal("15.0"), 60, true, false));
        creditRepository.addCredit(new Credit("CR002", testBank2, "CONSUMER",
                new BigDecimal("200000"), new BigDecimal("14.0"), 60, true, true));
        creditRepository.addCredit(new Credit("CR003", testBank1, "MORTGAGE",
                new BigDecimal("500000"), new BigDecimal("12.0"), 240, true, false));
    }

    @Test
    void testSelectOptimalCredit() {
        Optional<Credit> optimal = selectionService.selectOptimalCredit(testClient,
                new BigDecimal("150000"), 60, true, true);

        assertTrue(optimal.isPresent());
        assertNotNull(optimal.get().getId());
    }

    @Test
    void testSelectOptimalCreditWithEarlyRepaymentPreference() {
        Optional<Credit> optimal = selectionService.selectOptimalCredit(testClient,
                new BigDecimal("150000"), 60, true, false);

        assertTrue(optimal.isPresent());
    }

    @Test
    void testSelectOptimalCreditWithCreditLineIncreasePreference() {
        Optional<Credit> optimal = selectionService.selectOptimalCredit(testClient,
                new BigDecimal("150000"), 60, false, true);

        assertTrue(optimal.isPresent());
    }

    @Test
    void testSelectOptimalCreditNoMatches() {
        Client poorClient = new Client("CLI002", "Jane", "Smith", "jane@example.com",
                new BigDecimal("10000"), 500, false);

        Optional<Credit> optimal = selectionService.selectOptimalCredit(poorClient,
                new BigDecimal("1000000"), 60, true, true);

        assertFalse(optimal.isPresent());
    }

    @Test
    void testSelectTopCredits() {
        List<Credit> topCredits = selectionService.selectTopCredits(testClient,
                new BigDecimal("150000"), 60, 3);

        assertFalse(topCredits.isEmpty());
        assertTrue(topCredits.size() <= 3);
    }

    @Test
    void testSelectTopCreditsWithLimit() {
        List<Credit> topCredits = selectionService.selectTopCredits(testClient,
                new BigDecimal("150000"), 60, 1);

        assertTrue(topCredits.size() <= 1);
    }

    @Test
    void testSelectByLowestTotalPayment() {
        Optional<Credit> credit = selectionService.selectByLowestTotalPayment(testClient,
                new BigDecimal("150000"), 60);

        assertTrue(credit.isPresent());
    }

    @Test
    void testSelectByLowestMonthlyPayment() {
        Optional<Credit> credit = selectionService.selectByLowestMonthlyPayment(testClient,
                new BigDecimal("150000"), 60);

        assertTrue(credit.isPresent());
    }

    @Test
    void testSelectByBank() {
        List<Credit> credits = selectionService.selectByBank("Bank1");

        assertFalse(credits.isEmpty());
        assertTrue(credits.stream().allMatch(credit ->
                credit.getBank().getName().equals("Bank1")));
    }

    @Test
    void testSelectByBankNotFound() {
        List<Credit> credits = selectionService.selectByBank("NonExistentBank");

        assertTrue(credits.isEmpty());
    }

    @Test
    void testSelectByCreditType() {
        List<Credit> credits = selectionService.selectByCreditType("CONSUMER");

        assertFalse(credits.isEmpty());
        assertTrue(credits.stream().allMatch(credit ->
                credit.getCreditType().equals("CONSUMER")));
    }

    @Test
    void testSelectByCreditTypeNotFound() {
        List<Credit> credits = selectionService.selectByCreditType("NONEXISTENT");

        assertTrue(credits.isEmpty());
    }

    @Test
    void testSelectByLowestTotalPaymentNoMatches() {
        Client poorClient = new Client("CLI002", "Jane", "Smith", "jane@example.com",
                new BigDecimal("10000"), 500, false);

        Optional<Credit> credit = selectionService.selectByLowestTotalPayment(poorClient,
                new BigDecimal("1000000"), 60);

        assertFalse(credit.isPresent());
    }

    @Test
    void testSelectByLowestMonthlyPaymentNoMatches() {
        Client poorClient = new Client("CLI002", "Jane", "Smith", "jane@example.com",
                new BigDecimal("10000"), 500, false);

        Optional<Credit> credit = selectionService.selectByLowestMonthlyPayment(poorClient,
                new BigDecimal("1000000"), 60);

        assertFalse(credit.isPresent());
    }

    @Test
    void testSelectTopCreditsEmpty() {
        Client poorClient = new Client("CLI002", "Jane", "Smith", "jane@example.com",
                new BigDecimal("10000"), 500, false);

        List<Credit> topCredits = selectionService.selectTopCredits(poorClient,
                new BigDecimal("1000000"), 60, 3);

        assertTrue(topCredits.isEmpty());
    }

    @Test
    void testSelectByBankSortedByInterestRate() {
        List<Credit> credits = selectionService.selectByBank("Bank1");

        if (credits.size() > 1) {
            for (int i = 0; i < credits.size() - 1; i++) {
                assertTrue(credits.get(i).getInterestRate()
                        .compareTo(credits.get(i + 1).getInterestRate()) <= 0);
            }
        }
    }

    @Test
    void testSelectByCreditTypeSortedByInterestRate() {
        List<Credit> credits = selectionService.selectByCreditType("CONSUMER");

        if (credits.size() > 1) {
            for (int i = 0; i < credits.size() - 1; i++) {
                assertTrue(credits.get(i).getInterestRate()
                        .compareTo(credits.get(i + 1).getInterestRate()) <= 0);
            }
        }
    }
}


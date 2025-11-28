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

import static org.junit.jupiter.api.Assertions.*;

class CreditSearchServiceTest {

    private CreditRepository creditRepository;
    private CreditSearchService searchService;
    private Bank testBank1;
    private Bank testBank2;
    private Client testClient;

    @BeforeEach
    void setUp() {
        creditRepository = new CreditRepository();
        searchService = new CreditSearchService(creditRepository);
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
    void testSearchByClientNeeds() {
        List<Credit> results = searchService.searchByClientNeeds(testClient,
                new BigDecimal("150000"), 60);

        assertFalse(results.isEmpty());
        assertTrue(results.stream().allMatch(credit ->
                credit.getAmount().compareTo(new BigDecimal("150000")) >= 0));
    }

    @Test
    void testSearchByClientNeedsNoMatches() {
        Client poorClient = new Client("CLI002", "Jane", "Smith", "jane@example.com",
                new BigDecimal("10000"), 500, false);

        List<Credit> results = searchService.searchByClientNeeds(poorClient,
                new BigDecimal("150000"), 60);

        assertTrue(results.isEmpty());
    }

    @Test
    void testFindBestOffers() {
        List<Credit> results = searchService.findBestOffers(testClient,
                new BigDecimal("150000"), 60);

        assertFalse(results.isEmpty());
        assertTrue(results.size() <= 5);
    }

    @Test
    void testFindBestOffersSortedByInterestRate() {
        List<Credit> results = searchService.findBestOffers(testClient,
                new BigDecimal("150000"), 60);

        if (results.size() > 1) {
            for (int i = 0; i < results.size() - 1; i++) {
                assertTrue(results.get(i).getInterestRate()
                        .compareTo(results.get(i + 1).getInterestRate()) <= 0);
            }
        }
    }

    @Test
    void testFindWithLowestInterestRate() {
        List<Credit> results = searchService.findWithLowestInterestRate(
                new BigDecimal("50000"), new BigDecimal("300000"));

        assertFalse(results.isEmpty());
        if (results.size() > 1) {
            for (int i = 0; i < results.size() - 1; i++) {
                assertTrue(results.get(i).getInterestRate()
                        .compareTo(results.get(i + 1).getInterestRate()) <= 0);
            }
        }
    }

    @Test
    void testFindWithBestBankRating() {
        List<Credit> results = searchService.findWithBestBankRating(new BigDecimal("50000"));

        assertFalse(results.isEmpty());
        if (results.size() > 1) {
            BigDecimal firstRating = results.get(0).getBank().getRating();
            BigDecimal secondRating = results.get(1).getBank().getRating();
            assertTrue(firstRating.compareTo(secondRating) >= 0);
        }
    }

    @Test
    void testFindFlexibleCredits() {
        List<Credit> results = searchService.findFlexibleCredits(new BigDecimal("50000"));

        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(credit ->
                credit.isEarlyRepaymentAllowed() || credit.isCreditLineIncreaseAllowed()));
    }

    @Test
    void testFindWithEarlyRepayment() {
        List<Credit> results = searchService.findWithEarlyRepayment(new BigDecimal("50000"));

        assertFalse(results.isEmpty());
        assertTrue(results.stream().allMatch(Credit::isEarlyRepaymentAllowed));
    }

    @Test
    void testFindWithCreditLineIncrease() {
        List<Credit> results = searchService.findWithCreditLineIncrease(new BigDecimal("50000"));

        assertFalse(results.isEmpty());
        assertTrue(results.stream().allMatch(Credit::isCreditLineIncreaseAllowed));
    }

    @Test
    void testSearchByClientNeedsWithInsufficientAmount() {
        List<Credit> results = searchService.searchByClientNeeds(testClient,
                new BigDecimal("1000000"), 60);

        assertTrue(results.isEmpty());
    }

    @Test
    void testSearchByClientNeedsWithInsufficientTerm() {
        List<Credit> results = searchService.searchByClientNeeds(testClient,
                new BigDecimal("150000"), 300);

        assertTrue(results.isEmpty());
    }

    @Test
    void testFindBestOffersEmptyResult() {
        Client poorClient = new Client("CLI002", "Jane", "Smith", "jane@example.com",
                new BigDecimal("10000"), 500, false);

        List<Credit> results = searchService.findBestOffers(poorClient,
                new BigDecimal("1000000"), 60);

        assertTrue(results.isEmpty());
    }

    @Test
    void testFindWithLowestInterestRateEmptyRange() {
        List<Credit> results = searchService.findWithLowestInterestRate(
                new BigDecimal("1000000"), new BigDecimal("2000000"));

        assertTrue(results.isEmpty());
    }
}


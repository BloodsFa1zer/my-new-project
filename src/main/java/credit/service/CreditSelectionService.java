package credit.service;

import credit.model.Client;
import credit.model.Credit;
import credit.repository.CreditRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Сервіс для вибору оптимального кредиту
public class CreditSelectionService {
    private CreditRepository creditRepository;
    private CreditSearchService creditSearchService;

    public CreditSelectionService(CreditRepository creditRepository, CreditSearchService creditSearchService) {
        this.creditRepository = creditRepository;
        this.creditSearchService = creditSearchService;
    }

    // Знаходить один найкращий кредит з урахуванням переваг
    // Сортуємо: спочатку за ставкою, потім за наявністю бажаних опцій, потім за рейтингом банку
    public Optional<Credit> selectOptimalCredit(Client client, BigDecimal requestedAmount, int termMonths,
                                                boolean preferEarlyRepayment, boolean preferCreditLineIncrease) {
        List<Credit> candidates = creditSearchService.searchByClientNeeds(client, requestedAmount, termMonths);

        if (candidates.isEmpty()) {
            return Optional.empty();
        }

        Comparator<Credit> comparator = Comparator.comparing(Credit::getInterestRate);

        // Якщо клієнт хоче дострокове погашення - кредити з цією опцією будуть вище
        if (preferEarlyRepayment) {
            comparator = comparator.thenComparing(credit -> !credit.isEarlyRepaymentAllowed());
        }

        // Те саме для збільшення ліміту
        if (preferCreditLineIncrease) {
            comparator = comparator.thenComparing(credit -> !credit.isCreditLineIncreaseAllowed());
        }

        // В кінці сортуємо за рейтингом банку (вище = краще)
        comparator = comparator.thenComparing((Credit credit) -> credit.getBank().getRating()).reversed();

        return candidates.stream()
                .sorted(comparator)
                .findFirst();
    }

    public List<Credit> selectTopCredits(Client client, BigDecimal requestedAmount, int termMonths, int limit) {
        return creditSearchService.findBestOffers(client, requestedAmount, termMonths).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Optional<Credit> selectByLowestTotalPayment(Client client, BigDecimal requestedAmount, int termMonths) {
        List<Credit> candidates = creditSearchService.searchByClientNeeds(client, requestedAmount, termMonths);

        return candidates.stream()
                .min(Comparator.comparing(Credit::calculateTotalPayment)
                        .thenComparing(Credit::getInterestRate));
    }

    public Optional<Credit> selectByLowestMonthlyPayment(Client client, BigDecimal requestedAmount, int termMonths) {
        List<Credit> candidates = creditSearchService.searchByClientNeeds(client, requestedAmount, termMonths);

        return candidates.stream()
                .min(Comparator.comparing(Credit::calculateMonthlyPayment)
                        .thenComparing(Credit::getInterestRate));
    }

    public List<Credit> selectByBank(String bankName) {
        return creditRepository.findByBankName(bankName).stream()
                .sorted(Comparator.comparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }

    public List<Credit> selectByCreditType(String creditType) {
        return creditRepository.findByCreditType(creditType).stream()
                .sorted(Comparator.comparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }
}


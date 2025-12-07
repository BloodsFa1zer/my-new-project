package credit.service;

import credit.model.Client;
import credit.model.Credit;
import credit.repository.CreditRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Сервіс для пошуку кредитів
// Використовує Stream API для фільтрації та сортування
public class CreditSearchService {
    private CreditRepository creditRepository;

    public CreditSearchService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    // Шукає кредити які підходять клієнту
    // Перевіряє суму, термін та елігібельність клієнта
    public List<Credit> searchByClientNeeds(Client client, BigDecimal requestedAmount, int termMonths) {
        if (client == null || requestedAmount == null || termMonths <= 0) {
            return new java.util.ArrayList<>();
        }
        
        return creditRepository.findAll().stream()
                // Сума має бути >= запитаної
                .filter(credit -> credit != null && credit.getAmount() != null && 
                        credit.getAmount().compareTo(requestedAmount) >= 0)
                // Термін >= запитаного
                .filter(credit -> credit.getTermMonths() >= termMonths)
                // Перевіряємо чи може клієнт дозволити собі платіж
                .filter(credit -> {
                    BigDecimal monthlyPayment = credit.calculateMonthlyPayment();
                    return monthlyPayment != null && client.isEligibleForCredit(requestedAmount, monthlyPayment);
                })
                .collect(Collectors.toList());
    }

    // Знаходить топ-5 найкращих пропозицій
    // Сортуємо за ставкою (нижче = краще) та рейтингом банку (вище = краще)
    public List<Credit> findBestOffers(Client client, BigDecimal requestedAmount, int termMonths) {
        return searchByClientNeeds(client, requestedAmount, termMonths).stream()
                .filter(credit -> credit.getBank() != null && credit.getInterestRate() != null)
                .sorted(Comparator.comparing(Credit::getInterestRate)
                        .thenComparing(credit -> credit.getBank().getRating(), Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList());
    }

    // Шукає кредити з найнижчою ставкою в заданому діапазоні сум
    public List<Credit> findWithLowestInterestRate(BigDecimal minAmount, BigDecimal maxAmount) {
        if (minAmount == null || maxAmount == null) {
            return new java.util.ArrayList<>();
        }
        return creditRepository.findByAmountRange(minAmount, maxAmount).stream()
                .filter(credit -> credit != null && credit.getInterestRate() != null)
                .sorted(Comparator.comparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }

    // Шукає кредити від банків з найкращим рейтингом
    public List<Credit> findWithBestBankRating(BigDecimal minAmount) {
        if (minAmount == null) {
            return new java.util.ArrayList<>();
        }
        return creditRepository.findAll().stream()
                .filter(credit -> credit != null && credit.getAmount() != null && 
                        credit.getAmount().compareTo(minAmount) >= 0)
                .filter(credit -> credit.getBank() != null && credit.getInterestRate() != null)
                .sorted(Comparator.comparing((Credit credit) -> credit.getBank().getRating())
                        .reversed()
                        .thenComparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }

    // Шукає гнучкі кредити (з достроковим погашенням або збільшенням ліміту)
    public List<Credit> findFlexibleCredits(BigDecimal minAmount) {
        if (minAmount == null) {
            return new java.util.ArrayList<>();
        }
        return creditRepository.findAll().stream()
                .filter(credit -> credit != null && credit.getAmount() != null && 
                        credit.getAmount().compareTo(minAmount) >= 0)
                .filter(credit -> credit.isEarlyRepaymentAllowed() || credit.isCreditLineIncreaseAllowed())
                .filter(credit -> credit.getInterestRate() != null)
                .sorted(Comparator.comparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }

    public List<Credit> findWithEarlyRepayment(BigDecimal minAmount) {
        if (minAmount == null) {
            return new java.util.ArrayList<>();
        }
        return creditRepository.findWithEarlyRepayment().stream()
                .filter(credit -> credit != null && credit.getAmount() != null && 
                        credit.getAmount().compareTo(minAmount) >= 0)
                .filter(credit -> credit.getInterestRate() != null)
                .sorted(Comparator.comparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }

    public List<Credit> findWithCreditLineIncrease(BigDecimal minAmount) {
        if (minAmount == null) {
            return new java.util.ArrayList<>();
        }
        return creditRepository.findWithCreditLineIncrease().stream()
                .filter(credit -> credit != null && credit.getAmount() != null && 
                        credit.getAmount().compareTo(minAmount) >= 0)
                .filter(credit -> credit.getInterestRate() != null)
                .sorted(Comparator.comparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }
}


package ua.edu.credit.service;

import ua.edu.credit.model.Client;
import ua.edu.credit.model.Credit;
import ua.edu.credit.repository.CreditRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CreditSearchService {
    private CreditRepository creditRepository;

    public CreditSearchService(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public List<Credit> searchByClientNeeds(Client client, BigDecimal requestedAmount, int termMonths) {
        return creditRepository.findAll().stream()
                .filter(credit -> credit.getAmount().compareTo(requestedAmount) >= 0)
                .filter(credit -> credit.getTermMonths() >= termMonths)
                .filter(credit -> {
                    BigDecimal monthlyPayment = credit.calculateMonthlyPayment();
                    return client.isEligibleForCredit(requestedAmount, monthlyPayment);
                })
                .collect(Collectors.toList());
    }

    public List<Credit> findBestOffers(Client client, BigDecimal requestedAmount, int termMonths) {
        return searchByClientNeeds(client, requestedAmount, termMonths).stream()
                .sorted(Comparator.comparing(Credit::getInterestRate)
                        .thenComparing(credit -> credit.getBank().getRating(), Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Credit> findWithLowestInterestRate(BigDecimal minAmount, BigDecimal maxAmount) {
        return creditRepository.findByAmountRange(minAmount, maxAmount).stream()
                .sorted(Comparator.comparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }

    public List<Credit> findWithBestBankRating(BigDecimal minAmount) {
        return creditRepository.findAll().stream()
                .filter(credit -> credit.getAmount().compareTo(minAmount) >= 0)
                .sorted(Comparator.comparing((Credit credit) -> credit.getBank().getRating())
                        .reversed()
                        .thenComparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }

    public List<Credit> findFlexibleCredits(BigDecimal minAmount) {
        return creditRepository.findAll().stream()
                .filter(credit -> credit.getAmount().compareTo(minAmount) >= 0)
                .filter(credit -> credit.isEarlyRepaymentAllowed() || credit.isCreditLineIncreaseAllowed())
                .sorted(Comparator.comparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }

    public List<Credit> findWithEarlyRepayment(BigDecimal minAmount) {
        return creditRepository.findWithEarlyRepayment().stream()
                .filter(credit -> credit.getAmount().compareTo(minAmount) >= 0)
                .sorted(Comparator.comparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }

    public List<Credit> findWithCreditLineIncrease(BigDecimal minAmount) {
        return creditRepository.findWithCreditLineIncrease().stream()
                .filter(credit -> credit.getAmount().compareTo(minAmount) >= 0)
                .sorted(Comparator.comparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }
}


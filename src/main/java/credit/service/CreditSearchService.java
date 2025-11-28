package credit.service;

import credit.model.Client;
import credit.model.Credit;
import credit.repository.CreditRepository;

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
        if (client == null || requestedAmount == null || termMonths <= 0) {
            return new java.util.ArrayList<>();
        }
        return creditRepository.findAll().stream()
                .filter(credit -> credit != null && credit.getAmount() != null && 
                        credit.getAmount().compareTo(requestedAmount) >= 0)
                .filter(credit -> credit.getTermMonths() >= termMonths)
                .filter(credit -> {
                    BigDecimal monthlyPayment = credit.calculateMonthlyPayment();
                    return monthlyPayment != null && client.isEligibleForCredit(requestedAmount, monthlyPayment);
                })
                .collect(Collectors.toList());
    }

    public List<Credit> findBestOffers(Client client, BigDecimal requestedAmount, int termMonths) {
        return searchByClientNeeds(client, requestedAmount, termMonths).stream()
                .filter(credit -> credit.getBank() != null && credit.getInterestRate() != null)
                .sorted(Comparator.comparing(Credit::getInterestRate)
                        .thenComparing(credit -> credit.getBank().getRating(), Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Credit> findWithLowestInterestRate(BigDecimal minAmount, BigDecimal maxAmount) {
        if (minAmount == null || maxAmount == null) {
            return new java.util.ArrayList<>();
        }
        return creditRepository.findByAmountRange(minAmount, maxAmount).stream()
                .filter(credit -> credit != null && credit.getInterestRate() != null)
                .sorted(Comparator.comparing(Credit::getInterestRate))
                .collect(Collectors.toList());
    }

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


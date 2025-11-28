package credit.repository;

import credit.model.Credit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreditRepository {
    private List<Credit> credits;

    public CreditRepository() {
        this.credits = new ArrayList<>();
    }

    public void addCredit(Credit credit) {
        if (credit != null && credit.getId() != null) {
            credits.add(credit);
        }
    }

    public Optional<Credit> findById(String id) {
        return credits.stream()
                .filter(credit -> credit.getId().equals(id))
                .findFirst();
    }

    public List<Credit> findAll() {
        return new ArrayList<>(credits);
    }

    public List<Credit> findByBankName(String bankName) {
        return credits.stream()
                .filter(credit -> credit.getBank() != null &&
                        credit.getBank().getName().equalsIgnoreCase(bankName))
                .collect(Collectors.toList());
    }

    public List<Credit> findByCreditType(String creditType) {
        return credits.stream()
                .filter(credit -> credit.getCreditType().equalsIgnoreCase(creditType))
                .collect(Collectors.toList());
    }

    public List<Credit> findByAmountRange(java.math.BigDecimal minAmount, java.math.BigDecimal maxAmount) {
        return credits.stream()
                .filter(credit -> credit.getAmount().compareTo(minAmount) >= 0 &&
                        credit.getAmount().compareTo(maxAmount) <= 0)
                .collect(Collectors.toList());
    }

    public List<Credit> findWithEarlyRepayment() {
        return credits.stream()
                .filter(Credit::isEarlyRepaymentAllowed)
                .collect(Collectors.toList());
    }

    public List<Credit> findWithCreditLineIncrease() {
        return credits.stream()
                .filter(Credit::isCreditLineIncreaseAllowed)
                .collect(Collectors.toList());
    }

    public void removeCredit(String id) {
        credits.removeIf(credit -> credit.getId().equals(id));
    }

    public void clear() {
        credits.clear();
    }

    public int size() {
        return credits.size();
    }
}


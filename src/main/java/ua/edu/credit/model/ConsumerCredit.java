package ua.edu.credit.model;

import java.math.BigDecimal;

public class ConsumerCredit extends Credit {
    private String purpose;
    private boolean requiresCollateral;
    private BigDecimal maxAmount;

    public ConsumerCredit() {
        super();
        setCreditType("CONSUMER");
    }

    public ConsumerCredit(String id, Bank bank, BigDecimal amount, BigDecimal interestRate,
                          int termMonths, boolean earlyRepaymentAllowed,
                          boolean creditLineIncreaseAllowed, String purpose,
                          boolean requiresCollateral, BigDecimal maxAmount) {
        super(id, bank, "CONSUMER", amount, interestRate, termMonths,
                earlyRepaymentAllowed, creditLineIncreaseAllowed);
        this.purpose = purpose;
        this.requiresCollateral = requiresCollateral;
        this.maxAmount = maxAmount;
    }

    public boolean isAmountWithinLimit() {
        return maxAmount != null && getAmount().compareTo(maxAmount) <= 0;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public boolean isRequiresCollateral() {
        return requiresCollateral;
    }

    public void setRequiresCollateral(boolean requiresCollateral) {
        this.requiresCollateral = requiresCollateral;
    }

    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }
}


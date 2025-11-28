package ua.edu.credit.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class Credit {
    private String id;
    private Bank bank;
    private String creditType;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private int termMonths;
    private boolean earlyRepaymentAllowed;
    private boolean creditLineIncreaseAllowed;
    private LocalDate startDate;

    public Credit() {
    }

    public Credit(String id, Bank bank, String creditType, BigDecimal amount,
                  BigDecimal interestRate, int termMonths, boolean earlyRepaymentAllowed,
                  boolean creditLineIncreaseAllowed) {
        this.id = id;
        this.bank = bank;
        this.creditType = creditType;
        this.amount = amount;
        this.interestRate = interestRate;
        this.termMonths = termMonths;
        this.earlyRepaymentAllowed = earlyRepaymentAllowed;
        this.creditLineIncreaseAllowed = creditLineIncreaseAllowed;
        this.startDate = LocalDate.now();
    }

    public BigDecimal calculateTotalPayment() {
        BigDecimal monthlyRate = interestRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
        BigDecimal monthlyPayment = amount.multiply(
                monthlyRate.multiply(BigDecimal.valueOf(Math.pow(1 + monthlyRate.doubleValue(), termMonths)))
                        .divide(BigDecimal.valueOf(Math.pow(1 + monthlyRate.doubleValue(), termMonths) - 1), 4, RoundingMode.HALF_UP)
        );
        return monthlyPayment.multiply(BigDecimal.valueOf(termMonths));
    }

    public BigDecimal calculateMonthlyPayment() {
        return calculateTotalPayment().divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public int getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(int termMonths) {
        this.termMonths = termMonths;
    }

    public boolean isEarlyRepaymentAllowed() {
        return earlyRepaymentAllowed;
    }

    public void setEarlyRepaymentAllowed(boolean earlyRepaymentAllowed) {
        this.earlyRepaymentAllowed = earlyRepaymentAllowed;
    }

    public boolean isCreditLineIncreaseAllowed() {
        return creditLineIncreaseAllowed;
    }

    public void setCreditLineIncreaseAllowed(boolean creditLineIncreaseAllowed) {
        this.creditLineIncreaseAllowed = creditLineIncreaseAllowed;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return termMonths == credit.termMonths &&
                earlyRepaymentAllowed == credit.earlyRepaymentAllowed &&
                creditLineIncreaseAllowed == credit.creditLineIncreaseAllowed &&
                Objects.equals(id, credit.id) &&
                Objects.equals(bank, credit.bank) &&
                Objects.equals(creditType, credit.creditType) &&
                Objects.equals(amount, credit.amount) &&
                Objects.equals(interestRate, credit.interestRate) &&
                Objects.equals(startDate, credit.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bank, creditType, amount, interestRate, termMonths,
                earlyRepaymentAllowed, creditLineIncreaseAllowed, startDate);
    }

    @Override
    public String toString() {
        return "Credit{" +
                "id='" + id + '\'' +
                ", bank=" + bank +
                ", creditType='" + creditType + '\'' +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", termMonths=" + termMonths +
                ", earlyRepaymentAllowed=" + earlyRepaymentAllowed +
                ", creditLineIncreaseAllowed=" + creditLineIncreaseAllowed +
                ", startDate=" + startDate +
                '}';
    }
}


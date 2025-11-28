package credit.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MortgageCredit extends Credit {
    private BigDecimal propertyValue;
    private BigDecimal downPayment;
    private String propertyType;

    public MortgageCredit() {
        super();
        setCreditType("MORTGAGE");
    }

    public MortgageCredit(String id, Bank bank, BigDecimal amount, BigDecimal interestRate,
                          int termMonths, boolean earlyRepaymentAllowed,
                          boolean creditLineIncreaseAllowed, BigDecimal propertyValue,
                          BigDecimal downPayment, String propertyType) {
        super(id, bank, "MORTGAGE", amount, interestRate, termMonths,
                earlyRepaymentAllowed, creditLineIncreaseAllowed);
        this.propertyValue = propertyValue;
        this.downPayment = downPayment;
        this.propertyType = propertyType;
    }

    public BigDecimal calculateLoanToValue() {
        if (propertyValue == null || propertyValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return getAmount().divide(propertyValue, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(BigDecimal propertyValue) {
        this.propertyValue = propertyValue;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }
}


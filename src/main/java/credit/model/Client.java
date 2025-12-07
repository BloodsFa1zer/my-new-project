package credit.model;

import java.math.BigDecimal;
import java.util.Objects;

// Клас для представлення клієнта
public class Client {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal monthlyIncome;
    private int creditScore;  // від 300 до 850
    private boolean hasExistingCredits;

    public Client() {
    }

    public Client(String id, String firstName, String lastName, String email,
                  BigDecimal monthlyIncome, int creditScore, boolean hasExistingCredits) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.monthlyIncome = monthlyIncome;
        this.creditScore = creditScore;
        this.hasExistingCredits = hasExistingCredits;
    }

    // Перевіряє чи може клієнт отримати кредит
    // Платіж має бути не більше 40% доходу і кредитний рейтинг >= 600
    public boolean isEligibleForCredit(BigDecimal requestedAmount, BigDecimal monthlyPayment) {
        if (monthlyIncome == null || monthlyIncome.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal maxPayment = monthlyIncome.multiply(BigDecimal.valueOf(0.4));
        return monthlyPayment.compareTo(maxPayment) <= 0 && creditScore >= 600;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }

    public boolean isHasExistingCredits() {
        return hasExistingCredits;
    }

    public void setHasExistingCredits(boolean hasExistingCredits) {
        this.hasExistingCredits = hasExistingCredits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return creditScore == client.creditScore &&
                hasExistingCredits == client.hasExistingCredits &&
                Objects.equals(id, client.id) &&
                Objects.equals(firstName, client.firstName) &&
                Objects.equals(lastName, client.lastName) &&
                Objects.equals(email, client.email) &&
                Objects.equals(monthlyIncome, client.monthlyIncome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, monthlyIncome, creditScore, hasExistingCredits);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", monthlyIncome=" + monthlyIncome +
                ", creditScore=" + creditScore +
                ", hasExistingCredits=" + hasExistingCredits +
                '}';
    }
}


package credit.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CarCredit extends Credit {
    private String carBrand;
    private String carModel;
    private int carYear;
    private BigDecimal carValue;
    private boolean newCar;

    public CarCredit() {
        super();
        setCreditType("CAR");
    }

    public CarCredit(String id, Bank bank, BigDecimal amount, BigDecimal interestRate,
                     int termMonths, boolean earlyRepaymentAllowed,
                     boolean creditLineIncreaseAllowed, String carBrand,
                     String carModel, int carYear, BigDecimal carValue, boolean newCar) {
        super(id, bank, "CAR", amount, interestRate, termMonths,
                earlyRepaymentAllowed, creditLineIncreaseAllowed);
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carYear = carYear;
        this.carValue = carValue;
        this.newCar = newCar;
    }

    public BigDecimal calculateDepreciationRate() {
        if (carValue == null || carValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return getAmount().divide(carValue, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }

    public BigDecimal getCarValue() {
        return carValue;
    }

    public void setCarValue(BigDecimal carValue) {
        this.carValue = carValue;
    }

    public boolean isNewCar() {
        return newCar;
    }

    public void setNewCar(boolean newCar) {
        this.newCar = newCar;
    }
}


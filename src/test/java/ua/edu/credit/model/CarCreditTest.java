package ua.edu.credit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CarCreditTest {

    private Bank testBank;
    private CarCredit carCredit;

    @BeforeEach
    void setUp() {
        testBank = new Bank("TestBank", "TB001", 4.5);
        carCredit = new CarCredit("CAR001", testBank, new BigDecimal("200000"),
                new BigDecimal("15.5"), 60, true, true,
                "Toyota", "Camry", 2023, new BigDecimal("250000"), true);
    }

    @Test
    void testCarCreditCreation() {
        assertEquals("CAR001", carCredit.getId());
        assertEquals("CAR", carCredit.getCreditType());
        assertEquals(new BigDecimal("200000"), carCredit.getAmount());
        assertEquals("Toyota", carCredit.getCarBrand());
        assertEquals("Camry", carCredit.getCarModel());
        assertEquals(2023, carCredit.getCarYear());
        assertEquals(new BigDecimal("250000"), carCredit.getCarValue());
        assertTrue(carCredit.isNewCar());
    }

    @Test
    void testCarCreditDefaultConstructor() {
        CarCredit credit = new CarCredit();
        assertEquals("CAR", credit.getCreditType());
        assertNull(credit.getCarBrand());
    }

    @Test
    void testCalculateDepreciationRate() {
        BigDecimal depreciationRate = carCredit.calculateDepreciationRate();
        assertNotNull(depreciationRate);
        assertTrue(depreciationRate.compareTo(BigDecimal.ZERO) > 0);
        assertTrue(depreciationRate.compareTo(BigDecimal.valueOf(100)) <= 0);
    }

    @Test
    void testCalculateDepreciationRateWithZeroCarValue() {
        CarCredit credit = new CarCredit();
        credit.setCarValue(BigDecimal.ZERO);
        BigDecimal rate = credit.calculateDepreciationRate();
        assertEquals(BigDecimal.ZERO, rate);
    }

    @Test
    void testCalculateDepreciationRateWithNullCarValue() {
        CarCredit credit = new CarCredit();
        credit.setCarValue(null);
        BigDecimal rate = credit.calculateDepreciationRate();
        assertEquals(BigDecimal.ZERO, rate);
    }

    @Test
    void testCarCreditSetters() {
        CarCredit credit = new CarCredit();
        credit.setCarBrand("BMW");
        credit.setCarModel("X5");
        credit.setCarYear(2021);
        credit.setCarValue(new BigDecimal("180000"));
        credit.setNewCar(false);

        assertEquals("BMW", credit.getCarBrand());
        assertEquals("X5", credit.getCarModel());
        assertEquals(2021, credit.getCarYear());
        assertEquals(new BigDecimal("180000"), credit.getCarValue());
        assertFalse(credit.isNewCar());
    }

    @Test
    void testCarCreditInheritance() {
        assertTrue(carCredit instanceof Credit);
        assertNotNull(carCredit.getBank());
        assertNotNull(carCredit.getInterestRate());
    }

    @Test
    void testDepreciationRateCalculation() {
        carCredit.setCarValue(new BigDecimal("200000"));
        carCredit.setAmount(new BigDecimal("150000"));

        BigDecimal rate = carCredit.calculateDepreciationRate();
        assertEquals(new BigDecimal("75.0000"), rate);
    }

    @Test
    void testNewCarFlag() {
        carCredit.setNewCar(true);
        assertTrue(carCredit.isNewCar());

        carCredit.setNewCar(false);
        assertFalse(carCredit.isNewCar());
    }

    @Test
    void testCarCreditWithDifferentBrands() {
        carCredit.setCarBrand("Mercedes");
        assertEquals("Mercedes", carCredit.getCarBrand());

        carCredit.setCarBrand("Audi");
        assertEquals("Audi", carCredit.getCarBrand());
    }

    @Test
    void testCarYearRange() {
        carCredit.setCarYear(2020);
        assertEquals(2020, carCredit.getCarYear());

        carCredit.setCarYear(2024);
        assertEquals(2024, carCredit.getCarYear());
    }
}


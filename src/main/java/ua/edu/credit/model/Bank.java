package ua.edu.credit.model;

import java.util.Objects;

public class Bank {
    private String name;
    private String licenseNumber;
    private double rating;

    public Bank() {
    }

    public Bank(String name, String licenseNumber, double rating) {
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Double.compare(bank.rating, rating) == 0 &&
                Objects.equals(name, bank.name) &&
                Objects.equals(licenseNumber, bank.licenseNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, licenseNumber, rating);
    }

    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", rating=" + rating +
                '}';
    }
}


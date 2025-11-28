package ua.edu.credit.repository;

import ua.edu.credit.model.Bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BankRepository {
    private List<Bank> banks;

    public BankRepository() {
        this.banks = new ArrayList<>();
    }

    public void addBank(Bank bank) {
        if (bank != null && bank.getName() != null) {
            banks.add(bank);
        }
    }

    public Optional<Bank> findByName(String name) {
        return banks.stream()
                .filter(bank -> bank.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public Optional<Bank> findByLicenseNumber(String licenseNumber) {
        return banks.stream()
                .filter(bank -> bank.getLicenseNumber().equals(licenseNumber))
                .findFirst();
    }

    public List<Bank> findAll() {
        return new ArrayList<>(banks);
    }

    public List<Bank> findByMinRating(double minRating) {
        return banks.stream()
                .filter(bank -> bank.getRating() >= minRating)
                .collect(Collectors.toList());
    }

    public void removeBank(String name) {
        banks.removeIf(bank -> bank.getName().equalsIgnoreCase(name));
    }

    public void clear() {
        banks.clear();
    }

    public int size() {
        return banks.size();
    }
}


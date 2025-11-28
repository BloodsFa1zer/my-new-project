package ua.edu.credit;

import ua.edu.credit.model.*;
import ua.edu.credit.repository.BankRepository;
import ua.edu.credit.repository.CreditRepository;
import ua.edu.credit.service.CreditSearchService;
import ua.edu.credit.service.CreditSelectionService;
import ua.edu.credit.util.FileDataManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Application {
    private BankRepository bankRepository;
    private CreditRepository creditRepository;
    private CreditSearchService creditSearchService;
    private CreditSelectionService creditSelectionService;
    private FileDataManager fileDataManager;
    private Scanner scanner;

    public Application() {
        this.bankRepository = new BankRepository();
        this.creditRepository = new CreditRepository();
        this.creditSearchService = new CreditSearchService(creditRepository);
        this.creditSelectionService = new CreditSelectionService(creditRepository, creditSearchService);
        this.fileDataManager = new FileDataManager();
        this.scanner = new Scanner(System.in);
    }

    public void initialize() {
        try {
            List<Bank> banks = fileDataManager.loadBanks();
            for (Bank bank : banks) {
                bankRepository.addBank(bank);
            }

            List<Credit> credits = fileDataManager.loadCredits();
            for (Credit credit : credits) {
                creditRepository.addCredit(credit);
            }

            if (banks.isEmpty() && credits.isEmpty()) {
                initializeDefaultData();
            }
        } catch (Exception e) {
            initializeDefaultData();
        }
    }

    private void initializeDefaultData() {
        Bank bank1 = new Bank("PrivatBank", "PB001", 4.8);
        Bank bank2 = new Bank("Oschadbank", "OSB002", 4.6);
        Bank bank3 = new Bank("Raiffeisen Bank", "RB003", 4.9);
        Bank bank4 = new Bank("Monobank", "MB004", 4.7);

        bankRepository.addBank(bank1);
        bankRepository.addBank(bank2);
        bankRepository.addBank(bank3);
        bankRepository.addBank(bank4);

        MortgageCredit mortgage1 = new MortgageCredit("M001", bank1, new BigDecimal("500000"),
                new BigDecimal("12.5"), 240, true, false,
                new BigDecimal("600000"), new BigDecimal("100000"), "APARTMENT");
        MortgageCredit mortgage2 = new MortgageCredit("M002", bank2, new BigDecimal("800000"),
                new BigDecimal("11.8"), 300, true, true,
                new BigDecimal("1000000"), new BigDecimal("200000"), "HOUSE");

        ConsumerCredit consumer1 = new ConsumerCredit("C001", bank3, new BigDecimal("50000"),
                new BigDecimal("18.5"), 60, true, true,
                "HOME_RENOVATION", false, new BigDecimal("200000"));
        ConsumerCredit consumer2 = new ConsumerCredit("C002", bank4, new BigDecimal("30000"),
                new BigDecimal("19.2"), 36, true, false,
                "EDUCATION", false, new BigDecimal("100000"));

        CarCredit car1 = new CarCredit("CAR001", bank1, new BigDecimal("200000"),
                new BigDecimal("15.5"), 60, true, true,
                "Toyota", "Camry", 2023, new BigDecimal("250000"), true);
        CarCredit car2 = new CarCredit("CAR002", bank3, new BigDecimal("150000"),
                new BigDecimal("16.2"), 48, true, false,
                "BMW", "X5", 2021, new BigDecimal("180000"), false);

        creditRepository.addCredit(mortgage1);
        creditRepository.addCredit(mortgage2);
        creditRepository.addCredit(consumer1);
        creditRepository.addCredit(consumer2);
        creditRepository.addCredit(car1);
        creditRepository.addCredit(car2);
    }

    public void run() {
        initialize();

        while (true) {
            System.out.println("\n1. Search credits");
            System.out.println("2. Select optimal credit");
            System.out.println("3. View all credits");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleSearch();
                    break;
                case "2":
                    handleSelection();
                    break;
                case "3":
                    handleViewAll();
                    break;
                case "4":
                    saveData();
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void handleSearch() {
        System.out.print("Enter requested amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Enter term in months: ");
        int term = Integer.parseInt(scanner.nextLine().trim());

        Client client = createClientFromInput();

        List<Credit> results = creditSearchService.findBestOffers(client, amount, term);

        if (results.isEmpty()) {
            System.out.println("No suitable credits found");
        } else {
            System.out.println("\nFound " + results.size() + " credit(s):");
            results.forEach(credit -> {
                System.out.println(credit.getBank().getName() + " - " + credit.getCreditType() +
                        " - Rate: " + credit.getInterestRate() + "% - Monthly: " +
                        credit.calculateMonthlyPayment());
            });
        }
    }

    private void handleSelection() {
        System.out.print("Enter requested amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Enter term in months: ");
        int term = Integer.parseInt(scanner.nextLine().trim());

        Client client = createClientFromInput();

        var optimal = creditSelectionService.selectOptimalCredit(client, amount, term, true, true);

        if (optimal.isPresent()) {
            Credit credit = optimal.get();
            System.out.println("\nOptimal credit:");
            System.out.println("Bank: " + credit.getBank().getName());
            System.out.println("Type: " + credit.getCreditType());
            System.out.println("Rate: " + credit.getInterestRate() + "%");
            System.out.println("Monthly payment: " + credit.calculateMonthlyPayment());
            System.out.println("Total payment: " + credit.calculateTotalPayment());
        } else {
            System.out.println("No suitable credit found");
        }
    }

    private void handleViewAll() {
        List<Credit> credits = creditRepository.findAll();
        if (credits.isEmpty()) {
            System.out.println("No credits available");
        } else {
            credits.forEach(credit -> {
                System.out.println(credit.getId() + " - " + credit.getBank().getName() +
                        " - " + credit.getCreditType() + " - " + credit.getAmount() +
                        " - " + credit.getInterestRate() + "%");
            });
        }
    }

    private Client createClientFromInput() {
        System.out.print("Enter monthly income: ");
        BigDecimal income = new BigDecimal(scanner.nextLine().trim());
        System.out.print("Enter credit score: ");
        int score = Integer.parseInt(scanner.nextLine().trim());

        return new Client("CLI001", "John", "Doe", "john@example.com",
                income, score, false);
    }

    private void saveData() {
        try {
            fileDataManager.saveBanks(bankRepository.findAll());
            fileDataManager.saveCredits(creditRepository.findAll());
        } catch (Exception e) {
            System.err.println("Failed to save data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }
}


package credit;

import credit.model.*;
import credit.repository.BankRepository;
import credit.repository.CreditRepository;
import credit.service.CreditSearchService;
import credit.service.CreditSelectionService;
import credit.util.FileDataManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

// Головний клас - тут все запускається і працює
public class Application {
    // Тут зберігаються банки та кредити
    private BankRepository bankRepository;
    private CreditRepository creditRepository;
    
    // Сервіси для пошуку кредитів
    private CreditSearchService creditSearchService;
    private CreditSelectionService creditSelectionService;
    
    // Для роботи з файлами
    private FileDataManager fileDataManager;
    
    // Читаємо що користувач вводить
    private Scanner scanner;

    // Конструктор - створюємо всі потрібні об'єкти
    public Application() {
        this.bankRepository = new BankRepository();
        this.creditRepository = new CreditRepository();
        this.creditSearchService = new CreditSearchService(creditRepository);
        this.creditSelectionService = new CreditSelectionService(creditRepository, creditSearchService);
        this.fileDataManager = new FileDataManager();
        this.scanner = new Scanner(System.in);
    }

    // Завантажуємо дані з файлів, якщо їх немає - створюємо тестові
    public void initialize() {
        try {
            // Спробуємо завантажити з файлів
            List<Bank> banks = fileDataManager.loadBanks();
            for (Bank bank : banks) {
                bankRepository.addBank(bank);
            }

            List<Credit> credits = fileDataManager.loadCredits();
            for (Credit credit : credits) {
                creditRepository.addCredit(credit);
            }

            // Якщо немає кредитів - створюємо тестові дані
            // (банки можуть бути з попереднього запуску, але кредити потрібні для демонстрації)
            if (creditRepository.findAll().isEmpty()) {
                initializeDefaultData();
            }
        } catch (Exception e) {
            // Якщо помилка - теж створюємо тестові дані
            initializeDefaultData();
        }
    }

    // Створюємо тестові дані для демонстрації
    private void initializeDefaultData() {
        // Створюємо кілька банків з різними рейтингами
        Bank bank1 = new Bank("PrivatBank", "PB001", 4.8);
        Bank bank2 = new Bank("Oschadbank", "OSB002", 4.6);
        Bank bank3 = new Bank("Raiffeisen Bank", "RB003", 4.9);
        Bank bank4 = new Bank("Monobank", "MB004", 4.7);

        bankRepository.addBank(bank1);
        bankRepository.addBank(bank2);
        bankRepository.addBank(bank3);
        bankRepository.addBank(bank4);

        // Створюємо іпотечні кредити
        MortgageCredit mortgage1 = new MortgageCredit("M001", bank1, new BigDecimal("500000"),
                new BigDecimal("12.5"), 240, true, false,
                new BigDecimal("600000"), new BigDecimal("100000"), "APARTMENT");
        MortgageCredit mortgage2 = new MortgageCredit("M002", bank2, new BigDecimal("800000"),
                new BigDecimal("11.8"), 300, true, true,
                new BigDecimal("1000000"), new BigDecimal("200000"), "HOUSE");

        // Споживчі кредити
        ConsumerCredit consumer1 = new ConsumerCredit("C001", bank3, new BigDecimal("50000"),
                new BigDecimal("18.5"), 60, true, true,
                "HOME_RENOVATION", false, new BigDecimal("200000"));
        ConsumerCredit consumer2 = new ConsumerCredit("C002", bank4, new BigDecimal("30000"),
                new BigDecimal("19.2"), 36, true, false,
                "EDUCATION", false, new BigDecimal("100000"));

        // Автокредити
        CarCredit car1 = new CarCredit("CAR001", bank1, new BigDecimal("200000"),
                new BigDecimal("15.5"), 60, true, true,
                "Toyota", "Camry", 2023, new BigDecimal("250000"), true);
        CarCredit car2 = new CarCredit("CAR002", bank3, new BigDecimal("150000"),
                new BigDecimal("16.2"), 48, true, false,
                "BMW", "X5", 2021, new BigDecimal("180000"), false);

        // Додаємо все в репозиторій
        creditRepository.addCredit(mortgage1);
        creditRepository.addCredit(mortgage2);
        creditRepository.addCredit(consumer1);
        creditRepository.addCredit(consumer2);
        creditRepository.addCredit(car1);
        creditRepository.addCredit(car2);
    }

    // Головний метод - тут все починається
    public void run() {
        initialize();

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║         CREDIT MANAGEMENT SYSTEM                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("\nSelect mode:");
        System.out.println("1. Interactive Mode");
        System.out.println("2. Demo Mode (Automatic demonstration)");
        System.out.print("Choose option (1 or 2): ");

        String mode = scanner.nextLine().trim();
        if ("2".equals(mode)) {
            runDemoMode();  // демо режим
        } else {
            runInteractiveMode();  // інтерактивний режим
        }
    }

    // Інтерактивний режим - користувач сам вибирає що робити
    public void runInteractiveMode() {
        System.out.println("\nWelcome to Interactive Mode!");
        System.out.println("You can explore all features of the Credit Management System.");
        System.out.println("Type 'help' at any time to see available commands.\n");
        
        // Цикл працює поки не вийдемо
        while (true) {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║                    MAIN MENU                              ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("1. Search for best credit offers");
            System.out.println("2. Find optimal credit for client");
            System.out.println("3. View all available credits");
            System.out.println("4. View all banks");
            System.out.println("5. Search by credit type");
            System.out.println("6. Search by bank");
            System.out.println("7. Find credits with lowest interest rate");
            System.out.println("8. Find credits with best bank rating");
            System.out.println("9. Find flexible credits (early repayment/line increase)");
            System.out.println("0. Exit and save data");
            System.out.print("\nChoose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice.toLowerCase()) {
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
                    handleViewAllBanks();
                    break;
                case "5":
                    handleSearchByType();
                    break;
                case "6":
                    handleSearchByBank();
                    break;
                case "7":
                    handleLowestInterestRate();
                    break;
                case "8":
                    handleBestBankRating();
                    break;
                case "9":
                    handleFlexibleCredits();
                    break;
                case "0":
                case "exit":
                case "quit":
                    saveData();
                    System.out.println("\n╔══════════════════════════════════════════════════════════╗");
                    System.out.println("║  Thank you for using Credit Management System!            ║");
                    System.out.println("║  All data has been saved.                                 ║");
                    System.out.println("╚══════════════════════════════════════════════════════════╝");
                    return;
                case "help":
                    showHelp();
                    break;
                default:
                    System.out.println("\n❌ Invalid option. Please enter a number from 0-9, or type 'help'.");
            }
            
            if (!choice.equals("help") && !choice.equals("0") && !choice.equals("exit") && !choice.equals("quit")) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }
    
    private void showHelp() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                        HELP MENU                            ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("\nAvailable Commands:");
        System.out.println("  1 - Search for best credit offers based on client profile");
        System.out.println("  2 - Find the optimal credit matching specific preferences");
        System.out.println("  3 - View all available credits in the system");
        System.out.println("  4 - View all banks and their ratings");
        System.out.println("  5 - Filter credits by type (CONSUMER, MORTGAGE, CAR)");
        System.out.println("  6 - Find all credits from a specific bank");
        System.out.println("  7 - Find credits with the lowest interest rates");
        System.out.println("  8 - Find credits from banks with best ratings");
        System.out.println("  9 - Find flexible credits (early repayment/line increase)");
        System.out.println("  0 - Exit and save all data");
        System.out.println("  help - Show this help menu");
        System.out.println("\nTips:");
        System.out.println("  • All amounts should be entered as numbers (e.g., 100000)");
        System.out.println("  • Credit types are: CONSUMER, MORTGAGE, CAR");
        System.out.println("  • Credit score should be between 300-850");
        System.out.println("  • Data is automatically saved when you exit");
    }

    // Демо режим - автоматично показує всі можливості
    public void runDemoMode() {
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║              DEMO MODE - AUTOMATIC DEMONSTRATION         ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("\nThis demo will showcase all major features of the Credit Management System.");
            System.out.println("Each demonstration will run automatically with a brief pause between sections.\n");
            
            pause(2000);
        
        // Demo 1: Show all banks
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║ [DEMO 1] Displaying All Available Banks                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("The system manages multiple banks with their ratings and license numbers.\n");
        handleViewAllBanks();
        pause(3000);

        // Demo 2: Show all credits
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║ [DEMO 2] Displaying All Available Credits                ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("The system supports three types of credits: Consumer, Mortgage, and Car credits.\n");
        handleViewAll();
        pause(3000);

        // Demo 3: Search for best offers
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║ [DEMO 3] Searching for Best Credit Offers                ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("Scenario: Client needs a credit of 200,000 UAH for 60 months");
        System.out.println("Client Profile:");
        System.out.println("  • Monthly Income: 50,000 UAH");
        System.out.println("  • Credit Score: 750");
        System.out.println("  • No existing credits");
        System.out.println("\nSearching for top 5 best offers (sorted by interest rate and bank rating)...\n");
        Client demoClient1 = new Client("DEMO1", "John", "Doe", "john@demo.com",
                new BigDecimal("50000"), 750, false);
        List<Credit> bestOffers = creditSearchService.findBestOffers(demoClient1,
                new BigDecimal("200000"), 60);
        displayCredits(bestOffers, "Best Offers");
        pause(4000);

        // Demo 4: Find optimal credit
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║ [DEMO 4] Finding Optimal Credit with Preferences          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("Scenario: Client wants the best credit matching specific preferences");
        System.out.println("Client Profile:");
        System.out.println("  • Monthly Income: 60,000 UAH");
        System.out.println("  • Credit Score: 800");
        System.out.println("  • Requested Amount: 300,000 UAH");
        System.out.println("  • Term: 60 months");
        System.out.println("Preferences:");
        System.out.println("  ✓ Early repayment allowed");
        System.out.println("  ✓ Credit line increase allowed");
        System.out.println("\nFinding optimal credit that matches all criteria...\n");
        Client demoClient2 = new Client("DEMO2", "Jane", "Smith", "jane@demo.com",
                new BigDecimal("60000"), 800, false);
        var optimal = creditSelectionService.selectOptimalCredit(demoClient2,
                new BigDecimal("300000"), 60, true, true);
        if (optimal.isPresent()) {
            displayCreditDetails(optimal.get(), "Optimal Credit Recommendation");
            System.out.println("\n✓ This credit matches all client preferences and has the best terms!");
        } else {
            System.out.println("No optimal credit found for this criteria.");
        }
        pause(4000);

        // Demo 5: Search by credit type
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║ [DEMO 5] Searching Credits by Type (MORTGAGE)             ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("Filtering all mortgage credits from the system...\n");
        List<Credit> mortgages = creditSelectionService.selectByCreditType("MORTGAGE");
        displayCredits(mortgages, "Mortgage Credits");
        if (!mortgages.isEmpty() && mortgages.get(0) instanceof MortgageCredit) {
            MortgageCredit m = (MortgageCredit) mortgages.get(0);
            System.out.println("\nExample Mortgage Details:");
            System.out.println("  • Property Value: " + m.getPropertyValue() + " UAH");
            System.out.println("  • Down Payment: " + m.getDownPayment() + " UAH");
            System.out.println("  • Property Type: " + m.getPropertyType());
            System.out.println("  • Loan-to-Value Ratio: " + m.calculateLoanToValue() + "%");
        }
        pause(4000);

        // Demo 6: Search by bank
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║ [DEMO 6] Searching Credits by Bank (PrivatBank)         ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("Finding all credits offered by PrivatBank...\n");
        List<Credit> bankCredits = creditSelectionService.selectByBank("PrivatBank");
        displayCredits(bankCredits, "PrivatBank Credits");
        pause(3000);

        // Demo 7: Lowest interest rate
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║ [DEMO 7] Finding Credits with Lowest Interest Rate       ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("Searching for credits with the lowest interest rates");
            System.out.println("Amount range: 100,000 - 500,000 UAH\n");
            List<Credit> lowRate = creditSearchService.findWithLowestInterestRate(
                    new BigDecimal("100000"), new BigDecimal("500000"));
            displayCredits(lowRate, "Lowest Interest Rate Credits");
            pause(4000);
        } catch (Exception e) {
            System.err.println("Error in DEMO 7: " + e.getMessage());
        }

        // Demo 8: Best bank rating
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║ [DEMO 8] Finding Credits with Best Bank Rating           ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("Finding credits from banks with the highest ratings");
            System.out.println("Minimum amount: 100,000 UAH\n");
            List<Credit> bestRating = creditSearchService.findWithBestBankRating(new BigDecimal("100000"));
            displayCredits(bestRating, "Best Bank Rating Credits");
            pause(4000);
        } catch (Exception e) {
            System.err.println("Error in DEMO 8: " + e.getMessage());
        }

        // Demo 9: Flexible credits
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║ [DEMO 9] Finding Flexible Credits                        ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("Searching for credits that offer flexibility:");
            System.out.println("  • Early repayment allowed, OR");
            System.out.println("  • Credit line increase allowed");
            System.out.println("Minimum amount: 50,000 UAH\n");
            List<Credit> flexible = creditSearchService.findFlexibleCredits(new BigDecimal("50000"));
            displayCredits(flexible, "Flexible Credits");
            pause(4000);
        } catch (Exception e) {
            System.err.println("Error in DEMO 9: " + e.getMessage());
        }

        // Demo 10: Car credit specific
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║ [DEMO 10] Car Credit Details & Calculations              ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("Displaying detailed information about car credits, including");
            System.out.println("car specifications and depreciation rate calculations.\n");
            List<Credit> carCredits = creditSelectionService.selectByCreditType("CAR");
            for (Credit credit : carCredits) {
                if (credit instanceof CarCredit) {
                    CarCredit carCredit = (CarCredit) credit;
                    System.out.println("\n" + "═".repeat(55));
                    System.out.println("Car Credit ID: " + carCredit.getId());
                    System.out.println("Bank: " + carCredit.getBank().getName() + 
                            " (Rating: " + carCredit.getBank().getRating() + ")");
                    System.out.println("───────────────────────────────────────────────────────────");
                    System.out.println("Vehicle Information:");
                    System.out.println("  • Brand & Model: " + carCredit.getCarBrand() + " " + carCredit.getCarModel());
                    System.out.println("  • Year: " + carCredit.getCarYear());
                    System.out.println("  • New Car: " + (carCredit.isNewCar() ? "Yes" : "No"));
                    System.out.println("  • Car Value: " + carCredit.getCarValue() + " UAH");
                    System.out.println("Credit Terms:");
                    System.out.println("  • Loan Amount: " + carCredit.getAmount() + " UAH");
                    System.out.println("  • Interest Rate: " + carCredit.getInterestRate() + "%");
                    System.out.println("  • Term: " + carCredit.getTermMonths() + " months");
                    System.out.println("  • Monthly Payment: " + carCredit.calculateMonthlyPayment() + " UAH");
                    System.out.println("  • Total Payment: " + carCredit.calculateTotalPayment() + " UAH");
                    System.out.println("Calculations:");
                    System.out.println("  • Depreciation Rate: " + carCredit.calculateDepreciationRate() + "%");
                    System.out.println("  • Early Repayment: " + (carCredit.isEarlyRepaymentAllowed() ? "Allowed" : "Not Allowed"));
                    System.out.println("  • Credit Line Increase: " + (carCredit.isCreditLineIncreaseAllowed() ? "Allowed" : "Not Allowed"));
                }
            }
            pause(4000);
        } catch (Exception e) {
            System.err.println("Error in DEMO 10: " + e.getMessage());
        }

        // Demo 11: Consumer Credit Details
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║ [DEMO 11] Consumer Credit Details                        ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("Displaying consumer credit information with purpose and limits.\n");
            List<Credit> consumerCredits = creditSelectionService.selectByCreditType("CONSUMER");
            for (Credit credit : consumerCredits) {
                if (credit instanceof ConsumerCredit) {
                    ConsumerCredit consumer = (ConsumerCredit) credit;
                    System.out.println("\n" + "═".repeat(55));
                    System.out.println("Consumer Credit ID: " + consumer.getId());
                    System.out.println("Bank: " + consumer.getBank().getName());
                    System.out.println("───────────────────────────────────────────────────────────");
                    System.out.println("  • Purpose: " + consumer.getPurpose());
                    System.out.println("  • Amount: " + consumer.getAmount() + " UAH");
                    System.out.println("  • Max Amount: " + consumer.getMaxAmount() + " UAH");
                    System.out.println("  • Interest Rate: " + consumer.getInterestRate() + "%");
                    System.out.println("  • Requires Collateral: " + (consumer.isRequiresCollateral() ? "Yes" : "No"));
                    System.out.println("  • Within Limit: " + (consumer.isAmountWithinLimit() ? "Yes" : "No"));
                }
            }
            pause(4000);
        } catch (Exception e) {
            System.err.println("Error in DEMO 11: " + e.getMessage());
        }

        // Summary
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    DEMO SUMMARY                            ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("\nSystem Statistics:");
        System.out.println("  • Total Banks: " + bankRepository.findAll().size());
        System.out.println("  • Total Credits: " + creditRepository.findAll().size());
        System.out.println("  • Mortgage Credits: " + creditSelectionService.selectByCreditType("MORTGAGE").size());
        System.out.println("  • Consumer Credits: " + creditSelectionService.selectByCreditType("CONSUMER").size());
        System.out.println("  • Car Credits: " + creditSelectionService.selectByCreditType("CAR").size());
        
        System.out.println("\nFeatures Demonstrated:");
        System.out.println("  ✓ Bank management and rating system");
        System.out.println("  ✓ Multiple credit types (Mortgage, Consumer, Car)");
        System.out.println("  ✓ Intelligent credit search and filtering");
        System.out.println("  ✓ Optimal credit selection with preferences");
        System.out.println("  ✓ Interest rate and payment calculations");
        System.out.println("  ✓ Credit-specific calculations (LTV, Depreciation)");
        System.out.println("  ✓ Flexible credit options (early repayment, line increase)");
        System.out.println("  ✓ Data persistence (save/load functionality)");
        
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║         DEMO MODE COMPLETED SUCCESSFULLY!                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
            saveData();
            System.out.println("\n✓ All data has been saved to disk.");
            System.out.println("Press Enter to exit...");
            scanner.nextLine();
        } catch (Exception e) {
            System.err.println("\n❌ Error in demo mode: " + e.getMessage());
            e.printStackTrace();
            saveData(); // Спробуємо зберегти дані навіть при помилці
        }
    }

    // Просто пауза для демо режиму
    private void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Пошук найкращих пропозицій
    private void handleSearch() {
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║              SEARCH FOR BEST CREDIT OFFERS               ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("\nThis will find the top 5 best credit offers based on:");
            System.out.println("  • Interest rate (lower is better)");
            System.out.println("  • Bank rating (higher is better)");
            System.out.println("  • Client eligibility\n");
            
            // Читаємо суму
            System.out.print("Enter requested amount (UAH): ");
            String amountStr = scanner.nextLine().trim();
            if (amountStr.isEmpty()) {
                System.out.println("❌ Invalid amount. Please enter a number.");
                return;
            }
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("❌ Amount must be greater than zero.");
                return;
            }
            
            // Читаємо термін
            System.out.print("Enter term in months: ");
            String termStr = scanner.nextLine().trim();
            if (termStr.isEmpty()) {
                System.out.println("❌ Invalid term. Please enter a number.");
                return;
            }
            int term = Integer.parseInt(termStr);
            if (term <= 0) {
                System.out.println("❌ Term must be greater than zero.");
                return;
            }

            // Створюємо клієнта
            System.out.println("\nNow enter your client information:");
            Client client = createClientFromInput();
            if (client == null) {
                return;
            }

            // Шукаємо найкращі пропозиції
            System.out.println("\n🔍 Searching for best offers...\n");
            List<Credit> results = creditSearchService.findBestOffers(client, amount, term);
            if (results.isEmpty()) {
                System.out.println("❌ No suitable credits found for your criteria.");
                System.out.println("   Try adjusting the amount or term, or check your credit score.");
            } else {
                displayCredits(results, "Best Credit Offers");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format. Please enter numeric values only.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Знаходимо один найкращий кредит з урахуванням переваг
    private void handleSelection() {
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║              FIND OPTIMAL CREDIT                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("\nThis will find the single best credit that matches your preferences.");
            System.out.println("The system considers interest rate, bank rating, and your preferences.\n");
            
            // Зчитуємо параметри кредиту
            System.out.print("Enter requested amount (UAH): ");
            String amountStr = scanner.nextLine().trim();
            if (amountStr.isEmpty()) {
                System.out.println("❌ Invalid amount. Please enter a number.");
                return;
            }
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("❌ Amount must be greater than zero.");
                return;
            }
            
            System.out.print("Enter term in months: ");
            String termStr = scanner.nextLine().trim();
            if (termStr.isEmpty()) {
                System.out.println("❌ Invalid term. Please enter a number.");
                return;
            }
            int term = Integer.parseInt(termStr);
            if (term <= 0) {
                System.out.println("❌ Term must be greater than zero.");
                return;
            }

            // Зчитуємо переваги клієнта - ці параметри впливають на сортування
            System.out.println("\nCredit Preferences:");
            System.out.print("Prefer early repayment? (y/n): ");
            String earlyRepay = scanner.nextLine().trim().toLowerCase();
            boolean preferEarlyRepayment = "y".equals(earlyRepay) || "yes".equals(earlyRepay);
            
            System.out.print("Prefer credit line increase? (y/n): ");
            String lineIncrease = scanner.nextLine().trim().toLowerCase();
            boolean preferCreditLineIncrease = "y".equals(lineIncrease) || "yes".equals(lineIncrease);

            System.out.println("\nNow enter your client information:");
            Client client = createClientFromInput();
            if (client == null) {
                return;
            }

            // Знаходимо найкращий кредит
            System.out.println("\n🔍 Finding optimal credit...\n");
            var optimal = creditSelectionService.selectOptimalCredit(client, amount, term, 
                    preferEarlyRepayment, preferCreditLineIncrease);

            if (optimal.isPresent()) {
                displayCreditDetails(optimal.get(), "Optimal Credit Recommendation");
                System.out.println("\n✓ This is the best credit matching your criteria!");
            } else {
                System.out.println("\n❌ No suitable credit found for your criteria.");
                System.out.println("   Try adjusting your preferences or check your eligibility.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format. Please enter numeric values only.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void handleViewAll() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║              ALL AVAILABLE CREDITS                        ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        List<Credit> credits = creditRepository.findAll();
        if (credits.isEmpty()) {
            System.out.println("\n❌ No credits available in the system.");
        } else {
            System.out.println("\nTotal credits in system: " + credits.size() + "\n");
            displayCredits(credits, "All Available Credits");
        }
    }

    private void handleViewAllBanks() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║              ALL AVAILABLE BANKS                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        List<Bank> banks = bankRepository.findAll();
        if (banks.isEmpty()) {
            System.out.println("\n❌ No banks available");
        } else {
            System.out.println("\nTotal banks: " + banks.size());
            System.out.println("───────────────────────────────────────────────────────────");
            banks.forEach(bank -> {
                System.out.println(String.format("  %-25s | License: %-10s | Rating: %.1f/5.0",
                        bank.getName(), bank.getLicenseNumber(), bank.getRating()));
            });
            System.out.println("\nNote: Bank rating is on a scale of 0.0 to 5.0");
        }
    }

    private void handleSearchByType() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║              SEARCH BY CREDIT TYPE                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("\nAvailable credit types:");
        System.out.println("  • CONSUMER - For personal expenses, education, etc.");
        System.out.println("  • MORTGAGE - For purchasing real estate");
        System.out.println("  • CAR - For purchasing vehicles");
        System.out.print("\nEnter credit type: ");
        String type = scanner.nextLine().trim().toUpperCase();
        
        if (!type.equals("CONSUMER") && !type.equals("MORTGAGE") && !type.equals("CAR")) {
            System.out.println("❌ Invalid credit type. Please enter CONSUMER, MORTGAGE, or CAR.");
            return;
        }
        
        System.out.println("\n🔍 Searching for " + type + " credits...\n");
        List<Credit> credits = creditSelectionService.selectByCreditType(type);
        if (credits.isEmpty()) {
            System.out.println("❌ No " + type + " credits found.");
        } else {
            displayCredits(credits, "Credits of type: " + type);
        }
    }

    private void handleSearchByBank() {
        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║              SEARCH BY BANK                               ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println("\nAvailable banks:");
        bankRepository.findAll().forEach(bank -> 
            System.out.println("  • " + bank.getName() + " (Rating: " + bank.getRating() + ")"));
        System.out.print("\nEnter bank name: ");
        String bankName = scanner.nextLine().trim();
        
        if (bankName.isEmpty()) {
            System.out.println("❌ Bank name cannot be empty.");
            return;
        }
        
        System.out.println("\n🔍 Searching for credits from " + bankName + "...\n");
        List<Credit> credits = creditSelectionService.selectByBank(bankName);
        if (credits.isEmpty()) {
            System.out.println("❌ No credits found from " + bankName + ".");
            System.out.println("   Please check the bank name spelling.");
        } else {
            displayCredits(credits, "Credits from: " + bankName);
        }
    }

    private void handleLowestInterestRate() {
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║        FIND CREDITS WITH LOWEST INTEREST RATE            ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("\nEnter the amount range to search within:\n");
            
            System.out.print("Enter minimum amount (UAH): ");
            String minStr = scanner.nextLine().trim();
            if (minStr.isEmpty()) {
                System.out.println("❌ Invalid amount. Please enter a number.");
                return;
            }
            BigDecimal minAmount = new BigDecimal(minStr);
            if (minAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("❌ Minimum amount must be greater than zero.");
                return;
            }
            
            System.out.print("Enter maximum amount (UAH): ");
            String maxStr = scanner.nextLine().trim();
            if (maxStr.isEmpty()) {
                System.out.println("❌ Invalid amount. Please enter a number.");
                return;
            }
            BigDecimal maxAmount = new BigDecimal(maxStr);
            if (maxAmount.compareTo(minAmount) < 0) {
                System.out.println("❌ Maximum amount must be greater than or equal to minimum amount.");
                return;
            }
            
            System.out.println("\n🔍 Searching for credits with lowest interest rates...\n");
            List<Credit> credits = creditSearchService.findWithLowestInterestRate(minAmount, maxAmount);
            if (credits.isEmpty()) {
                System.out.println("❌ No credits found in the specified amount range.");
            } else {
                displayCredits(credits, "Credits with Lowest Interest Rate");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format. Please enter numeric values only.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void handleBestBankRating() {
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║        FIND CREDITS WITH BEST BANK RATING                ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("\nThis will find credits from banks with the highest ratings.\n");
            
            System.out.print("Enter minimum amount (UAH): ");
            String minStr = scanner.nextLine().trim();
            if (minStr.isEmpty()) {
                System.out.println("❌ Invalid amount. Please enter a number.");
                return;
            }
            BigDecimal minAmount = new BigDecimal(minStr);
            if (minAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("❌ Minimum amount must be greater than zero.");
                return;
            }
            
            System.out.println("\n🔍 Searching for credits with best bank ratings...\n");
            List<Credit> credits = creditSearchService.findWithBestBankRating(minAmount);
            if (credits.isEmpty()) {
                System.out.println("❌ No credits found with the specified minimum amount.");
            } else {
                displayCredits(credits, "Credits with Best Bank Rating");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format. Please enter numeric values only.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void handleFlexibleCredits() {
        try {
            System.out.println("\n╔══════════════════════════════════════════════════════════╗");
            System.out.println("║              FIND FLEXIBLE CREDITS                       ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝");
            System.out.println("\nFlexible credits offer:");
            System.out.println("  • Early repayment allowed, OR");
            System.out.println("  • Credit line increase allowed");
            System.out.println("\nThese features provide more flexibility in managing your credit.\n");
            
            System.out.print("Enter minimum amount (UAH): ");
            String minStr = scanner.nextLine().trim();
            if (minStr.isEmpty()) {
                System.out.println("❌ Invalid amount. Please enter a number.");
                return;
            }
            BigDecimal minAmount = new BigDecimal(minStr);
            if (minAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("❌ Minimum amount must be greater than zero.");
                return;
            }
            
            System.out.println("\n🔍 Searching for flexible credits...\n");
            List<Credit> credits = creditSearchService.findFlexibleCredits(minAmount);
            if (credits.isEmpty()) {
                System.out.println("❌ No flexible credits found with the specified minimum amount.");
            } else {
                displayCredits(credits, "Flexible Credits (Early Repayment/Line Increase)");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format. Please enter numeric values only.");
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private void displayCredits(List<Credit> credits, String title) {
        if (credits.isEmpty()) {
            System.out.println("\nNo credits found.");
            return;
        }
        
        System.out.println("\n" + title + " (" + credits.size() + " found):");
        System.out.println("───────────────────────────────────────────────────────────");
        for (int i = 0; i < credits.size(); i++) {
            Credit credit = credits.get(i);
            if (credit != null && credit.getBank() != null && credit.getAmount() != null && 
                    credit.getInterestRate() != null) {
                System.out.println(String.format("\n%d. Credit ID: %s", i + 1, credit.getId()));
                System.out.println(String.format("   Bank: %s (Rating: %.1f)", 
                        credit.getBank().getName(), credit.getBank().getRating()));
                System.out.println(String.format("   Type: %s", credit.getCreditType()));
                System.out.println(String.format("   Amount: %s", credit.getAmount()));
                System.out.println(String.format("   Interest Rate: %s%%", credit.getInterestRate()));
                System.out.println(String.format("   Term: %d months", credit.getTermMonths()));
                System.out.println(String.format("   Monthly Payment: %s", 
                        credit.calculateMonthlyPayment()));
                System.out.println(String.format("   Total Payment: %s", 
                        credit.calculateTotalPayment()));
                System.out.println(String.format("   Early Repayment: %s", 
                        credit.isEarlyRepaymentAllowed() ? "Yes" : "No"));
                System.out.println(String.format("   Credit Line Increase: %s", 
                        credit.isCreditLineIncreaseAllowed() ? "Yes" : "No"));
                
                // Show specific details for different credit types
                if (credit instanceof CarCredit) {
                    CarCredit carCredit = (CarCredit) credit;
                    System.out.println(String.format("   Car: %s %s (%d)", 
                            carCredit.getCarBrand(), carCredit.getCarModel(), carCredit.getCarYear()));
                } else if (credit instanceof MortgageCredit) {
                    MortgageCredit mortgage = (MortgageCredit) credit;
                    System.out.println(String.format("   Property Value: %s", mortgage.getPropertyValue()));
                    System.out.println(String.format("   Property Type: %s", mortgage.getPropertyType()));
                } else if (credit instanceof ConsumerCredit) {
                    ConsumerCredit consumer = (ConsumerCredit) credit;
                    System.out.println(String.format("   Purpose: %s", consumer.getPurpose()));
                }
            }
        }
    }

    private void displayCreditDetails(Credit credit, String title) {
        if (credit == null) {
            System.out.println("No credit to display.");
            return;
        }
        
        System.out.println("\n" + title + ":");
        System.out.println("───────────────────────────────────────────────────────────");
        System.out.println(String.format("  Credit ID: %s", credit.getId()));
        System.out.println(String.format("  Bank: %s (Rating: %.1f)", 
                credit.getBank().getName(), credit.getBank().getRating()));
        System.out.println(String.format("  Type: %s", credit.getCreditType()));
        System.out.println(String.format("  Amount: %s", credit.getAmount()));
        System.out.println(String.format("  Interest Rate: %s%%", credit.getInterestRate()));
        System.out.println(String.format("  Term: %d months", credit.getTermMonths()));
        System.out.println(String.format("  Monthly Payment: %s", credit.calculateMonthlyPayment()));
        System.out.println(String.format("  Total Payment: %s", credit.calculateTotalPayment()));
        System.out.println(String.format("  Early Repayment: %s", 
                credit.isEarlyRepaymentAllowed() ? "Yes" : "No"));
        System.out.println(String.format("  Credit Line Increase: %s", 
                credit.isCreditLineIncreaseAllowed() ? "Yes" : "No"));
    }

    // Створюємо клієнта з введених даних
    private Client createClientFromInput() {
        try {
            System.out.print("Enter monthly income (UAH): ");
            String incomeStr = scanner.nextLine().trim();
            if (incomeStr.isEmpty()) {
                System.out.println("❌ Invalid income. Please enter a number.");
                return null;
            }
            BigDecimal income = new BigDecimal(incomeStr);
            if (income.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("❌ Income must be greater than zero.");
                return null;
            }
            
            System.out.print("Enter credit score (300-850): ");
            String scoreStr = scanner.nextLine().trim();
            if (scoreStr.isEmpty()) {
                System.out.println("❌ Invalid credit score. Please enter a number.");
                return null;
            }
            int score = Integer.parseInt(scoreStr);
            if (score < 300 || score > 850) {
                System.out.println("⚠️  Warning: Credit score should typically be between 300-850.");
            }

            return new Client("CLI001", "John", "Doe", "john@example.com",
                    income, score, false);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format. Please enter numeric values only.");
            return null;
        }
    }

    // Зберігаємо дані в файли
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


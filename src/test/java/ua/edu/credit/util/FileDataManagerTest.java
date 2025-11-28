package ua.edu.credit.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.edu.credit.model.Bank;
import ua.edu.credit.model.Credit;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileDataManagerTest {

    private FileDataManager fileDataManager;
    private static final String TEST_CREDITS_FILE = "data/test_credits.json";
    private static final String TEST_BANKS_FILE = "data/test_banks.json";

    @BeforeEach
    void setUp() {
        fileDataManager = new FileDataManager();
        cleanupTestFiles();
    }

    private void cleanupTestFiles() {
        new File(TEST_CREDITS_FILE).delete();
        new File(TEST_BANKS_FILE).delete();
        new File("data").delete();
    }

    @Test
    void testSaveAndLoadBanks() throws IOException {
        List<Bank> banks = new ArrayList<>();
        banks.add(new Bank("Bank1", "B001", 4.5));
        banks.add(new Bank("Bank2", "B002", 4.8));

        fileDataManager.saveBanks(banks);
        List<Bank> loadedBanks = fileDataManager.loadBanks();

        assertEquals(2, loadedBanks.size());
        assertEquals("Bank1", loadedBanks.get(0).getName());
        assertEquals("Bank2", loadedBanks.get(1).getName());
    }

    @Test
    void testSaveAndLoadCredits() throws IOException {
        List<Credit> credits = new ArrayList<>();
        Bank bank = new Bank("TestBank", "TB001", 4.5);
        credits.add(new Credit("CR001", bank, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));
        credits.add(new Credit("CR002", bank, "MORTGAGE", new BigDecimal("500000"),
                new BigDecimal("12.0"), 240, true, true));

        fileDataManager.saveCredits(credits);
        List<Credit> loadedCredits = fileDataManager.loadCredits();

        assertEquals(2, loadedCredits.size());
        assertEquals("CR001", loadedCredits.get(0).getId());
        assertEquals("CR002", loadedCredits.get(1).getId());
    }

    @Test
    void testLoadBanksWhenFileNotExists() throws IOException {
        List<Bank> banks = fileDataManager.loadBanks();
        assertNotNull(banks);
        assertTrue(banks.isEmpty());
    }

    @Test
    void testLoadCreditsWhenFileNotExists() throws IOException {
        List<Credit> credits = fileDataManager.loadCredits();
        assertNotNull(credits);
        assertTrue(credits.isEmpty());
    }

    @Test
    void testSaveEmptyBanksList() throws IOException {
        fileDataManager.saveBanks(new ArrayList<>());
        List<Bank> loadedBanks = fileDataManager.loadBanks();
        assertTrue(loadedBanks.isEmpty());
    }

    @Test
    void testSaveEmptyCreditsList() throws IOException {
        fileDataManager.saveCredits(new ArrayList<>());
        List<Credit> loadedCredits = fileDataManager.loadCredits();
        assertTrue(loadedCredits.isEmpty());
    }

    @Test
    void testSaveBanksCreatesDirectory() throws IOException {
        List<Bank> banks = new ArrayList<>();
        banks.add(new Bank("TestBank", "TB001", 4.5));

        fileDataManager.saveBanks(banks);

        File dataDir = new File("data");
        assertTrue(dataDir.exists() || dataDir.isDirectory());
    }

    @Test
    void testSaveCreditsCreatesDirectory() throws IOException {
        List<Credit> credits = new ArrayList<>();
        Bank bank = new Bank("TestBank", "TB001", 4.5);
        credits.add(new Credit("CR001", bank, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));

        fileDataManager.saveCredits(credits);

        File dataDir = new File("data");
        assertTrue(dataDir.exists() || dataDir.isDirectory());
    }

    @Test
    void testMultipleSaveAndLoadBanks() throws IOException {
        List<Bank> banks1 = new ArrayList<>();
        banks1.add(new Bank("Bank1", "B001", 4.5));
        fileDataManager.saveBanks(banks1);

        List<Bank> banks2 = new ArrayList<>();
        banks2.add(new Bank("Bank2", "B002", 4.8));
        fileDataManager.saveBanks(banks2);

        List<Bank> loadedBanks = fileDataManager.loadBanks();
        assertEquals(1, loadedBanks.size());
        assertEquals("Bank2", loadedBanks.get(0).getName());
    }

    @Test
    void testMultipleSaveAndLoadCredits() throws IOException {
        Bank bank = new Bank("TestBank", "TB001", 4.5);

        List<Credit> credits1 = new ArrayList<>();
        credits1.add(new Credit("CR001", bank, "CONSUMER", new BigDecimal("100000"),
                new BigDecimal("15.0"), 60, true, false));
        fileDataManager.saveCredits(credits1);

        List<Credit> credits2 = new ArrayList<>();
        credits2.add(new Credit("CR002", bank, "MORTGAGE", new BigDecimal("500000"),
                new BigDecimal("12.0"), 240, true, true));
        fileDataManager.saveCredits(credits2);

        List<Credit> loadedCredits = fileDataManager.loadCredits();
        assertEquals(1, loadedCredits.size());
        assertEquals("CR002", loadedCredits.get(0).getId());
    }
}


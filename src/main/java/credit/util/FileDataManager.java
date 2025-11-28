package credit.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import credit.model.Bank;
import credit.model.Credit;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileDataManager {
    private static final String CREDITS_FILE = "data/credits.json";
    private static final String BANKS_FILE = "data/banks.json";
    private Gson gson;

    public FileDataManager() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public void saveCredits(List<Credit> credits) throws IOException {
        java.io.File file = new java.io.File(CREDITS_FILE);
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(credits, writer);
        }
    }

    public List<Credit> loadCredits() throws IOException {
        java.io.File file = new java.io.File(CREDITS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Credit>>(){}.getType();
            List<Credit> credits = gson.fromJson(reader, listType);
            return credits != null ? credits : new ArrayList<>();
        }
    }

    public void saveBanks(List<Bank> banks) throws IOException {
        java.io.File file = new java.io.File(BANKS_FILE);
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(banks, writer);
        }
    }

    public List<Bank> loadBanks() throws IOException {
        java.io.File file = new java.io.File(BANKS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Bank>>(){}.getType();
            List<Bank> banks = gson.fromJson(reader, listType);
            return banks != null ? banks : new ArrayList<>();
        }
    }
}


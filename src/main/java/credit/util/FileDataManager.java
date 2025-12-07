package credit.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import credit.model.Bank;
import credit.model.Credit;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileDataManager {
    private static final String CREDITS_FILE = "data/credits.json";
    private static final String BANKS_FILE = "data/banks.json";
    private Gson gson;

    public FileDataManager() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.format(formatter));
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString(), formatter);
        }
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


package de.silencio.better_beacon_range;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class BeaconConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger("better_beacon_range");

    private static final Path CONFIG_PATH = Path.of("config", "better_beacon_range.json");
    private double base = 10.0; // Minecraft defaults
    private double perLevel = 10.0; // Minecraft defaults
    private boolean belowInfinite = false; // Minecraft defaults

    public void loadConfig() {
        // Check if the config file exists
        if (!CONFIG_PATH.toFile().exists()) {
            createDefaultConfig();
        }

        // Now try to read the config file
        try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
            JsonObject configJson = JsonParser.parseReader(reader).getAsJsonObject();

            if (configJson.has("base")) {
                this.base = configJson.get("base").getAsDouble();
            } else {
                configJson.addProperty("base", base);
                updateConfig(configJson);
            }

            if (configJson.has("perLevel")) {
                this.perLevel = configJson.get("perLevel").getAsDouble();
            } else {
                configJson.addProperty("perLevel", perLevel);
                updateConfig(configJson);
            }

            if (configJson.has("belowInfinite")) {
                this.belowInfinite = configJson.get("belowInfinite").getAsBoolean();
            } else {
                configJson.addProperty("belowInfinite", belowInfinite);
                updateConfig(configJson);
            }

            LOGGER.info("Loaded Beacon Config: base = {}, perLevel = {}, belowInfinite = {}", base, perLevel, belowInfinite);
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.warn("Failed to load config, using default values.");
        }
    }

    private void updateConfig(JsonObject configJson) {
        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
            writer.write(configJson.toString());
            LOGGER.info("Updated better beacon range config.");
        } catch (IOException e) {
            LOGGER.error("Failed to update default config.");
        }
    }

    private void createDefaultConfig() {
        JsonObject defaultConfig = new JsonObject();
        defaultConfig.addProperty("base", base);
        defaultConfig.addProperty("perLevel", perLevel);
        defaultConfig.addProperty("belowInfinite", belowInfinite);

        // Write the default config to the file
        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
            writer.write(defaultConfig.toString());
            LOGGER.info("Created default config: {}", CONFIG_PATH);
        } catch (IOException e) {
            LOGGER.error("Failed to create default config.");
        }
    }

    public double getBase() {
        return base;
    }

    public double getPerLevel() {
        return perLevel;
    }

    public boolean isBelowInfinite() {
        return belowInfinite;
    }
}

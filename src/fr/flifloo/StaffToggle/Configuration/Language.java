package fr.flifloo.StaffToggle.Configuration;

import fr.flifloo.StaffToggle.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Language{
    public FileConfiguration config;

    public Language (Main main) {
        String configLanguage = main.getConfig().getString("language");
        InputStream resourceLanguage = main.getResource("languages/" + configLanguage + ".yml");
        File configFile = new File(main.getDataFolder(),  "language.yml");

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            try {
                Files.copy(resourceLanguage, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
            if (!configLanguage.equalsIgnoreCase(config.getString("language"))) {
                Files.copy(resourceLanguage, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                config.load(configFile);
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}

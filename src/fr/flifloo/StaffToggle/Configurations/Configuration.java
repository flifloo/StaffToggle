package fr.flifloo.StaffToggle.Configurations;

import fr.flifloo.StaffToggle.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Configuration{
    public String fileName;
    public FileConfiguration config;
    public File configFile;
    InputStream resourceFile;
    private Main plugin;

    public Configuration (Main main, String name, String resourcePath) {
        this.plugin = main;
        this.fileName = name;
        resourceFile = this.plugin.getResource(resourcePath);
        configFile = new File(this.plugin.getDataFolder(),  fileName + ".yml");

        if (!this.configFile.exists()) {
            this.configFile.getParentFile().mkdirs();
            try {
                Files.copy(resourceFile, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = new YamlConfiguration();
        try {
            this.config.load(this.configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            this.plugin.getLogger().warning("Unable to save " + this.fileName); // shouldn't really happen, but save throws the exception
        }
    }

    public void set(String path, Object value) {
        this.config.set(path, value);
        this.save();
    }
}

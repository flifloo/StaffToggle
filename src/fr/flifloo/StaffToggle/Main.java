package fr.flifloo.StaffToggle;

import fr.flifloo.StaffToggle.Commands.*;
import fr.flifloo.StaffToggle.Configuration.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin {
    public Language language;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getConfig().getString("language");
        language = new Language(this);
        registerCommands();
    }

    @Override
    public void onDisable() {

    }

    public void registerCommands() {
        getCommand("staff").setExecutor(new Staff(this));
    }
}

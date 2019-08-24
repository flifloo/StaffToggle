package fr.flifloo.StaffToggle;

import fr.flifloo.StaffToggle.Commands.Staff;
import fr.flifloo.StaffToggle.Configurations.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public Language language;
    public StaffConf staffConf;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        language = new Language(this);
        staffConf = new StaffConf(this);
        registerCommands();
    }

    @Override
    public void onDisable() {

    }

    public void registerCommands() {
        getCommand("staff").setExecutor(new Staff(this));
    }
}

package fr.flifloo.StaffToggle;

import fr.flifloo.StaffToggle.Commands.Staff;
import fr.flifloo.StaffToggle.Configurations.*;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public Language language;
    public StaffConf staffConf;
    public LuckPerms luckPerms;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        language = new Language(this);
        staffConf = new StaffConf(this);
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }
        registerCommands();
    }

    @Override
    public void onDisable() {
        staffConf.save();
    }

    public void registerCommands() {
        getCommand("staff").setExecutor(new Staff(this));
    }
}

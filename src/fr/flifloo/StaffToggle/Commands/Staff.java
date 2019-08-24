package fr.flifloo.StaffToggle.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.flifloo.StaffToggle.Main;

public class Staff implements CommandExecutor {
    private Main plugin;
    private FileConfiguration language;
    private FileConfiguration staffConf;

    public Staff(Main plugin) {
        this.plugin = plugin;
        language = plugin.language.config;
        staffConf = plugin.staffConf.config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String message;
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Command only for players !");
            return false;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            message = language.getString("staff.help");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        else if (args[0].equalsIgnoreCase("modo") || args[0].equalsIgnoreCase("admin")) {
            Player  player = (Player) sender;
            if (!staffConf.isSet("players." + player.getUniqueId())) {
                player.sendMessage("Not config yet !");
                staffConf.set("players." + player.getUniqueId(), "test");
                plugin.staffConf.save();
            } else {
                player.sendMessage("Fond config !");
            }
        }
        else {
            message = language.getString("unknown");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return false;
        }
        return true;
    }
}

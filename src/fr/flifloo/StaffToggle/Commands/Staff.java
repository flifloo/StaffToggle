package fr.flifloo.StaffToggle.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.flifloo.StaffToggle.Main;

public class Staff implements CommandExecutor {
    Main plugin;

    public Staff(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender player, Command cmd, String label, String[] args) {
        if (!(player instanceof Player)) {
            player.sendMessage(ChatColor.RED + "Command only for players !");
            return false;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            String message = plugin.language.config.getString("staff.help");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        else if (args[0].equalsIgnoreCase("modo")) {
            player.sendMessage(ChatColor.GOLD + "Switch modo !");
        }
        else if (args[0].equalsIgnoreCase("admin")) {
            player.sendMessage(ChatColor.GOLD + "Switch admin  !");
        }
        return true;
    }
}

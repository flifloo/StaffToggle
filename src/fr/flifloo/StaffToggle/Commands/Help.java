package fr.flifloo.StaffToggle.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.flifloo.StaffToggle.Main;

public class Help implements CommandExecutor {
    Main plugin;

    public Help(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender player, Command cmd, String label, String[] args) {
        player.sendMessage(ChatColor.AQUA + "StaffToogle help !");
        return true;
    }
}

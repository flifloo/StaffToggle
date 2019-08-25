package fr.flifloo.StaffToggle.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.flifloo.StaffToggle.Main;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

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
        Player  player = (Player) sender;
        PlayerInventory playerIvt = player.getInventory();
        String playerConf = "players." + player.getUniqueId();
        String state = playerConf + ".state";
        String armor = playerConf + ".inventory" + ".armor";
        String content = playerConf + ".inventory" + "content";

        if (!staffConf.isSet(state)) {
            plugin.staffConf.set(state, false);
        }
        if (!staffConf.getBoolean(state)){
            plugin.staffConf.set(state, true);
            plugin.staffConf.set(armor, playerIvt.getArmorContents());
            plugin.staffConf.set(content, playerIvt.getContents());
            playerIvt.clear();
            player.sendMessage("On");
        } else {
            plugin.staffConf.set(state, false);
            playerIvt.clear();
            ItemStack[] backupIvt = (ItemStack[]) staffConf.get(armor);
            playerIvt.setArmorContents(backupIvt);
            backupIvt = (ItemStack[]) staffConf.get(content);
            playerIvt.setContents(backupIvt);
            player.sendMessage("Off");
        }

        return true;
    }
}

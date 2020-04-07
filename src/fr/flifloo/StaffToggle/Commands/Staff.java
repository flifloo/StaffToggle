package fr.flifloo.StaffToggle.Commands;

import fr.flifloo.StaffToggle.Configurations.PlayerConfig;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import fr.flifloo.StaffToggle.Main;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

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
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Command only for players !");
            return false;
        }
        Player  player = (Player) sender;
        String state = "players." + player.getUniqueId() + ".state";

        if (!staffConf.isSet(state)) {
            staffConf.set(state, false);
        }

        if (!staffConf.getBoolean(state)){
            staffConf.set(state, true);
            save(player, "normal");
            restore(player, "staff");
            player.sendMessage("Staff mod enabled !");

        } else {
            staffConf.set(state, false);
            save(player, "staff");
            restore(player, "normal");
            player.sendMessage("Staff mod disabled !");
        }
        return true;
    }

    private void save(Player player, String mode) {
        PlayerInventory playerInventory = player.getInventory();
        String playerConf = "players." + player.getUniqueId() + "." + mode;
        staffConf.set(playerConf + PlayerConfig.INVENTORY.getValue(), playerInventory.getContents());
        staffConf.set(playerConf + PlayerConfig.ARMOR.getValue(), playerInventory.getArmorContents());
        staffConf.set(playerConf + PlayerConfig.LEVEL.getValue(), player.getLevel());
        staffConf.set(playerConf + PlayerConfig.EXP.getValue(), player.getExp());
        staffConf.set(playerConf + PlayerConfig.GAMEMODE.getValue(), player.getGameMode());
    }

    private void restore(Player player, String mode) {
        String playerConf = "players." + player.getUniqueId() + "." + mode;
        ItemStack[] inventory = (ItemStack[]) staffConf.get(playerConf + PlayerConfig.INVENTORY.getValue());
        ItemStack[] armor = (ItemStack[]) staffConf.get(playerConf + PlayerConfig.ARMOR.getValue());
        int level = staffConf.getInt(playerConf + PlayerConfig.LEVEL.getValue());
        Object exp = staffConf.get(playerConf + PlayerConfig.EXP.getValue());
        GameMode gameMode = (GameMode) staffConf.get(playerConf + PlayerConfig.GAMEMODE.getValue());
        PlayerInventory playerInventory = player.getInventory();
        if (inventory != null)
            playerInventory.setContents(inventory);
        if (armor != null)
            playerInventory.setArmorContents(armor);
        if (gameMode == null)
            gameMode = GameMode.SURVIVAL;
        if (exp != null)
            player.setExp((float) exp);
        player.setLevel(level);
        player.setGameMode(gameMode);
    }
}

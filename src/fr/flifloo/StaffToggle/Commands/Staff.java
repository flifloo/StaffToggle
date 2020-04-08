package fr.flifloo.StaffToggle.Commands;

import com.mysql.fabric.xmlrpc.base.Array;
import fr.flifloo.StaffToggle.Configurations.PlayerConfig;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
        language = plugin.language.getConfig();
        staffConf = plugin.staffConf.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(language.getString("notPlayer"));
            return false;
        }
        Player  player = (Player) sender;
        GroupManager groupManager = plugin.luckPerms.getGroupManager();
        String[] groups = groupManager.getLoadedGroups().stream()
                .map(Group::getName)
                .filter(t->player.hasPermission("staff.group."+t))
                .toArray(String[]::new);
        if (groups.length == 0) {
            player.sendMessage(language.getString("premissionDenide"));
            return false;
        } else if (groups.length > 1)
            Arrays.sort(groups, Comparator.comparingInt((String s) -> groupManager.getGroup(s).getWeight().isEmpty() ? 0 : groupManager.getGroup(s).getWeight().getAsInt()).reversed());

        String userGroup = groups[0];
        UserManager userManager = plugin.luckPerms.getUserManager();
        User user = userManager.getUser(player.getUniqueId());

        if (user == null)
            player.sendMessage(language.getString("userNull"));
        String state = "players." + player.getUniqueId() + PlayerConfig.STATE.getValue();

        if (!staffConf.isSet(state))
            staffConf.set(state, false);
        if (!staffConf.getBoolean(state)){
            staffConf.set(state, true);
            save(player, "normal");
            restore(player, "staff");
            if (user != null) {
                Group group = groupManager.getGroup(userGroup);
                if (group != null)
                    user.data().add(InheritanceNode.builder(group).build());
                else
                    player.sendMessage(language.getString("unknonGroup"));
            }
            player.sendMessage(language.getString("enable"));
        } else {
            staffConf.set(state, false);
            save(player, "staff");
            restore(player, "normal");
            if (user != null)
                user.getNodes().stream()
                        .filter(NodeType.INHERITANCE::matches)
                        .map(NodeType.INHERITANCE::cast)
                        .filter(t->t.getGroupName().equals(userGroup))
                        .forEach(t->user.data().remove(t));
            player.sendMessage(language.getString("disable"));
        }
        if (user != null)
            userManager.saveUser(user);
        return true;
    }

    private void save(Player player, String mode) {
        PlayerInventory playerInventory = player.getInventory();
        String playerConf = "players." + player.getUniqueId() + "." + mode;
        staffConf.set(playerConf + PlayerConfig.INVENTORY.getValue(), playerInventory.getContents());
        staffConf.set(playerConf + PlayerConfig.ARMOR.getValue(), playerInventory.getArmorContents());
        staffConf.set(playerConf + PlayerConfig.LEVEL.getValue(), player.getLevel());
        staffConf.set(playerConf + PlayerConfig.EXP.getValue(), player.getExp());
        staffConf.set(playerConf + PlayerConfig.GAMEMODE.getValue(), player.getGameMode().getValue());
    }

    private void restore(Player player, String mode) {
        String playerConf = "players." + player.getUniqueId() + "." + mode;
        ItemStack[] inventory = getItemStackConfig(playerConf + PlayerConfig.INVENTORY.getValue());
        ItemStack[] armor = getItemStackConfig(playerConf + PlayerConfig.ARMOR.getValue());
        int level = staffConf.getInt(playerConf + PlayerConfig.LEVEL.getValue());
        Object exp = staffConf.get(playerConf + PlayerConfig.EXP.getValue());
        GameMode gameMode = GameMode.getByValue(staffConf.getInt(playerConf + PlayerConfig.GAMEMODE.getValue()));
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setContents(inventory);
        playerInventory.setArmorContents(armor);
        if (exp != null) {
            if (exp.getClass() == Double.class)
                player.setExp(((Double) exp).floatValue());
            else
                player.setExp((float) exp);
        }
        player.setLevel(level);
        if (gameMode == null)
            gameMode = GameMode.SURVIVAL;
        player.setGameMode(gameMode);
    }

    private ItemStack[] getItemStackConfig(String config) {
        Object itemStackRaw = staffConf.get(config);
        ArrayList<ItemStack> itemStackArray;
        ItemStack[] itemStacks = null;
        if (itemStackRaw != null) {
            if (itemStackRaw.getClass() == ArrayList.class) {
                itemStackArray = (ArrayList<ItemStack>) itemStackRaw;
                itemStacks = new ItemStack[itemStackArray.size()];
                for (int i = 0; i<itemStackArray.size(); i++)
                    itemStacks[i] = itemStackArray.get(i);
            } else {
                itemStacks = (ItemStack[]) staffConf.get(config);
            }
        }
        return itemStacks;
    }
}

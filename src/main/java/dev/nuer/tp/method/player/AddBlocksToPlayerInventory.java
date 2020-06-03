package dev.nuer.tp.method.player;

import dev.nuer.tp.support.actionbarapi.ActionBarAPI;
import dev.nuer.tp.managers.FileManager;
import dev.nuer.tp.method.Chat;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Class that handles adding block drops to a players inventory
 */
public class AddBlocksToPlayerInventory {
    public static ArrayList<Player> messagedPlayers = new ArrayList<>();

    /**
     * Adds the drops from the specified event to the players inventory
     *
     * @param blockToBreak the event to get the drops of
     * @param player       the player who brock the block
     */
    public static void addBlocks(Block blockToBreak, Player player) {
        if (!messagedPlayers.contains(player)) {
            messagedPlayers.add(player);
            if (player.getInventory().firstEmpty() == -1) {
                if (FileManager.get("config").getBoolean("inventory-full-action-bar.enabled")) {
                    String message = FileManager.get("config").getString("inventory-full-action-bar.message");
                    ActionBarAPI.sendActionBar(player, Chat.applyColor(message));
                } else {
                    new PlayerMessage("inventory-full", player);
                }
            }
        }
        for (ItemStack item : blockToBreak.getDrops()) {
            player.getInventory().addItem(item);
        }
        blockToBreak.setType(Material.AIR);
        blockToBreak.getDrops().clear();
    }

    /**
     * Method to sell blocks, used for harvester hoes - the items are removed but not added to the players inventory
     *
     * @param blockToBreak the event to get the drops of
     * @param player       the player who brock the block
     */
    public static void sellBlocks(Block blockToBreak, Player player) {
        if (!messagedPlayers.contains(player)) {
            messagedPlayers.add(player);
            if (player.getInventory().firstEmpty() == -1) {
                if (FileManager.get("config").getBoolean("inventory-full-action-bar.enabled")) {
                    String message = FileManager.get("config").getString("inventory-full-action-bar.message");
                    ActionBarAPI.sendActionBar(player, Chat.applyColor(message));
                } else {
                    new PlayerMessage("inventory-full", player);
                }
            }
        }
        blockToBreak.setType(Material.AIR);
        blockToBreak.getDrops().clear();
    }
}
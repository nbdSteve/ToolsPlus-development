package dev.nuer.tp.listener;

import dev.nuer.tp.support.nbtapi.NBTItem;
import dev.nuer.tp.managers.FileManager;
import dev.nuer.tp.managers.ToolsAttributeManager;
import dev.nuer.tp.tools.AlterToolModifier;
import dev.nuer.tp.tools.ChangeMode;
import dev.nuer.tp.tools.chunk.ChunkQueueManipulation;
import dev.nuer.tp.tools.chunk.ChunkRemoval;
import dev.nuer.tp.tools.lightning.CreateLightningStrike;
import dev.nuer.tp.tools.multi.ChangeToolRadius;
import dev.nuer.tp.tools.sell.SellChestContents;
import dev.nuer.tp.tools.smelt.SmeltStorageContents;
import dev.nuer.tp.tools.tnt.AlterChestContents;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Class that handles plugin events triggered by a player Interacting
 */
public class PlayerInteract implements Listener {

    /**
     * Method for Lightning Wands
     *
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void playerInteractLightningWand(PlayerInteractEvent event) {
        if (!(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            return;
        //Store the player
        Player player = event.getPlayer();
        //If the players item doesn't have meta / lore, return
        if (event.getItem() == null || !event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasLore()) return;
        //Create a new nbt object
        NBTItem nbtItem = new NBTItem(event.getItem());
        //Get the type of tool being used
        try {
            if (nbtItem.getBoolean("tools+.lightning")) {
                //Store the location to Strike
                Block locationToStrike;
                //Check to target block based on where the user is looking / clicked
                if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                    locationToStrike = player.getTargetBlock(null, FileManager.get("config").getInt("lightning-reach-distance"));
                } else {
                    locationToStrike = event.getClickedBlock();
                }
                //Run the code to strike the ground
                CreateLightningStrike.createStrikeGround(player, "lightning",
                        "lightning-wands." + nbtItem.getInteger(
                                "tools+.raw.id"), locationToStrike, nbtItem);
            }
        } catch (NullPointerException e) {
            //NBT tag is null because this is not a lightning wand
        }
        try {
            if (nbtItem.getBoolean("tools+.chunk")) {
                event.setCancelled(true);
                if (player.isSneaking()) return;
                if (ChunkRemoval.pendingChunks.contains(player.getLocation().getChunk())) {
                    ChunkQueueManipulation.removeChunksFromQueue(player.getLocation().getChunk(), player,
                            ChangeToolRadius.getToolRadius(nbtItem.getItem().getItemMeta().getLore(), nbtItem.getItem(), ToolsAttributeManager.chunkToolRadiusUnique));
                } else {
                    ChunkRemoval.removeChunksInRadius(player.getLocation().getChunk(), player,
                            ChangeToolRadius.getToolRadius(nbtItem.getItem().getItemMeta().getLore(), nbtItem.getItem(), ToolsAttributeManager.chunkToolRadiusUnique));
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            //NBT tag is null because this is not a chunk tool
        }
    }

    /**
     * Method for Sell Wands
     *
     * @param event PlayerInteractEvent
     */
    @EventHandler
    public void playerLeftClickInteractWithWand(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
        //Store the player
        Player player = event.getPlayer();
        //If the players item doesn't have meta / lore, return
        if (event.getItem() == null || !event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasLore()) return;
        //Create a new nbt object
        NBTItem nbtItem = new NBTItem(player.getItemInHand());
        //Get the type of tool being used
        try {
            if (nbtItem.getBoolean("tools+.sell")) {
                if (!(event.getClickedBlock().getType().equals(Material.CHEST)
                        || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST))) {
                    return;
                }
                //Create a new block break event to ensure that the player can sell that chest
                BlockBreakEvent chestSell = new BlockBreakEvent(event.getClickedBlock(), player);
                Bukkit.getPluginManager().callEvent(chestSell);
                //Check if the event is cancelled
                if (chestSell.isCancelled()) return;
                //Run the code to sell the items
                SellChestContents.sellContents(event.getClickedBlock(), player, "sell",
                        "sell-wands." + nbtItem.getInteger("tools+.raw.id"),
                        AlterToolModifier.getCurrentModifier(nbtItem.getItem().getItemMeta().getLore(),
                                nbtItem.getItem(), true, ToolsAttributeManager.sellWandModifierUnique), nbtItem);
            }
        } catch (NullPointerException e) {
            //NBT tag is null because this is not a sell wand
        }
        try {
            if (nbtItem.getBoolean("tools+.tnt")) {
                if (!(event.getClickedBlock().getType().equals(Material.CHEST)
                        || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST))) {
                    return;
                }
                //Create a new block break event to ensure that the player can modify that chest
                BlockBreakEvent tntCraft = new BlockBreakEvent(event.getClickedBlock(), player);
                Bukkit.getPluginManager().callEvent(tntCraft);
                //Check if the event is cancelled
                if (tntCraft.isCancelled()) return;
                //Run the code for the tnt wand
                AlterChestContents.manipulateContents(event.getClickedBlock(), player, "tnt",
                        "tnt-wands." + nbtItem.getInteger("tools+.raw.id"),
                        AlterToolModifier.getCurrentModifier(nbtItem.getItem().getItemMeta().getLore(),
                                nbtItem.getItem(), true, ToolsAttributeManager.tntWandModifierUnique),
                        !ChangeMode.changeToolMode(nbtItem.getItem().getItemMeta().getLore(),
                                nbtItem.getItem().getItemMeta(), nbtItem.getItem(), ToolsAttributeManager.tntWandModeUnique, false), nbtItem);
            }
        } catch (NullPointerException e) {
            //NBT tag is null because this is not a tnt wand
        }
        try {
            if (nbtItem.getBoolean("tools+.smelt")) {
                if (!(event.getClickedBlock().getType().equals(Material.CHEST)
                        || event.getClickedBlock().getType().equals(Material.TRAPPED_CHEST))) {
                    return;
                }
                BlockBreakEvent smeltConvert = new BlockBreakEvent(event.getClickedBlock(), player);
                Bukkit.getPluginManager().callEvent(smeltConvert);
                if (smeltConvert.isCancelled()) return;
                SmeltStorageContents.smeltContents(event.getClickedBlock(), player, "smelt",
                        "smelt-wands." + nbtItem.getInteger("tools+.raw.id"), nbtItem);
            }
        } catch (NullPointerException e) {
            //NBT tag is null because this is not a tnt wand
        }
    }
}
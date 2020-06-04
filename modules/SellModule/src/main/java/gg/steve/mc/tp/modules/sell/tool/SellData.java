package gg.steve.mc.tp.modules.sell.tool;

import gg.steve.mc.tp.integration.SellIntegrationManager;
import gg.steve.mc.tp.modules.sell.SellModule;
import gg.steve.mc.tp.tool.LoadedTool;
import gg.steve.mc.tp.tool.ToolData;
import gg.steve.mc.tp.utils.CubeUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;

public class SellData implements ToolData {

    public SellData() {
    }

    @Override
    public void onBlockBreak(BlockBreakEvent blockBreakEvent, LoadedTool loadedTool) {
    }

    @Override
    public void onInteract(PlayerInteractEvent event, LoadedTool tool) {
        if (event.getClickedBlock() == null || event.getClickedBlock().getType().equals(Material.AIR)) return;
        event.setCancelled(true);
        List<Block> blocks = CubeUtil.getCube(event.getPlayer(), event.getClickedBlock(), tool.getRadius(), SellModule.moduleId, true);
        if (blocks.isEmpty()) return;
        List<Inventory> inventories = new ArrayList<>();
        for (Block block : blocks) {
            if (!(block.getState() instanceof InventoryHolder)) continue;
            Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
            if (!inventories.contains(inventory)) inventories.add(inventory);
        }
        if (inventories.isEmpty()) return;
        int amount = SellIntegrationManager.doInventorySale(event.getPlayer(), inventories, tool);
        if (amount == 0) return;
        if (!tool.decrementUses(event.getPlayer())) return;
        if (!tool.incrementBlocksMined(event.getPlayer(), amount)) return;
    }
}
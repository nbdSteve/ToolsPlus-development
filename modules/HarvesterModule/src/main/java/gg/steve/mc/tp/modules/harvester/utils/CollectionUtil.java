package gg.steve.mc.tp.modules.harvester.utils;

import gg.steve.mc.tp.integration.region.RegionProviderType;
import gg.steve.mc.tp.tool.PlayerTool;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {
    private Player player;
    private PlayerTool tool;
    private List<Block> primed;
    private int caneMined;
    private int blocksMined;

    public CollectionUtil(Player player, PlayerTool tool) {
        this.player = player;
        this.tool = tool;
        this.primed = new ArrayList<>();
        this.caneMined = 0;
    }

    public List<Block> getCropBlocks(Block start) {
        if (!this.primed.isEmpty()) this.primed.clear();
        this.caneMined = 0;
        this.blocksMined = 0;
        int radius = tool.getRadius();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = radius; y >= -radius; y--) {
                    Block block = start.getRelative(x, y, z);
                    HarvestableBlockType cropType = HarvestableBlockType.getTypeFromBlock(block);
                    if (cropType == null) continue;
                    boolean breakAllowed = true;
                    // check if the player can break this specific block
                    for (RegionProviderType regionProvider : RegionProviderType.values()) {
                        try {
                            if (!regionProvider.isBreakAllowed(player, block)) breakAllowed = false;
                        } catch (NoClassDefFoundError e) {
                            continue;
                        }
                    }
                    if (!breakAllowed) break;
                    switch (cropType) {
                        case SUGAR_CANE:
                        case CACTUS:
                            if (!isSameBlockBelow(block)) break;
                            isSameBlockAbove(block);
                            break;
                        default:
                            this.primed.add(block);
                            break;
                    }
                }
            }
        }
        this.blocksMined += primed.size();
        return this.primed;
    }

    public void isSameBlockAbove(Block start) {
        Block above = start.getRelative(BlockFace.UP);
        if (!this.primed.contains(start)) {
            this.primed.add(start);
            this.caneMined++;
        }
        if (start.getType() == above.getType()) {
            if (this.primed.contains(above)) return;
            this.primed.add(0, above);
            this.caneMined++;
            isSameBlockAbove(above);
        }
    }

    public boolean isSameBlockBelow(Block start) {
        Block below = start.getRelative(BlockFace.DOWN);
        return start.getType() == below.getType();
    }

    public int getCaneMined() {
        return caneMined;
    }

    public int getBlocksMined() {
        return blocksMined;
    }
}

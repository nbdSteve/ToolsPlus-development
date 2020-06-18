package gg.steve.mc.tp.libs.wgl;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import gg.steve.mc.tp.integration.libs.ToolsPlusLib;
import gg.steve.mc.tp.integration.libs.ToolsPlusLibType;
import org.bukkit.Location;

public class WorldGuardLegacyLib extends ToolsPlusLib {

    public WorldGuardLegacyLib() {
        super(ToolsPlusLibType.WORLDGUARD_LEGACY);
    }

    @Override
    public boolean isBreakAllowed(Location location) {
        ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(location.getWorld()).getApplicableRegions(location);
        return set.queryState(null, DefaultFlag.BLOCK_BREAK) != StateFlag.State.DENY;
    }
}

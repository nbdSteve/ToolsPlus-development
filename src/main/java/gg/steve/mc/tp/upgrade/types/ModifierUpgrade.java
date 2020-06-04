package gg.steve.mc.tp.upgrade.types;

import gg.steve.mc.tp.managers.PluginFile;
import gg.steve.mc.tp.tool.LoadedTool;
import gg.steve.mc.tp.upgrade.AbstractUpgrade;
import gg.steve.mc.tp.upgrade.UpgradeType;
import gg.steve.mc.tp.upgrade.utils.DowngradeHelper;
import gg.steve.mc.tp.upgrade.utils.UpgradeHelper;
import org.bukkit.entity.Player;

public class ModifierUpgrade extends AbstractUpgrade {

    public ModifierUpgrade(PluginFile file) {
        super(UpgradeType.MODIFIER, file);
    }

    @Override
    public boolean doUpgrade(Player player, LoadedTool tool) {
        UpgradeHelper helper = new UpgradeHelper(player, tool, this);
        if (!helper.isUpgradeable()) return false;
        if (!helper.hasAlreadyPayedForLevel()) {
            if (!getCurrency().isSufficientFunds(player, tool, helper.getCost())) {
                return false;
            }
        }
        return helper.isUpgradeSuccess();
    }

    @Override
    public boolean doDowngrade(Player player, LoadedTool tool) {
        DowngradeHelper helper = new DowngradeHelper(player, tool, this);
        if (!helper.isDowngradeable()) return false;
        if (!getCurrency().isSufficientFunds(player, tool, helper.getCost())) {
            return false;
        }
        return helper.isDowngradeSuccess();
    }
}
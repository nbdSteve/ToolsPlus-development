package gg.steve.mc.tp.currency.types;

import gg.steve.mc.tp.ToolsPlus;
import gg.steve.mc.tp.currency.AbstractCurrency;
import gg.steve.mc.tp.currency.CurrencyType;
import gg.steve.mc.tp.message.GeneralMessage;
import gg.steve.mc.tp.tool.LoadedTool;
import gg.steve.mc.tp.utils.XpUtil;
import org.bukkit.entity.Player;

public class XpRawCurrencyType extends AbstractCurrency {

    public XpRawCurrencyType() {
        super(CurrencyType.XP_RAW);
    }

    @Override
    public boolean isSufficientFunds(Player player, LoadedTool tool, double cost) {
        if (XpUtil.getTotalExperience(player) < cost) {
            GeneralMessage.INSUFFICIENT_FUNDS.message(player,
                    ToolsPlus.formatNumber(XpUtil.getTotalExperience(player)),
                    ToolsPlus.formatNumber(cost),
                    getCurrencyType().getPrefix(),
                    getCurrencyType().getSuffix());
            return false;
        }
        XpUtil.setTotalExperience(player, (int) (XpUtil.getTotalExperience(player) - cost));
        return true;
    }
}
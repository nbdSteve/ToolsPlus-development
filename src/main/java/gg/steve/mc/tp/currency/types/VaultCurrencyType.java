package gg.steve.mc.tp.currency.types;

import gg.steve.mc.tp.ToolsPlus;
import gg.steve.mc.tp.currency.AbstractCurrency;
import gg.steve.mc.tp.currency.CurrencyType;
import gg.steve.mc.tp.message.GeneralMessage;
import gg.steve.mc.tp.tool.LoadedTool;
import org.bukkit.entity.Player;

public class VaultCurrencyType extends AbstractCurrency {

    public VaultCurrencyType() {
        super(CurrencyType.VAULT);
    }

    @Override
    public boolean isSufficientFunds(Player player, LoadedTool tool, double cost) {
        if (ToolsPlus.eco().getBalance(player) < cost) {
            GeneralMessage.INSUFFICIENT_FUNDS.message(player,
                    ToolsPlus.formatNumber(ToolsPlus.eco().getBalance(player)),
                    ToolsPlus.formatNumber(cost),
                    getCurrencyType().getPrefix(),
                    getCurrencyType().getSuffix());
            return false;
        }
        ToolsPlus.eco().withdrawPlayer(player, cost);
        return true;
    }
}
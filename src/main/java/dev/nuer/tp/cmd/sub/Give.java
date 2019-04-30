package dev.nuer.tp.cmd.sub;

import dev.nuer.tp.ToolsPlus;
import dev.nuer.tp.initialize.MapInitializer;
import dev.nuer.tp.method.itemCreation.CraftItem;
import dev.nuer.tp.method.player.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that handles the give argument of the main command
 */
public class Give {

    /**
     * Gives the player the specified tool
     *
     * @param sender CommandSender, person sending the command
     * @param args   String[], list of command arguments
     */
    public static void onCmd(CommandSender sender, String[] args) {
        if (sender.hasPermission("toolsplus.admin")) {
            try {
                Player target = Bukkit.getPlayer(args[1]);
                if (!Bukkit.getOnlinePlayers().contains(target)) {
                    if (sender instanceof Player) {
                        new PlayerMessage("invalid-command", (Player) sender, "{reason}",
                                "The player you are trying to give that tool to is not online.");
                    } else {
                        ToolsPlus.LOGGER.info("[Tools+] Invalid command, check the GitHub wiki for command help.");
                    }
                    return;
                }
                //Create the starting modifier for the tool
                int toolStartingModifier = 1;
                if ((args.length == 6 || args.length == 7) &&
                        (args[2].equalsIgnoreCase("multi")
                                || args[2].equalsIgnoreCase("sell")
                                || args[2].equalsIgnoreCase("harvester")
                                || args[2].equalsIgnoreCase("tnt")
                                || args[2].equalsIgnoreCase("aqua"))) {
                    toolStartingModifier = verifyStartingModifier(sender, args[5], args[2], Integer.parseInt(args[4]));
                }
                //Get the starting uses from the configuration, or reset for command
                String startingUses = null;
                try {
                    startingUses = ToolsPlus.getFiles().get(args[2]).getString(args[2] + "-wands." + args[4] + ".uses.starting");
                    if (args.length == 6 && (args[2].equalsIgnoreCase("lightning")
                            || args[2].equalsIgnoreCase("sand"))) {
                        startingUses = args[5];
                    } else if (args.length == 7 && (args[2].equalsIgnoreCase("sell")
                            || args[2].equalsIgnoreCase("tnt")
                            || args[2].equalsIgnoreCase("aqua"))) {
                        startingUses = args[6];
                    }
                } catch (NullPointerException e) {
                    //Not a wand just disregard
                }
                if (args[2].equalsIgnoreCase("multi")) {
                    new CraftItem(args[3],
                            ToolsPlus.getFiles().get("multi").getString("multi-tools." + args[4] + ".name"),
                            ToolsPlus.getFiles().get("multi").getStringList("multi-tools." + args[4] + ".lore"),
                            ToolsPlus.getFiles().get("multi").getStringList("multi-tools." + args[4] + ".enchantments"),
                            "multi", Integer.parseInt(args[4]), target, "{mode}", MapInitializer.multiToolModeUnique.get(Integer.parseInt(args[4])).get(1),
                            "{radius}", MapInitializer.multiToolRadiusUnique.get(Integer.parseInt(args[4])).get(toolStartingModifier), "debug", "debug");
                }
                if (args[2].equalsIgnoreCase("trench")) {
                    new CraftItem(args[3],
                            ToolsPlus.getFiles().get("trench").getString("trench-tools." + args[4] + ".name"),
                            ToolsPlus.getFiles().get("trench").getStringList("trench-tools." + args[4] + ".lore"),
                            ToolsPlus.getFiles().get("trench").getStringList("trench-tools." + args[4] + ".enchantments"),
                            "trench", Integer.parseInt(args[4]), target);
                }
                if (args[2].equalsIgnoreCase("tray")) {
                    new CraftItem(args[3],
                            ToolsPlus.getFiles().get("tray").getString("tray-tools." + args[4] + ".name"),
                            ToolsPlus.getFiles().get("tray").getStringList("tray-tools." + args[4] + ".lore"),
                            ToolsPlus.getFiles().get("tray").getStringList("tray-tools." + args[4] + ".enchantments"),
                            "tray", Integer.parseInt(args[4]), target);
                }
                if (args[2].equalsIgnoreCase("sand")) {
                    new CraftItem(args[3],
                            ToolsPlus.getFiles().get("sand").getString("sand-wands." + args[4] + ".name"),
                            ToolsPlus.getFiles().get("sand").getStringList("sand-wands." + args[4] + ".lore"),
                            ToolsPlus.getFiles().get("sand").getStringList("sand-wands." + args[4] + ".enchantments"),
                            "sand", Integer.parseInt(args[4]), target, "debug", "debug",
                            "debug", "debug", "{uses}", startingUses);
                }
                if (args[2].equalsIgnoreCase("lightning")) {
                    new CraftItem(args[3], ToolsPlus.getFiles().get("lightning").getString("lightning-wands" + "." + args[4] + ".name"),
                            ToolsPlus.getFiles().get("lightning").getStringList("lightning-wands." + args[4] + ".lore"),
                            ToolsPlus.getFiles().get("lightning").getStringList("lightning-wands." + args[4] + ".enchantments"),
                            "lightning", Integer.parseInt(args[4]), target, "debug", "debug", "debug", "debug",
                            "{uses}", startingUses);
                }
                if (args[2].equalsIgnoreCase("harvester")) {
                    String[] modifierParts = MapInitializer.harvesterModifierUnique.get(Integer.parseInt(args[4])).get(toolStartingModifier).split("-");
                    new CraftItem(args[3], ToolsPlus.getFiles().get("harvester").getString("harvester-tools" + "." + args[4] + ".name"),
                            ToolsPlus.getFiles().get("harvester").getStringList("harvester-tools." + args[4] + ".lore"),
                            ToolsPlus.getFiles().get("harvester").getStringList("harvester-tools." + args[4] + ".enchantments"), "harvester",
                            Integer.parseInt(args[4]), target, "{mode}", MapInitializer.harvesterModeUnique.get(Integer.parseInt(args[4])).get(1), "{modifier}", modifierParts[0], "debug", "debug");
                }
                if (args[2].equalsIgnoreCase("sell")) {
                    String[] modifierParts = MapInitializer.sellWandModifierUnique.get(Integer.parseInt(args[4])).get(toolStartingModifier).split("-");
                    new CraftItem(args[3],
                            ToolsPlus.getFiles().get("sell").getString("sell-wands." + args[4] + ".name"),
                            ToolsPlus.getFiles().get("sell").getStringList("sell-wands." + args[4] + ".lore"),
                            ToolsPlus.getFiles().get("sell").getStringList("sell-wands." + args[4] + ".enchantments"),
                            "sell", Integer.parseInt(args[4]), target, "debug", "debug",
                            "{modifier}", modifierParts[0], "{uses}", startingUses);
                }
                if (args[2].equalsIgnoreCase("tnt")) {
                    String[] modifierParts =
                            MapInitializer.tntWandModifierUnique.get(Integer.parseInt(args[4])).get(toolStartingModifier).split("-");
                    new CraftItem(args[3],
                            ToolsPlus.getFiles().get("tnt").getString("tnt-wands." + args[4] + ".name"),
                            ToolsPlus.getFiles().get("tnt").getStringList("tnt-wands." + args[4] + ".lore"),
                            ToolsPlus.getFiles().get("tnt").getStringList("tnt-wands." + args[4] + ".enchantments"),
                            "tnt", Integer.parseInt(args[4]), target, "{mode}", MapInitializer.tntWandModeUnique.get(Integer.parseInt(args[4])).get(1),
                            "{modifier}", modifierParts[0], "{uses}", startingUses);
                }
                if (args[2].equalsIgnoreCase("aqua")) {
                    new CraftItem(args[3],
                            ToolsPlus.getFiles().get("aqua").getString("aqua-wands." + args[4] + ".name"),
                            ToolsPlus.getFiles().get("aqua").getStringList("aqua-wands." + args[4] + ".lore"),
                            ToolsPlus.getFiles().get("aqua").getStringList("aqua-wands." + args[4] + ".enchantments"),
                            "aqua", Integer.parseInt(args[4]), target, "{mode}", MapInitializer.aquaWandModeUnique.get(Integer.parseInt(args[4])).get(1),
                            "{radius}", MapInitializer.aquaWandRadiusUnique.get(Integer.parseInt(args[4])).get(toolStartingModifier), "{uses}", startingUses);
                }
            } catch (Exception invalidCommandParameters) {
                if (sender instanceof Player) {
                    invalidCommandParameters.printStackTrace();
                    new PlayerMessage("invalid-command", (Player) sender, "{reason}",
                            "An error occurred. Please check your command syntax, then your configuration (stack trace console)");
                } else {
                    ToolsPlus.LOGGER.info("[Tools+] Invalid command, check the GitHub wiki for command help.");
                }
            }
        } else {
            if (sender instanceof Player) {
                new PlayerMessage("no-permission", (Player) sender);
            }
        }
    }

    /**
     * Checks that the modifier the player is trying to apply is valid
     *
     * @param sender           CommandSender, person sending the command
     * @param startingModifier String, the modifier to set
     * @param typeOfTool       String, the type of tool; sell, multi etc.
     * @param toolID           Integer, the id from config
     * @return
     */
    public static int verifyStartingModifier(CommandSender sender, String startingModifier, String typeOfTool, int toolID) {
        int modifier = 1;
        try {
            modifier = Integer.parseInt(startingModifier);
        } catch (NumberFormatException e) {
            return modifier;
        }
        if (modifier < 1 || modifier > getMap(typeOfTool).get(toolID).size() - 3) {
            if (sender instanceof Player) {
                new PlayerMessage("invalid-command", (Player) sender, "{reason}", "The modifier: " + startingModifier + ", is not defined for that tool");
            } else {
                ToolsPlus.LOGGER.severe("[Tools+] The modifier you entered: " + startingModifier + ", is not defined for that tool.");
            }
            return 1;
        }
        return modifier;
    }

    /**
     * Returns the modifier map for that tool
     *
     * @param typeOfTool String, the type of tool; sell, multi etc.
     * @return HashMap<Integer, ArrayList <String>>
     */
    public static HashMap<Integer, ArrayList<String>> getMap(String typeOfTool) {
        if (typeOfTool.equalsIgnoreCase("multi")) return MapInitializer.multiToolRadiusUnique;
        if (typeOfTool.equalsIgnoreCase("harvester")) return MapInitializer.harvesterModifierUnique;
        if (typeOfTool.equalsIgnoreCase("sell")) return MapInitializer.sellWandModifierUnique;
        if (typeOfTool.equalsIgnoreCase("tnt")) return MapInitializer.tntWandModifierUnique;
        if (typeOfTool.equalsIgnoreCase("aqua")) return MapInitializer.aquaWandRadiusUnique;
        return null;
    }
}
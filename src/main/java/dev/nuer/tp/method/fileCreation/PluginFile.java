package dev.nuer.tp.method.fileCreation;

import dev.nuer.tp.ToolsPlus;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class that handles loading .yml files for the plugin
 */
public class PluginFile {
    //Register main instance
    private Plugin plugin = ToolsPlus.instance;
    //YAML configuration for the file
    private YamlConfiguration yamlFile;
    //Store the file name for later use
    private String fileName;
    //File to be created
    private File file;

    /**
     * Generates the provided yml file, the filename must be that of a file in the resources folder.
     *
     * @param fileName the name of the file being generated
     */
    public PluginFile(String fileName) {
        file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
            ToolsPlus.LOGGER.info("[Tools+] The plugin file " + fileName + " was not found, loading it now.");
        }
        yamlFile = new YamlConfiguration();
        try {
            yamlFile.load(file);
        } catch (InvalidConfigurationException e) {
            ToolsPlus.LOGGER.severe("[Tools+] The plugin file " + fileName +
                    " is not in the correct format, please contact the developer. Disabling the plugin");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        } catch (FileNotFoundException e) {
            ToolsPlus.LOGGER.severe("[Tools+] The plugin file " + fileName +
                    " was not found, please contact the developer. Disabling the plugin.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        } catch (IOException e) {
            ToolsPlus.LOGGER.severe("[Tools+] The plugin file " + fileName +
                    " could not be loaded, please contact the developer. Disabling the plugin.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
        //Instance variables
        this.fileName = fileName;
    }

    /**
     * Saves the file, this is used for setting variables in a method
     */
    public void save() {
        try {
            yamlFile.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * reload the file, after the yml has been edited
     */
    public void reload() {
        try {
            yamlFile.load(file);
        } catch (InvalidConfigurationException e) {
            ToolsPlus.LOGGER.severe("[Tools+] The plugin file " + fileName +
                    " is not in the correct format, plugin check your YAML syntax.");
        } catch (FileNotFoundException e) {
            ToolsPlus.LOGGER.severe("[Tools+] The plugin file " + fileName +
                    " was not found, plugin contact the developer. Disabling the plugin.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        } catch (IOException e) {
            ToolsPlus.LOGGER.severe("[Tools+] The plugin file " + fileName +
                    " could not be loaded, plugin contact the developer. Disabling the plugin.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    /**
     * get the yaml configuration for the file
     *
     * @return yaml configuration
     */
    public YamlConfiguration get() {
        return yamlFile;
    }
}
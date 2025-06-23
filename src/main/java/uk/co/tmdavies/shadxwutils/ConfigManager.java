package uk.co.tmdavies.shadxwutils;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class ConfigManager {

    private final JavaPlugin plugin;
    private final HashMap<String, ShadowConfig> configurationFiles;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        configurationFiles = new HashMap<>();
    }

    /**
     * Constructs and adds a new ShadowConfig.
     * @param name Name of Configuration file.
     * @param override Overrides directory.
     * @param preload Preloads the file for instant use.
     */
    public void addConfigFile(String name, boolean override, boolean preload) {
        this.configurationFiles.put(name, new ShadowConfig(plugin, name, override, preload));
    }

    /**
     * Constructs and adds new default config. This is used for configs that you have personally
     * made inside your resource folder. May contain comments you need in them.
     * @param defaultName Original name of file in Resource folder.
     * @param newName New name of Configuration file.
     * @param override Overrides directory.
     * @param preload Preloads the file for instant use.
     */
    public void addDefaultConfig(String defaultName, String newName, boolean override, boolean preload) {
        this.configurationFiles.put(newName, new ShadowConfig(plugin, newName, override, preload, true, defaultName));
    }

    /**
     * Adds new ShadowConfig.
     * @param config Already constructed ShadowConfig.
     */
    public void addConfigFile(ShadowConfig config) {
        this.configurationFiles.put(config.getFile().getName().replace(".yml", ""), config);
    }

    /**
     * Get a configuration file.
     * @param configFile Name of Config.
     * @return ShadowConfig.
     */
    public ShadowConfig getConfigurationFile(String configFile) {
        return configurationFiles.get(configFile);
    }

    /**
     * Reloads all registered ShadowConfig.
     */
    public void reloadConfigs() {
        configurationFiles.values().forEach(ShadowConfig::load);
    }

    /**
     * DEPRECATED
     * You should be doing this yourself via getConfigurationFile#saveConfig after using.
     */
    @Deprecated
    public void saveConfigs() {
        configurationFiles.values().forEach(ShadowConfig::save);
    }

}

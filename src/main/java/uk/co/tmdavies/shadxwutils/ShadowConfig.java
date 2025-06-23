package uk.co.tmdavies.shadxwutils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ShadowConfig {

    private final String path;
    private final JavaPlugin plugin;
    private File file;
    private YamlConfiguration ymlFile;

    /**
     * Creates new config.
     * @param plugin Plugin Instance.
     * @param path Path to config location.
     */
    public ShadowConfig(JavaPlugin plugin, String path) {
        this.plugin = plugin;
        this.path = this.plugin.getDataFolder() + "/" + path;
        this.file = new File(this.path);
    }

    /**
     * Creates new config with directory override option.
     * @param plugin Plugin Instance.
     * @param path Path to config location.
     * @param override Override directory.
     */
    public ShadowConfig(JavaPlugin plugin, String path, boolean override) {
        this.plugin = plugin;
        this.path = (!override ? plugin.getDataFolder() + "/" : "./plugins/") + path;
        this.file = new File(this.path);
    }

    /**
     * Creates and loads a new config.
     * @param plugin Plugin Instance.
     * @param path Path to config location.
     * @param override Override directory.
     * @param preload Preloads config for instance use.
     */
    public ShadowConfig(JavaPlugin plugin, String path, boolean override, boolean preload) {
        this.plugin = plugin;
        this.path = (!override ? plugin.getDataFolder() + "/" : "./plugins/") + path;
        this.file = new File(this.path);

        if (preload) {
            boolean fileExists = this.create();
            this.load();
        }
    }

    /**
     * Creates a config using default config inside of resource folder.
     * @param plugin Plugin Instance.
     * @param path Path to default config location.
     * @param override Override directory.
     * @param preload Preloads config for instant use.
     * @param defaultConfig Whether you're copying a config inside the resource folder.
     * @param fileNameOverride New name for config.
     */
    public ShadowConfig(JavaPlugin plugin, String path, boolean override, boolean preload, boolean defaultConfig, String fileNameOverride) {
        this.plugin = plugin;
        this.path = (!override ? this.plugin.getDataFolder() + "/" : "./plugins/") + path;
        this.file = new File(this.path);

        // Creates the new file destination (with name override).
        File newFile = new File((!override ? this.plugin.getDataFolder() + "/" : "./plugins/") + fileNameOverride);

        if (newFile.exists()) {
            this.file = newFile;

            if (preload) {
                this.load();
            }

            return;
        }

        // If the file doesn't exist and the default config is true, save the default config. (If there is a template in the resource folder)
        if (defaultConfig && !this.file.exists()) {
            this.plugin.saveResource(path, false);
        }

        // If the file doesn't exist, create a brand-new file.
        if (!this.file.exists()) {
            this.create();
        }

        // If the file name override is null or empty, don't rename the file.
        if (fileNameOverride == null || fileNameOverride.isEmpty()) {
            if (preload) {
                this.load();
            }

            return;
        }

        // If the file already exists, set the file to the new file.
        if (newFile.exists()) {
            this.file = newFile;

            if (preload) {
                this.load();
            }

            return;
        }

        // If the file name override doesn't end with .yml, add it.
        if (!fileNameOverride.endsWith(".yml")) {
            fileNameOverride += ".yml";
        }

        // Renames the file.
        boolean success = this.file.renameTo(newFile);

        // If the file failed to rename, log an error.
        if (!success) {
            plugin.getLogger().severe("[CONFIG] &cError renaming file: " + this.path + ", To: " + fileNameOverride);
            return;
        }

        // Sets the file to the new file.
        this.file = newFile;

        if (preload) {
            this.load();
        }
    }

    public boolean create() {
        if (!plugin.getDataFolder().exists()) {
            boolean generalDir = plugin.getDataFolder().mkdirs();
        }

        boolean fileDir = file.getParentFile().mkdirs();

        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch(IOException e) {
                plugin.getLogger().severe("[CONFIG] &cError creating file: " + this.path);
                e.printStackTrace();
            }
        }

        return true;
    }

    public boolean exists() {
        return this.file.exists();
    }

    public String getPath() {
        return this.path;
    }

    public void save() {
        try {
            this.ymlFile.save(this.file);
        } catch (IOException e) {
            plugin.getLogger().severe("[CONFIG] &cError saving file: " + this.path);
            e.printStackTrace();
        }
    }

    public void load() {
        this.ymlFile = YamlConfiguration.loadConfiguration(this.file);
    }

    public boolean delete() {
        return this.file.delete();
    }

    public void set(String path, Object value) {
        this.ymlFile.set(path, value);
        save();
    }

    public void add(String path, Object value) {
        if (!this.contains(path)) {
            this.ymlFile.set(path, value);
        }
    }

    public Object get(String path) {
        return this.ymlFile.get(path);
    }

    public File getFile() {
        return this.file;
    }

    public Object getByDefault(String path, Object value) {
        this.add(path, value);
        return this.get(path);
    }

    public boolean contains(String path) {
        return this.ymlFile.contains(path);
    }

    public Set<String> getKeys(String path) {
        return this.getKeys(path, false);
    }

    public Set<String> getKeys(String path, boolean deep) {
        try {
            return Objects.requireNonNull(this.ymlFile.getConfigurationSection(path)).getKeys(deep);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean getBoolean(String path) {
        return this.ymlFile.getBoolean(path);
    }

    public boolean getBooleanByDefault(String path, boolean value) {
        this.add(path, value);
        return this.getBoolean(path);
    }

    public double getDouble(String path) {
        return this.ymlFile.getDouble(path);
    }

    public Double getDoubleByDefault(String path, Double value) {
        this.add(path, value);
        return this.getDouble(path);
    }

    public float getFloat(String path) {
        return Float.parseFloat(Objects.requireNonNull(this.ymlFile.getString(path)));
    }

    public Float getFloatByDefault(String path, Float value) {
        this.add(path, value);
        return this.getFloat(path);
    }

    public int getInt(String path) {
        return this.ymlFile.getInt(path);
    }

    public int getIntByDefault(String path, Integer value) {
        this.add(path, value);
        return this.getInt(path);
    }

    public String getString(String path) {
        return this.ymlFile.getString(path);
    }

    public String getStringByDefault(String path, String value) {
        this.add(path, value);
        return this.getString(path);
    }

    public List<String> getStringList(String path) {
        return getYML().getStringList(path);
    }

    public List<String> getColouredStringList(String path) {
        List<String> toReturn = new ArrayList<>();

        getStringList(path).forEach(string -> toReturn.add(ChatColor.translateAlternateColorCodes('&', string)));

        return toReturn;
    }

    public String getColouredString(String path) {
        return ChatColor.translateAlternateColorCodes('&', this.getString(path));
    }

    public ItemStack getItemStack(String path) {
        return this.ymlFile.getItemStack(path);
    }

    public ItemStack getItemStackByDefault(String path, ItemStack value) {
        this.add(path, value);
        return this.getItemStack(path);
    }

    public YamlConfiguration getYML() {
        return this.ymlFile;
    }

    public void setYML(YamlConfiguration ymlFile) {
        this.ymlFile = ymlFile;
    }

}

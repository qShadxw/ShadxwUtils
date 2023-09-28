package uk.co.tmdavies.shadxwutils;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import uk.co.tmdavies.shadxwutils.utils.ShadowConfig;
import uk.co.tmdavies.shadxwutils.utils.ShadowLogger;
import uk.co.tmdavies.shadxwutils.utils.ShadowUtils;

import java.util.HashMap;

public class ShadxwUtils extends JavaPlugin {

    public static HashMap<Entity, BukkitRunnable> entityTasks;
    public static ShadowLogger logger;
    public static ShadowConfig config;
    public static boolean resourcePackEnabled;
    public static boolean kickOnDecline;

    @Override
    public void onLoad() {
        logger = new ShadowLogger();
        config = new ShadowConfig(this.getClass(), "config.yml", false, true);

        if (config.getString("ResourcePack.URL") == null) {
            config.add("Prefix", "&6ShadxwUtils &8»&7");
            config.add("ResourcePack.Enabled", true);
            config.add("ResourcePack.URL", "https://url.download.link/here");
            config.add("ResourcePack.KickOnFailedDownload", true);
            config.add("ResourcePack.KickOnResourcePackDeclined", true);
            config.add("ResourcePack.KickMessage", "%prefix% &cYou must accept the resource pack to play on this server.");
            config.add("VPN.KickMessage", "%prefix% &cYou must disable your VPN to play on this server.");
            config.save();
        }

        logger.log(ShadowLogger.Reason.GENERIC, "&aPlugin has successfully loaded.");
    }

    @Override
    public void onEnable() {
        ShadowUtils.loadFiles(this);

        resourcePackEnabled = config.getBoolean("ResourcePack.Enabled");
        kickOnDecline = config.getBoolean("ResourcePack.KickOnResourcePackDeclined");
        entityTasks = new HashMap<>();

        logger.log(ShadowLogger.Reason.GENERIC, "&aPlugin has successfully enabled.");
    }

}

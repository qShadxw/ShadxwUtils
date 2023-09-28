package uk.co.tmdavies.shadxwutils.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.shadxwutils.ShadxwUtils;
import uk.co.tmdavies.shadxwutils.utils.ShadowUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

public class PlayerListener implements Listener {

    public PlayerListener(ShadxwUtils plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (ShadxwUtils.resourcePackEnabled) {
            event.getPlayer().setResourcePack(ShadxwUtils.config.getString("ResourcePack.URL"));
        }

        // Anti VPN - https://github.com/ImBubbles/AntiVPN/blob/master/src/main/java/me/bubbles/antivpn/events/Login.java
        Player player = event.getPlayer();

        if (player.hasPermission("shadxwutils.bypassvpn")) return;

        try {
            if (Objects.requireNonNull(player.getAddress()).getAddress().equals(InetAddress.getLocalHost())) return;
        } catch (UnknownHostException ignored) {
            return;
        }

        new Thread(() -> {
            try (Socket socket = new Socket(player.getAddress().getAddress(), 80)) {
                Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(ShadxwUtils.class), () -> {
                    player.kickPlayer(ShadowUtils.Colour(ShadxwUtils.config.getString("VPN.KickMessage")
                            .replace("%prefix%", ShadxwUtils.config.getString("Prefix"))));
                });
            } catch (IOException ignored) {
                // Not a VPN
                return;
            }
        }).start();
    }

    @EventHandler
    public void onResourcePackDeclined(PlayerResourcePackStatusEvent event) {
        if (!ShadxwUtils.resourcePackEnabled) return;
        if (!ShadxwUtils.kickOnDecline) return;
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.ACCEPTED) return;
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) return;
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD
                && !ShadxwUtils.config.getBoolean("ResourcePack.KickOnFailedDownload")) return;

        event.getPlayer().kickPlayer(ShadowUtils.Colour(ShadxwUtils.config.getString("ResourcePack.KickMessage")
                .replace("%prefix%", ShadxwUtils.config.getString("Prefix"))));
    }

}

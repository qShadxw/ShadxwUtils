package uk.co.tmdavies.shadxwutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import uk.co.tmdavies.shadxwutils.ShadxwUtils;
import uk.co.tmdavies.shadxwutils.utils.ShadowUtils;

public class BaseCommand implements CommandExecutor {

    public BaseCommand(ShadxwUtils plugin) {
        plugin.getCommand("shadxwutils").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        if (!sender.hasPermission("shadxwutils.admin")) {
            sender.sendMessage("§6ShadxwUtils §8»§7 You do not have permission to use this command.");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§6ShadxwUtils §8»§7 Version: §60.1.0");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage("§6ShadxwUtils §8»§7 Reloading config...");
            ShadxwUtils.config.load();
            ShadxwUtils.resourcePackEnabled = ShadxwUtils.config.getBoolean("ResourcePack.Enabled");
            ShadxwUtils.kickOnDecline = ShadxwUtils.config.getBoolean("ResourcePack.KickOnResourcePackDeclined");

            Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(ShadxwUtils.class), () -> {
                sender.sendMessage("§6ShadxwUtils §8»§7 Reloaded Config.");
            }, 20L, 1L);

            return true;
        }

        ShadowUtils.sendCenteredMessage(player, "&6ShadxwUtils");
        ShadowUtils.sendCenteredMessage(player, "&8&oby Carbonate | Version: 0.1.0");
        ShadowUtils.sendCenteredMessage(player, "&4");
        ShadowUtils.sendCenteredMessage(player, "&7/shadxwutils <reload> &8- &7Displays this message.");
        ShadowUtils.sendCenteredMessage(player, "&7/rusticresourcepack <toggle|reload> &8- &7Reloads the config.");
        ShadowUtils.sendCenteredMessage(player, "&7/spawndisplay <displaytype> <item> <entitytag> &8- &7Spawns a display.");
        ShadowUtils.sendCenteredMessage(player, "&7/deletedisplay <entitytag> &8- &7Deletes a display.");
        ShadowUtils.sendCenteredMessage(player, "&7/cancelmodify <entitytag> &8- &7Cancels a modify task.");
        ShadowUtils.sendCenteredMessage(player, "&7/modifyentity <entitytag> <mod> <value> &8- &7Modifies an entity.");

        return true;
    }
}

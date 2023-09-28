package uk.co.tmdavies.shadxwutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.tmdavies.shadxwutils.ShadxwUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResourcePackCommand implements CommandExecutor, TabCompleter {

    public ResourcePackCommand(ShadxwUtils plugin) {
        Objects.requireNonNull(plugin.getCommand("rusticresourcepack")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("rusticresourcepack")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, String[] args) {
        if (!sender.hasPermission("shadxwutils.resourcepack")) {
            sender.sendMessage("§6ShadxwUtils §8»§7 You do not have permission to use this command.");
            return true;
        }

        switch (args.length) {
            case 1 -> {
                switch (args[0].toLowerCase()) {
                    case "reload" -> {
                        sender.sendMessage("§6ShadxwUtils §8»§7 Reloading config...");
                        ShadxwUtils.config.load();
                        ShadxwUtils.resourcePackEnabled = ShadxwUtils.config.getBoolean("ResourcePack.Enabled");
                        ShadxwUtils.kickOnDecline = ShadxwUtils.config.getBoolean("ResourcePack.KickOnResourcePackDeclined");

                        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(ShadxwUtils.class), () -> {
                            sender.sendMessage("§6ShadxwUtils §8»§7 Reloaded Config.");
                        }, 20L, 1L);

                        return true;
                    }
                    case "toggle" -> {
                        ShadxwUtils.resourcePackEnabled = !ShadxwUtils.resourcePackEnabled;
                        ShadxwUtils.config.set("ResourcePack.Enabled", ShadxwUtils.resourcePackEnabled);
                        ShadxwUtils.config.reload();
                        sender.sendMessage("§6ShadxwUtils §8»§7 Resource pack " + (ShadxwUtils.resourcePackEnabled ? "enabled" : "disabled") + ".");

                        return true;
                    }
                    default -> sender.sendMessage("§6ShadxwUtils §8»§7 /rusticresourcepack <reload|toggle>");
                }
            }
            default -> sender.sendMessage("§6ShadxwUtils §8»§7 /rusticresourcepack <reload|toggle>");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String string, @NotNull String[] args) {
        if (!sender.hasPermission("shadxwutils.resourcepack")) return null;

        ArrayList<String> arguments = new ArrayList<>();

        switch (args.length) {
            case 1 -> {
                arguments.add("reload");
                arguments.add("toggle");
            }
        }

        return arguments;
    }
}

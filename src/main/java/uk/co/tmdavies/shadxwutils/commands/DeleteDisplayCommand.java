package uk.co.tmdavies.shadxwutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.tmdavies.shadxwutils.ShadxwUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteDisplayCommand implements CommandExecutor, TabCompleter {

    // /deletedisplay entitytag

    public DeleteDisplayCommand(ShadxwUtils plugin) {
        Objects.requireNonNull(plugin.getCommand("deletedisplay")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("deletedisplay")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        if (!sender.hasPermission("shadxwutils.deletedisplay")) {
            sender.sendMessage("§6ShadxwUtils §8»§7 You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§6ShadxwUtils §8»§7 /deletedisplay <entitytag>");
            return true;
        }

        World world;

        if (sender instanceof Player player) {
            world = player.getWorld();
        } else {
            world = Bukkit.getWorlds().get(0);
        }

        world.getEntities().forEach(entity -> {
            if (entity.getScoreboardTags().contains(args[0].toLowerCase())) {
                entity.remove();
            }
        });

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        ArrayList<String> arguments = new ArrayList<>();

        if (args.length == 1) {
            for (Entity entity : Bukkit.getWorlds().get(0).getEntities()) {
                entity.getScoreboardTags().forEach((tag) -> {
                    if (!arguments.contains(tag)) {
                        arguments.add(tag);
                    }
                });
            }
        }

        return arguments;
    }
}

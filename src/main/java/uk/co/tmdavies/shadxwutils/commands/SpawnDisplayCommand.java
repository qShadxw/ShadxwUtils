package uk.co.tmdavies.shadxwutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.tmdavies.shadxwutils.ShadxwUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpawnDisplayCommand implements CommandExecutor, TabCompleter {

    // /spawndisplay type item entitytag

    public SpawnDisplayCommand(ShadxwUtils plugin) {
        Objects.requireNonNull(plugin.getCommand("spawndisplay")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("spawndisplay")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        if (!sender.hasPermission("shadxwutils.spawndisplay")) {
            sender.sendMessage("§6ShadxwUtils §8»§7 You do not have permission to use this command.");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§6ShadxwUtils §8»§7 You must be a player to use this command.");
            return true;
        }

        if (args.length != 3) {
            sender.sendMessage("§6ShadxwUtils §8»§7 /spawndisplay <type> <item> <entitytag>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "item" -> {
                try {
                    Material.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("§6ShadxwUtils §8»§7 Invalid item.");
                    return true;
                }

                ItemDisplay entity = (ItemDisplay) player.getWorld().spawnEntity(player.getLocation(), EntityType.ITEM_DISPLAY);
                entity.setItemStack(new ItemStack(Material.valueOf(args[1].toUpperCase())));
                entity.addScoreboardTag(args[2].toLowerCase());
            }
            case "text" -> {
                sender.sendMessage("§6ShadxwUtils §8»§7 Not implemented yet.");
            }
            case "block" -> {
                try {
                    Material.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage("§6ShadxwUtils §8»§7 Invalid item.");
                    return true;
                }

                BlockDisplay entity = (BlockDisplay) player.getWorld().spawnEntity(player.getLocation(), EntityType.BLOCK_DISPLAY);
                entity.setBlock(Material.valueOf(args[1].toUpperCase()).createBlockData());
                entity.addScoreboardTag(args[2].toLowerCase());
            }
            default -> {
                sender.sendMessage("§6ShadxwUtils §8»§7 Invalid display type.");
            }
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        ArrayList<String> arguments = new ArrayList<>();

        switch (args.length) {
            case 1 -> {
                arguments.add("item");
                arguments.add("text");
                arguments.add("block");
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("item") || args[0].equalsIgnoreCase("block")) {
                    for (Material material : Material.values()) {
                        arguments.add(material.name().toLowerCase());
                    }
                }
                if (args[0].equalsIgnoreCase("text")) {
                    arguments.add("<text>");
                }
            }
            case 3 -> {
                arguments.add("<entitytag>");
            }
        }

        return arguments;
    }


}

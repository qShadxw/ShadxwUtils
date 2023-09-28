package uk.co.tmdavies.shadxwutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.tmdavies.shadxwutils.ShadxwUtils;
import uk.co.tmdavies.shadxwutils.utils.ShadowUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CancelModifyCommand implements CommandExecutor, TabCompleter {

    public CancelModifyCommand(ShadxwUtils plugin) {
        Objects.requireNonNull(plugin.getCommand("cancelmodify")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("cancelmodify")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        if (!sender.hasPermission("shadxwutils.cancelmodify")) {
            sender.sendMessage("§6ShadxwUtils §8»§7 You do not have permission to use this command.");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§6ShadxwUtils §8»§7 /cancelmodify <entitytag>");
            return true;
        }

        if (args[0].toLowerCase().equals("%all")) {
            ShadxwUtils.entityTasks.forEach((ent, task) -> {
                task.cancel();
                ShadxwUtils.entityTasks.remove(ent);
            });

            sender.sendMessage("§6ShadxwUtils §8»§7 Successfully cancelled modify tasks for §6" + ShadxwUtils.entityTasks.size() + "§7 entities.");

            return true;
        }

        ArrayList<Entity> entities = ShadowUtils.getEntitiesByTag(Bukkit.getWorlds().get(0), args[0]);

        entities.forEach((ent) -> {
            if (ShadxwUtils.entityTasks.containsKey(ent)) {
                ShadxwUtils.entityTasks.get(ent).cancel();
                ShadxwUtils.entityTasks.remove(ent);
                sender.sendMessage("§6ShadxwUtils §8»§7 Successfully cancelled modify task for entity §6" + ent.getUniqueId() + "§7.");
            }
        });

        sender.sendMessage("§6ShadxwUtils §8»§7 Successfully cancelled modify tasks for §6" + entities.size() + "§7 entities.");

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

package uk.co.tmdavies.shadxwutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import uk.co.tmdavies.shadxwutils.ShadxwUtils;
import uk.co.tmdavies.shadxwutils.utils.ShadowUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModifyEntityCommand implements CommandExecutor, TabCompleter {

    // /modifyentity entitytag action speed

    public ModifyEntityCommand(ShadxwUtils plugin) {
        Objects.requireNonNull(plugin.getCommand("modifyentity")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("modifyentity")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String string, String[] args) {
        if (!sender.hasPermission("shadxwutils.modifyentity")) {
            sender.sendMessage("§6ShadxwUtils §8»§7 You do not have permission to use this command.");
            return true;
        }

        if (args.length != 3 && args.length != 7) {
            sender.sendMessage("§6ShadxwUtils §8»§7 /modifyentity <entitytag> <action> <speed>");
            sender.sendMessage("§6ShadxwUtils §8»§7 /modifyentity <entitytag> setrotation <left|right> <x> <y> <z> <w>");
            return true;
        }

        List<Entity> entities = ShadowUtils.getEntitiesByTag(Bukkit.getWorlds().get(0), args[0]);

        if (entities.isEmpty()) {
            sender.sendMessage("§6ShadxwUtils §8»§7 No entities found with that tag.");
            return true;
        }

        try {
            Double.parseDouble(args[1].equalsIgnoreCase("settransformation") ? args[3] : args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§6ShadxwUtils §8»§7 Invalid number.");
            return true;
        }

        switch(args[1].toLowerCase()) {
            case "rotate" -> {
                entities.forEach((ent) -> {
                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    ent.setRotation(ent.getLocation().getYaw() + Float.parseFloat(args[2]), ent.getLocation().getPitch());
                });
            }
            case "constantrotate" -> {
                entities.forEach((ent) -> {
                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    BukkitRunnable entityTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            ent.setRotation(ent.getLocation().getYaw() + Float.parseFloat(args[2]), ent.getLocation().getPitch());
                        }
                    };

                    handleTasks(ent, entityTask);
                });
            }
            case "bounce" -> {
                entities.forEach((ent) -> {
                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    BukkitRunnable entityTask = new BukkitRunnable() {
                        int currentTick = 0;
                        final double speed = Double.parseDouble(args[2]);
                        double lastYOffset = 0;

                        @Override
                        public void run() {
                            double yOffset = Math.sin(Math.toRadians(currentTick * speed));
                            ent.teleport(ent.getLocation().add(0, (yOffset - lastYOffset), 0));

                            lastYOffset = yOffset;
                            currentTick++;
                        }
                    };

                    handleTasks(ent, entityTask);
                });
            }
            case "rotatebounce" -> {
                entities.forEach((ent) -> {
                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    BukkitRunnable entityTask = new BukkitRunnable() {
                        int currentTick = 0;
                        final double speed = Double.parseDouble(args[2]);
                        double lastYOffset = 0;

                        @Override
                        public void run() {
                            double yOffset = Math.sin(Math.toRadians(currentTick * speed));
                            ent.teleport(ent.getLocation().add(0, (yOffset - lastYOffset), 0));
                            ent.setRotation(ent.getLocation().getYaw() + Float.parseFloat(args[2]), ent.getLocation().getPitch());

                            lastYOffset = yOffset;
                            currentTick++;
                        }
                    };

                    handleTasks(ent, entityTask);
                });
            }
            case "ascend" -> {
                entities.forEach((ent) -> {
                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    BukkitRunnable entityTask = new BukkitRunnable() {
                        final double speed = Double.parseDouble(args[2]);

                        @Override
                        public void run() {
                            ent.teleport(ent.getLocation().add(0, speed, 0));
                        }
                    };

                    handleTasks(ent, entityTask);
                });
            }
            case "descend" -> {
                entities.forEach((ent) -> {
                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    BukkitRunnable entityTask = new BukkitRunnable() {
                        final double speed = Double.parseDouble(args[2]);

                        @Override
                        public void run() {
                            ent.teleport(ent.getLocation().subtract(0, speed, 0));
                        }
                    };

                    handleTasks(ent, entityTask);
                });
            }
            case "size" -> {
                entities.forEach((ent) -> {
                    if (!(ent instanceof Display displayEntity)) {
                        return;
                    }

                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    double size = Double.parseDouble(args[2]);
                    Transformation transformation = displayEntity.getTransformation();

                    transformation.getScale().set(size, size, size);
                    displayEntity.setTransformation(transformation);
                });
            }
            case "spin" -> {
                entities.forEach((ent) -> {
                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    BukkitRunnable entityTask = new BukkitRunnable() {

                        final Location loc = ent.getLocation();
                        double time = 0;
                        final double radius = Double.parseDouble(args[2]);

                        @Override
                        public void run() {

                            time = time + Math.PI / 16;
                            double x = radius * Math.cos(time);
                            double z = radius * Math.sin(time);
                            Vector velocity = new Vector(x, 0, z);
                            velocity = rotateAroundAxisY(velocity, 10);

                            loc.add(velocity.getX(), velocity.getY(), velocity.getZ());

                            ent.teleport(loc);
                            //loc.getWorld().spawnParticle(particleEffect, loc, particleAmount);
                            loc.subtract(velocity.getX(), velocity.getY(), velocity.getZ());

                        }
                    };

                    handleTasks(ent, entityTask);
                });
            }
            case "spinrotate" -> {
                entities.forEach((ent) -> {
                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    BukkitRunnable entityTask = new BukkitRunnable() {

                        final Location loc = ent.getLocation();
                        double time = 0;
                        final double radius = Double.parseDouble(args[2]);

                        @Override
                        public void run() {

                            time = time + Math.PI / 16;
                            double x = radius * Math.cos(time);
                            double z = radius * Math.sin(time);
                            Vector velocity = new Vector(x, 0, z);
                            velocity = rotateAroundAxisY(velocity, 10);

                            loc.add(velocity.getX(), velocity.getY(), velocity.getZ());

                            ent.teleport(loc);
                            ent.setRotation(loc.getYaw() + Float.parseFloat(args[2]), loc.getPitch());
                            loc.subtract(velocity.getX(), velocity.getY(), velocity.getZ());

                        }
                    };

                    handleTasks(ent, entityTask);
                });
            }
            case "setpitch" -> {
                entities.forEach((ent) -> {
                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    ent.setRotation(ent.getLocation().getYaw(), Float.parseFloat(args[2]));
                });
            }
            case "setyaw" -> {
                entities.forEach((ent) -> {
                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    ent.setRotation(Float.parseFloat(args[2]), ent.getLocation().getPitch());
                });
            }
            case "settransformation" -> {
                entities.forEach((ent) -> {
                    if (!(ent instanceof Display displayEntity)) {
                        return;
                    }
                    if (ShadxwUtils.entityTasks.containsKey(ent)) {
                        ShadxwUtils.entityTasks.get(ent).cancel();
                        ShadxwUtils.entityTasks.remove(ent);
                    }

                    Transformation transformation = displayEntity.getTransformation();
                    Quaternionf rotation = null;

                    if (args[2].equalsIgnoreCase("left")) {
                        rotation = transformation.getLeftRotation();
                    }
                    if (args[2].equalsIgnoreCase("right")) {
                        rotation = transformation.getRightRotation();
                    }
                    if (rotation == null) {
                        sender.sendMessage("§6ShadxwUtils §8»§7 Invalid rotation.");
                        return;
                    }

                    rotation.set(Float.parseFloat(args[3]), Float.parseFloat(args[4]), Float.parseFloat(args[5]), Float.parseFloat(args[6]));

                    displayEntity.setTransformation(transformation);
                });
            }
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String string, @NotNull String[] args) {
        ArrayList<String> arguments = new ArrayList<>();

        switch(args.length) {
            case 1 -> {
                Bukkit.getWorlds().get(0).getEntities().forEach((ent) -> {
                    ent.getScoreboardTags().forEach((tag) -> {
                        if (!arguments.contains(tag)) {
                            arguments.add(tag);
                        }
                    });
                });
            }
            case 2 -> {
                arguments.add("rotate");
                arguments.add("constantrotate");
                arguments.add("bounce");
                arguments.add("rotatebounce");
                arguments.add("ascend");
                arguments.add("descend");
                arguments.add("size");
                arguments.add("spin");
                arguments.add("spinrotate");
                arguments.add("settransformation");
            }
            case 3 -> {
                if (args[1].equalsIgnoreCase("settransformation")) {
                    arguments.add("left");
                    arguments.add("right");
                    return arguments;
                }
                arguments.add("<number>");
            }
            case 4, 5, 6, 7 -> {
                if (args[1].equalsIgnoreCase("settransformation")) {
                    arguments.add("<number>");
                }
            }
        }

        return arguments;
    }

    public void handleTasks(Entity entity, BukkitRunnable entityTask) {
        if (ShadxwUtils.entityTasks.containsKey(entity)) {
            ShadxwUtils.entityTasks.get(entity).cancel();
            ShadxwUtils.entityTasks.remove(entity);
        }

        ShadxwUtils.entityTasks.put(entity, entityTask);
        entityTask.runTaskTimer(JavaPlugin.getPlugin(ShadxwUtils.class), 0, 1);
    }

    private Vector rotateAroundAxisY(Vector v, double angle) {

        angle = -angle;
        angle = Math.toRadians(angle);

        double x, z, cos, sin;

        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;

        return v.setX(x).setZ(z);

    }

}

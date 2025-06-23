package uk.co.tmdavies.shadxwutils;

import com.google.common.reflect.ClassPath;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.tmdavies.shadxwutils.commands.ShadowCommand;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class PluginManager {

    private final JavaPlugin plugin;
    private String commandPackagePath;
    private String listenerPackagePath;

    public PluginManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.commandPackagePath = null;
        this.listenerPackagePath = null;
    }

    public PluginManager(JavaPlugin plugin, String commandPackagePath, String listenerPackagePath) {
        this.plugin = plugin;
        this.commandPackagePath = commandPackagePath;
        this.listenerPackagePath = listenerPackagePath;
    }

    public JavaPlugin getPluginInstance() {
        return plugin;
    }

    /**
     * Registers all commands extending {@link uk.co.tmdavies.shadxwutils.commands.ShadowCommand} in supplied command path.
     */
    public void registerCommands() {
        if (this.commandPackagePath == null) {
            this.plugin.getLogger().warning("You need to specify the command package to use this feature.");

            return;
        }

        // Gets main class loader.
        ClassLoader classLoader = plugin.getClass().getClassLoader();
        try {
            // Gets the class path to the class loader.
            ClassPath path = ClassPath.from(classLoader);

            // Iterates through all the classes in the commands package.
            for (ClassPath.ClassInfo info : path.getTopLevelClassesRecursive(commandPackagePath)) {
                // Checking
                this.plugin.getLogger().warning(String.format("Checking Command: %s", info.getName()));

                // Checks if the class is the ShadowCommand class, if so then continue because it's not an actual command.
                if (info.getSimpleName().equals("ShadowCommand")) {
                    continue;
                }

                // Gets the class from the class path.
                Class<?> clazz = Class.forName(info.getName(), true, classLoader);

                // Checks if the class extends ShadowCommand.
                if (ShadowCommand.class.isAssignableFrom(clazz)) {
                    // Creates a new instance of the class.
                    ShadowCommand shadowCommand = (ShadowCommand) clazz.getConstructors()[0].newInstance();

                    // Registers the command and tab completer.
                    registerCommandAndTab(plugin, shadowCommand.getName(), shadowCommand);
                }
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException |
                 InvocationTargetException exception) {
            this.plugin.getLogger().severe("Failed to register commands.");
            this.plugin.getLogger().severe(exception.getMessage());
        }
    }

    /**
     * Registers all listeners inside supplied listener path.
     */
    public void registerListeners() {
        if (this.listenerPackagePath == null) {
            this.plugin.getLogger().warning("You need to specify the listener package to use this feature.");

            return;
        }

        // Gets main class loader.
        ClassLoader classLoader = plugin.getClass().getClassLoader();
        try {
            // Gets the class path to the class loader.
            ClassPath path = ClassPath.from(classLoader);

            // Iterates through all the classes in the listeners.game package.
            for (ClassPath.ClassInfo info : path.getTopLevelClassesRecursive(listenerPackagePath)) {

                // Checking
                this.plugin.getLogger().warning(String.format("Checking Listener: %s", info.getName()));

                // Gets the class from the class path.
                Class<?> clazz = Class.forName(info.getName(), true, classLoader);

                // Checks if the class extends Listener.
                if (Listener.class.isAssignableFrom(clazz)) {
                    // Creates a new instance of the class.
                    Listener listener = (Listener) clazz.getConstructors()[0].newInstance();

                    // Registers the listener.
                    Bukkit.getPluginManager().registerEvents(listener, plugin);

                    this.plugin.getLogger().info(String.format("Registering listener: %s", info.getName()));
                }
            }
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            this.plugin.getLogger().severe("Failed to register listeners.");
            this.plugin.getLogger().severe(exception.getMessage());
        }
    }

    private void registerCommandAndTab(JavaPlugin plugin, String command, ShadowCommand shadowCommand) {
        this.plugin.getLogger().info(String.format("Registering Command: %s", command));

        Objects.requireNonNull(plugin.getCommand(command)).setExecutor(shadowCommand);
        Objects.requireNonNull(plugin.getCommand(command)).setTabCompleter(shadowCommand);
    }

    public void setCommandPackagePath(String commandPackagePath) {
        this.commandPackagePath = commandPackagePath;
    }

    public void setListenerPackagePath(String listenerPackagePath) {
        this.listenerPackagePath = listenerPackagePath;
    }

}

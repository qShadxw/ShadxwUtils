package uk.co.tmdavies.shadxwutils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import java.util.List;

public class ShadowLogger {

    private ConsoleCommandSender logger;

    public ShadowLogger() {
        init();
    }

    private void init() {
        if (this.logger == null) {
            this.logger = Bukkit.getConsoleSender();
        }
    }

    public void log(String message, Object... args) {
        this.logger.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(message, args)));
    }

    public void log(Reason reason, String message, Object... args) {
        this.logger.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(reason.getColour() + reason.getPrefix() + message, args)));
    }

    public void error(String message, Object... args) {
        this.logger.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(Reason.ERROR.getColour() + Reason.ERROR.getPrefix() + message, args)));
    }

    public void error(Reason reason, String message, Object... args) {
        this.logger.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(Reason.ERROR.getColour() + Reason.ERROR.getPrefix() + reason.getColour() + reason.getPrefix() + message, args)));
    }

    public void warning(String message, Object... args) {
        this.logger.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(Reason.WARNING.getColour() + Reason.WARNING.getPrefix() + message, args)));
    }

    public void warning(Reason reason, String message, Object... args) {
        this.logger.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(Reason.WARNING.getColour() + Reason.WARNING.getPrefix() + reason.getColour() + reason.getPrefix() + message, args)));
    }

    public void startUp(String pluginName, String pluginVersion, List<String> authors) {
        log(Reason.GENERIC, "");
        log(Reason.GENERIC, "&a   ============  &7%s V%s  &a============", pluginName, pluginVersion);
        log(Reason.GENERIC, "                &aCreated by: %s", authors.toString().replace("[", "").replace("]", ""));
        log(Reason.GENERIC, "");
    }

    public enum Reason {

        GENERIC("", ChatColor.WHITE),
        ERROR("[Error] ", ChatColor.RED),
        WARNING("[Warning] ", ChatColor.GOLD),
        CONFIG("[Config] ", ChatColor.AQUA),
        SQL("[SQL] ", ChatColor.DARK_AQUA),
        KEY("[Key] ", ChatColor.GREEN),
        API("[API] ", ChatColor.YELLOW),
        ECONOMY("[Economy] ", ChatColor.BLUE);

        private final String prefix;
        private final ChatColor colour;

        Reason(String prefix, ChatColor colour) {
            this.prefix = prefix;
            this.colour = colour;
        }

        public String getPrefix() {
            return this.prefix;
        }

        public ChatColor getColour() {
            return this.colour;
        }

    }
}

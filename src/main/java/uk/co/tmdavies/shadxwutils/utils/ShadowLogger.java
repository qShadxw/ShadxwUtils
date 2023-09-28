package uk.co.tmdavies.shadxwutils.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class ShadowLogger {

    private java.util.logging.Logger logger;

    public ShadowLogger() {
        init();
    }

    private void init() {
        if (this.logger == null) {
            this.logger = new java.util.logging.Logger("Whitelister", null) {
            };

            this.logger.setParent(Bukkit.getLogger());
            this.logger.setLevel(Level.ALL);
        }
    }

    public void log(Reason reason, String message) {
        this.logger.log(Level.INFO, reason.getColour() + reason.getPrefix() + ShadowUtils.Colour(" " + message));
    }

    public void error(Reason reason, String message) {
        this.logger.log(Level.INFO, Reason.ERROR.getColour() + Reason.ERROR.getPrefix() + reason.getPrefix() + ShadowUtils.Colour(message));
    }

    public void startUp() {
        log(Reason.GENERIC, "");
        log(Reason.GENERIC, "ShadowGens");
        log(Reason.GENERIC, "Carbonate");
        log(Reason.GENERIC, "");
    }

    public enum

    Reason {

        GENERIC("", ChatColor.WHITE),
        ERROR("[Error] ", ChatColor.RED),
        CONFIG("[Config] ", ChatColor.BLUE),
        SQL("[SQL] ", ChatColor.GOLD),
        KEY("[Key] ", ChatColor.DARK_PURPLE),
        API("[API] ", ChatColor.GREEN);

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


package net.kigawa.spigot.pluginutil.message.logger;

import org.bukkit.ChatColor;

public class InfoLogger extends Logger {
    public InfoLogger(String title, net.kigawa.util.Logger logger) {
        super(logger);
        logger(ChatColor.GREEN + title);
    }
}

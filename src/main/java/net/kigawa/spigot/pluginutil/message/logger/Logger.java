package net.kigawa.spigot.pluginutil.message.logger;

import net.kigawa.spigot.pluginutil.PluginUtil;
import net.kigawa.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Logger {
    private net.kigawa.util.Logger logger;

    public Logger(net.kigawa.util.Logger logger) {
        this.logger = logger;
    }

    public void sendItem(String itemName, String description) {
        logger(ChatColor.BLUE + itemName + ChatColor.WHITE + ": " + description);
    }

    public void sendItem(String itemName, int[] description) {
        sendItem(itemName, Util.createString(description));
    }

    public void sendItem(String itemName, int i) {
        sendItem(itemName, Integer.toString(i));
    }

    public void sendItem(String itemName, List<Player> players) {
        sendItem(itemName, PluginUtil.createString(players));
    }

    public net.kigawa.util.Logger getLogger() {
        return this.logger;
    }

    public void logger(String message) {
        this.logger.logger(message);
    }
}

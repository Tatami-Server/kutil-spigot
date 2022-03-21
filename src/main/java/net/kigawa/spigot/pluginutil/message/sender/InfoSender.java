package net.kigawa.spigot.pluginutil.message.sender;

import net.kigawa.kutil.kutil.Kutil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoSender extends Sender {

    public InfoSender(String title, CommandSender sender) {
        super(sender);
        sender.sendMessage(ChatColor.GREEN + title);
    }

    public InfoSender(String title, List<Player> players) {
        super(Kutil.changeListType(players, CommandSender.class));
        sendMessage(Kutil.changeListType(players, CommandSender.class), ChatColor.GREEN + title);
    }

    public static String getString(String message) {
        return ChatColor.GREEN + "Info: " + message;
    }
}

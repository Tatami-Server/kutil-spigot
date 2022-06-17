package net.kigawa.spigot.pluginutil.message.sender;


import net.kigawa.kutil.kutil.Kutil;
import net.kigawa.spigot.pluginutil.message.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ErrorSender extends Sender {
    public ErrorSender(String title, Player player) {
        super(player);
        player.sendMessage(ChatColor.RED + title);
    }

    public ErrorSender(String title, List<Player> senders) {
        super(Kutil.changeListType(senders, CommandSender.class));
        Messenger.sendMessage(senders, title);
    }

    public static String getString(String message) {
        return ChatColor.RED + "Error: " + message;
    }
}

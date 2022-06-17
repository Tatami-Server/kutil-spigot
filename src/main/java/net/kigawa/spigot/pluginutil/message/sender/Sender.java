package net.kigawa.spigot.pluginutil.message.sender;

import net.kigawa.kutil.kutil.Kutil;
import net.kigawa.kutil.kutil.KutilString;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Sender {
    private final List<CommandSender> senders = new ArrayList<>();

    public Sender(CommandSender sender) {
        this.senders.add(sender);
    }

    public Sender(List<CommandSender> senders) {
        this.senders.addAll(senders);
    }

    public static void sendMessage(List<CommandSender> senders, String message) {
        for (CommandSender sender : senders) {
            sender.sendMessage(message);
        }
    }

    public void sendItem(String itemName, String description) {
        sendMessage(senders, ChatColor.BLUE + itemName + ChatColor.WHITE + ": " + description);
    }

    public void sendItem(String itemName, int[] description) {
        var sb = new StringBuffer();
        KutilString.insertSymbol(sb, ",", Kutil.castIntArray(description, new Integer[description.length]), Object::toString);
        sendItem(itemName, sb.toString());
    }

    public void sendItem(String itemName, int i) {
        sendItem(itemName, Integer.toString(i));
    }

    public List<CommandSender> getSenders() {
        return senders;
    }
}

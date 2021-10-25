package net.kigawa.spigot.pluginutil.message;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Messenger {
    PluginBase plugin;

    public Messenger(PluginBase plugin) {
        this.plugin = plugin;
    }

    public static void sendMessage(List<Player> players, String message) {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }

    public static void sendMessage(List<Player> players, TextComponent textComponent, ChatMessageType type) {
        for (Player player : players) {
            sendMessage(player, textComponent, type);
        }
    }

    public static void sendMessage(Player player, TextComponent textComponent, ChatMessageType type) {
        player.spigot().sendMessage(type, textComponent);
    }

    public void sendTitle(List<Player> players, String title, String subTitle) {
        sendTitle(players, title, subTitle, 10, 20, 10);
    }

    public void sendTitle(List<Player> players, String title) {
        sendTitle(players, title, "");
    }

    public void sendTitle(List<Player> players, String title, int fadein, int stay, int fadeout) {
        sendTitle(players, title, "", fadein, stay, fadeout);
    }

    public void sendTitle(List<Player> players, String title, String subTitle, int fadein, int stay, int fadeout) {
        for (Player player : players) {
            player.sendTitle(title, subTitle, fadein, stay, fadeout);
        }
    }

    public void sendTitleLater(List<Player> players, String title, Long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                sendTitle(players, title);
            }
        }.runTaskLater(plugin, delay);
    }
}

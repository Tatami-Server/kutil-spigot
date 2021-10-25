package net.kigawa.spigot.pluginutil.timer;

import net.kigawa.spigot.pluginutil.PluginBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class Counter extends BukkitRunnable {
    int count;
    PluginBase plugin;
    Scoreboard bord;
    Objective objective;
    Score score;
    int titleCount;
    List<Player> players;
    String lastMessage;
    ChatColor countdownColor;
    String unit;
    Counter oneMinCounter;

    public Counter(String bordName, String bordID, PluginBase kigawaPlugin) {
        bord = Bukkit.getScoreboardManager().getNewScoreboard();
        plugin = kigawaPlugin;
        objective = bord.registerNewObjective(bordID, "dummy", bordName);
    }

    public void startSec(Long delay, int count, int titleCount, List<Player> players, String lastMessage, ChatColor countdownColor) {
        this.count = count;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        score = objective.getScore("時間(秒)");
        this.titleCount = titleCount;
        this.players = players;
        this.lastMessage = lastMessage;
        this.countdownColor = countdownColor;
        for (Player player : players) {
            player.setScoreboard(bord);
        }
        unit = "sec";
        runTaskTimer(plugin, delay, 20);
    }

    public void startMin(Long delay, int count, int titleCount, List<Player> players, String lastMessage, ChatColor countdownColor) {
        this.count = count;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        score = objective.getScore("時間(分)");
        this.titleCount = titleCount;
        this.players = players;
        this.lastMessage = lastMessage;
        this.countdownColor = countdownColor;
        for (Player player : players) {
            player.setScoreboard(bord);
        }
        unit = "min";
        runTaskTimer(plugin, delay, 20 * 60);

    }

    public void onRun() {
    }

    public void end() {
        //reset
        bord.resetScores("時間(分)");
        if (oneMinCounter != null) oneMinCounter.end();
        for (Player player : players) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
        cancel();
    }

    @Override
    public void run() {
        onRun();
        plugin.logger("run");
        score.setScore(count);
        if (unit.equals("sec")) {
            //count down
            if (count <= titleCount) {
                plugin.getMessenger().sendTitle(players, countdownColor + Integer.toString(count), "");
            }
            //send message
            if (count == 0) {
                sendLastMessage();
                bord.resetScores("時間(秒)");
                end();
            }
        } else {
            if (unit.equals("min")) {
                if (count == 1) {
                    plugin.getMessenger().sendTitle(players, countdownColor + Integer.toString(count) + "分", "");
                    bord.resetScores("時間(分)");
                    //reset
                    bord.resetScores("時間(分)");
                    if (oneMinCounter != null) oneMinCounter.end();
                    for (Player player : players) {
                        player.setScoreboard(player.getScoreboard());
                    }
                    //create new counter
                    oneMinCounter = new Counter(objective.getDisplayName(), objective.getName(), plugin);
                    oneMinCounter.startSec(0L, 60, titleCount, players, lastMessage, countdownColor);
                    cancel();
                }
            }
        }
        count--;
    }

    public void sendLastMessage() {
        plugin.getMessenger().sendTitle(players, lastMessage, "");
    }
}

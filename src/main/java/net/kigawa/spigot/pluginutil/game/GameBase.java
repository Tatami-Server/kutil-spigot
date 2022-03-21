package net.kigawa.spigot.pluginutil.game;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.spigot.pluginutil.message.sender.ErrorSender;
import net.kigawa.spigot.pluginutil.player.User;
import net.kigawa.spigot.pluginutil.player.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class GameBase<D extends GameDataBase, M extends GameManagerBase, U extends User> {
    private final D data;
    private final M manager;
    private final PluginBase plugin;
    private final UserManager<U> userManager;
    private final World world;
    private boolean timer = true;
    private int tick = 0;
    private int sec = 0;
    private int min = 0;

    public GameBase(D data, M manager) {
        this.data = data;
        this.manager = manager;
        plugin = manager.getPlugin();
        userManager = new UserManager<U>(plugin);
        world = plugin.getServer().getWorld(data.getWorld());

        timer();
    }

    public abstract U newUser(Player player);

    public abstract String onStart();

    public abstract String onEnd();

    public abstract void tickTimer();

    public abstract void secTimer();

    public abstract void minTimer();

    public void timer() {
        if (timer) {
            tickTimer();
            tick++;

            if (tick >= 20) {
                secTimer();
                tick = 0;
                sec++;

                if (sec >= 60) {
                    minTimer();
                    sec = 0;
                    min++;
                }
            }
            Bukkit.getScheduler().runTaskLater(plugin, this::timer, 1);
        }
    }

    public void stopTimer() {
        timer = false;
    }

    public boolean contain(D data) {
        return (data.equals(data));
    }

    public boolean equals(String name) {
        return data.getName().equals(name);
    }

    public String end() {
        var end = onEnd();
        stopTimer();
        manager.removeGame(this);
        return end;
    }

    public String start() {
        if (world == null) {
            return ErrorSender.getString(data.getWorld() + "is not exit");
        }
        for (Player player : world.getPlayers()) {
            newUser(player);
        }
        return onStart();
    }

    public PluginBase getPlugin() {
        return plugin;
    }

    public UserManager<U> getUserManager() {
        return userManager;
    }

    public U getUser(Player player) {
        return userManager.getUser(player);
    }

    public M getManager() {
        return manager;
    }

    public D getData() {
        return data;
    }

    public World getWorld() {
        return world;
    }
}

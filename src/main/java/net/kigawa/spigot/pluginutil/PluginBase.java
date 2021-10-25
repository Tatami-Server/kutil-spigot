package net.kigawa.spigot.pluginutil;

import net.kigawa.spigot.pluginutil.command.FirstCommand;
import net.kigawa.spigot.pluginutil.message.Messenger;
import net.kigawa.spigot.pluginutil.player.PlayerGetter;
import net.kigawa.spigot.pluginutil.player.Teleporter;
import net.kigawa.spigot.pluginutil.player.User;
import net.kigawa.spigot.pluginutil.player.UserManager;
import net.kigawa.spigot.pluginutil.recorder.Recorder;
import net.kigawa.util.HasEnd;
import net.kigawa.util.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class PluginBase extends JavaPlugin implements Logger, Listener {
    private final List<FirstCommand> commands = new ArrayList<>();
    private final List<HasEnd> hasEnds = new ArrayList<>();
    private boolean debug;
    private Recorder recorder;
    private PlayerGetter playerGetter;
    private Messenger messenger;
    private Teleporter teleporter;
    private UserManager userManager;

    public abstract void addConfigDefault(FileConfiguration config);

    public abstract void onStart();

    @Override
    public void onLoad() {
        logger("onLoad");
    }

    @Override
    public void onEnable() {
        logger("onEnable");
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        config.addDefault("debug", false);
        config.addDefault("useDB", false);
        addConfigDefault(config);
        config.options().copyDefaults(true);
        this.saveConfig();
        debug = config.getBoolean("debug");
        getServer().getPluginManager().registerEvents(this, this);

        recorder = new Recorder(this);
        playerGetter = new PlayerGetter(this);
        messenger = new Messenger(this);
        teleporter = new Teleporter();
        userManager = new UserManager(this);

        onStart();
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent event) {
        User.playerJoinEvent(event, this);
    }

    @EventHandler
    public void leaveEvent(PlayerQuitEvent event) {
        User.playerQuitEvent(event);
    }

    @Override
    public void onDisable() {
        logger("onDisable");
        for (HasEnd hasEnd : hasEnds) {
            hasEnd.end();
        }
        User.onDisable(this);
    }

    public Teleporter getTeleporter() {
        return teleporter;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public void registerEvents(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void logger(String message) {
        if (debug) {
            this.getLogger().info(message);
        }
    }

    public void logger(int message) {
        if (debug) {
            this.getLogger().info(Integer.toString(message));
        }
    }

    public void logger(boolean message) {
        logger(String.valueOf(message));
    }

    public void logger(double message) {
        logger(Double.toString(message));
    }

    public void addCommand(FirstCommand command) {
        commands.add(command);
        List<String> permission = new ArrayList<>();
        permission.add(getName());
        command.setPermission(permission);
    }

    public void addHasEnd(HasEnd hasEnd) {
        hasEnds.add(hasEnd);
    }

    public List<FirstCommand> getCommands() {
        return commands;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public PlayerGetter getPlayerGetter() {
        return playerGetter;
    }
}

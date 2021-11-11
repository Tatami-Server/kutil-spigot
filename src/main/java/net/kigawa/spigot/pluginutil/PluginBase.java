package net.kigawa.spigot.pluginutil;

import net.kigawa.spigot.pluginutil.command.Command;
import net.kigawa.spigot.pluginutil.command.CommandParent;
import net.kigawa.spigot.pluginutil.command.FirstCommand;
import net.kigawa.spigot.pluginutil.message.Messenger;
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

public abstract class PluginBase extends JavaPlugin implements Logger, Listener, CommandParent {
    private final List<FirstCommand> commands = new ArrayList<>();
    private final List<HasEnd> hasEnds = new ArrayList<>();
    private boolean debug;
    private Recorder recorder;
    private Messenger messenger;
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
        messenger = new Messenger(this);
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

    public void addCommand(Command command) {
        FirstCommand firstCommand = (FirstCommand) command;
        commands.add(firstCommand);
        List<String> permission = new ArrayList<>();
        permission.add(getName());
        firstCommand.setPermission(permission);
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
}

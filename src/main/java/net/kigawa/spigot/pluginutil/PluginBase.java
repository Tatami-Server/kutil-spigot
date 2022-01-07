package net.kigawa.spigot.pluginutil;

import net.kigawa.file.FileUtil;
import net.kigawa.interfaces.HasEnd;
import net.kigawa.log.Logger;
import net.kigawa.spigot.pluginutil.command.CommandManager;
import net.kigawa.spigot.pluginutil.command.CommandParent;
import net.kigawa.spigot.pluginutil.message.Messenger;
import net.kigawa.spigot.pluginutil.player.User;
import net.kigawa.spigot.pluginutil.player.UserManager;
import net.kigawa.spigot.pluginutil.recorder.Recorder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class PluginBase extends JavaPlugin implements Listener, CommandParent {
    public static boolean debug;
    public static boolean useDB;
    public static boolean log;
    private final List<HasEnd> hasEnds = new ArrayList<>();
    private net.kigawa.log.Logger logger;
    private Recorder recorder;
    private Messenger messenger;
    private UserManager userManager;

    public abstract void addConfigDefault(FileConfiguration config);

    public abstract void enable();

    public abstract void disable();

    public abstract void load();

    /**
     * @deprecated
     */
    public void onStart() {
    }


    @Override
    public void onLoad() {
        Logger.getInstance().info("loading...");


        load();
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        config.addDefault("debug", false);
        config.addDefault("useDB", false);
        config.addDefault("log", true);

        addConfigDefault(config);
        config.options().copyDefaults(true);
        this.saveConfig();

        debug = config.getBoolean("debug");
        useDB = config.getBoolean("useDB");
        log = config.getBoolean("log");

        Level level = Level.INFO;
        if (debug) level = Level.FINE;
        File logDir = null;
        if (log) logDir = FileUtil.getFile(getDataFolder(), "logs");

        Logger.enable(getName(), getLogger(), level, logDir);
        CommandManager.enable(this);

        Logger.getInstance().info("enable " + getName());

        recorder = new Recorder(this);
        messenger = new Messenger(this);
        userManager = new UserManager(this);

        registerEvents(this);

        onStart();
        enable();
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
        Logger.getInstance().info("disable " + getName());
        for (HasEnd hasEnd : hasEnds) {
            hasEnd.end();
        }
        User.onDisable(this);

        disable();
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public void registerEvents(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * @deprecated
     */
    public void logger(String message) {
        if (debug) {
            this.getLogger().info(message);
        }
    }

    /**
     * @deprecated
     */
    public void logger(int message) {
        if (debug) {
            this.getLogger().info(Integer.toString(message));
        }
    }

    /**
     * @deprecated
     */
    public void logger(boolean message) {
        logger(String.valueOf(message));
    }

    /**
     * @deprecated
     */
    public void logger(double message) {
        logger(Double.toString(message));
    }

    public void addHasEnd(HasEnd hasEnd) {
        hasEnds.add(hasEnd);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public CommandParent getCommandParent() {
        return null;
    }
}

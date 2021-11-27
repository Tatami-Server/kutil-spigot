package net.kigawa.spigot.pluginutil;

import net.kigawa.spigot.pluginutil.command.Command;
import net.kigawa.spigot.pluginutil.command.CommandParent;
import net.kigawa.spigot.pluginutil.command.FirstCommand;
import net.kigawa.spigot.pluginutil.message.Messenger;
import net.kigawa.spigot.pluginutil.player.User;
import net.kigawa.spigot.pluginutil.player.UserManager;
import net.kigawa.spigot.pluginutil.recorder.Recorder;
import net.kigawa.util.HasEnd;
import net.kigawa.util.InterfaceLogger;
import net.kigawa.util.Logger;
import org.bukkit.command.CommandSender;
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

public abstract class PluginBase extends JavaPlugin implements InterfaceLogger, Listener, CommandParent {
    private final List<FirstCommand> commands = new ArrayList<>();
    private final List<HasEnd> hasEnds = new ArrayList<>();
    public static boolean debug;
    public static boolean useDB;
    public static boolean log;
    private Logger logger;
    private Recorder recorder;
    private Messenger messenger;
    private UserManager userManager;

    public abstract void addConfigDefault(FileConfiguration config);

    public abstract void onStart();

    @Override
    public void info(Object o) {
        logger.info(o);
    }

    @Override
    public void warning(Object o) {
        logger.warning(o);
    }

    @Override
    public void title(Object o) {
        logger.title(o);
    }

    @Override
    public void debug(Object o) {
        logger.debug(o);
    }

    @Override
    public void onLoad() {
        logger("onLoad");
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

        logger = new Logger(
                new File(getDataFolder(), "logs"),
                config.getBoolean("log"),
                config.getBoolean("debug"),
                (String s) -> getLogger().log(Level.INFO, s),
                false
        );
        info("enabling " + getName());
        debug = config.getBoolean("debug");
        useDB = config.getBoolean("useDB");
        log = config.getBoolean("log");
        recorder = new Recorder(this);
        messenger = new Messenger(this);
        userManager = new UserManager(this);


        registerEvents(this);

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

    public void addCommand(Command command) {
        FirstCommand firstCommand = (FirstCommand) command;
        commands.add(firstCommand);
        List<String> permission = new ArrayList<>();
        permission.add(getName());
    }

    @Override
    public StringBuffer getPermission(CommandSender sender) {
        if (sender.hasPermission(getName()) | sender.hasPermission(getName() + ".*")) return null;
        return new StringBuffer(getName());
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

    @Override
    public int getWordNumber() {
        return 0;
    }

    public CommandParent getCommandParent() {
        return null;
    }
}

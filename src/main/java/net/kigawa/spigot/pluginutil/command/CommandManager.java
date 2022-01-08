package net.kigawa.spigot.pluginutil.command;

import net.kigawa.log.LogSender;
import net.kigawa.log.Logger;
import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.spigot.pluginutil.SpigotUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements LogSender {
    private static CommandManager commandManager;
    private static CommandMap commandMap;

    private final List<AbstractCmdProcess> commandList = new ArrayList<>();
    private final PluginBase pluginBase;

    private CommandManager(PluginBase pluginBase) {
        this.pluginBase = pluginBase;
    }

    public static CommandManager getInstance() {
        return commandManager;
    }

    public static void enable(PluginBase pluginBase) {
        commandManager = new CommandManager(pluginBase);
        try {
            Class<?> cla = Class.forName(SpigotUtil.getCraftPackage() + ".CraftServer");
            Method method = cla.getMethod("getCommandMap");
            commandMap = (CommandMap) method.invoke(Bukkit.getServer());
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Logger.getInstance().warning(e);
        }
    }

    public AbstractCmdProcess getCommand(String name) {
        for (AbstractCmdProcess cmd : commandList) {
            if (cmd.getName().equals(name)) return cmd;
        }
        return null;
    }

    public void register(AbstractCmdProcess command) {
        commandMap.register(command.getName(), command);
        commandList.add(command);
    }

    PluginBase getPluginBase() {
        return pluginBase;
    }
}

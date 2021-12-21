package net.kigawa.spigot.pluginutil.command;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.util.LogSender;
import net.kigawa.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommandManager extends LogSender {
    private static CommandManager commandManager;

    private final List<Command> commandList = new ArrayList<>();
    private final PluginBase pluginBase;

    private CommandManager(PluginBase pluginBase) {
        this.pluginBase = pluginBase;
    }

    public static CommandManager getInstance() {
        return commandManager;
    }

    public static void enable(PluginBase pluginBase) {
        commandManager = new CommandManager(pluginBase);
    }

    public PluginCommand registerCommand(String command) {
        try {
            Class<PluginCommand> commandClass = PluginCommand.class;
            Constructor<PluginCommand> commandConstructor = commandClass.getDeclaredConstructor(String.class, Plugin.class);
            PluginCommand pluginCommand = commandConstructor.newInstance(command, pluginBase);

            Class<?> serverClass = Class.forName("org.bukkit.craftbukkit.CraftServer");
            Method method = serverClass.getMethod("getCommandMap");
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) method.invoke(Bukkit.getServer());

            simpleCommandMap.register(pluginBase.getName(), pluginCommand);

            return pluginCommand;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            Logger.getInstance().warning(e);
        }
        return null;
    }

    public void setExecutor(FirstCommand firstCommand) {
        PluginCommand pluginCommand = pluginBase.getCommand(firstCommand.getName());
        if (pluginCommand == null) pluginCommand = registerCommand(firstCommand.getName());
        if (pluginCommand == null) {
            Logger.getInstance().warning("can't register command");
            return;
        }
        pluginCommand.setExecutor(firstCommand);
    }
}

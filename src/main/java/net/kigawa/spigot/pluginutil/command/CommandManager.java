package net.kigawa.spigot.pluginutil.command;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.util.LogSender;
import net.kigawa.util.Logger;
import org.bukkit.command.PluginCommand;

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

    public void setExecutor(FirstCommand firstCommand) {
        PluginCommand pluginCommand = pluginBase.getCommand(firstCommand.getName());
        if (pluginCommand == null) {
            Logger.getInstance().warning("can't register command");
            return;
        }
        pluginCommand.setExecutor(firstCommand);
    }
}

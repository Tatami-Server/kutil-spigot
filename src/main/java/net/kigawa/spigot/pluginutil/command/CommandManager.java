package net.kigawa.spigot.pluginutil.command;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.util.LogSender;
import net.kigawa.util.Logger;
import org.bukkit.command.PluginCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends LogSender {
    private static CommandManager commandManager;

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
    }

    public AbstractCmdProcess getCommand(String name) {
        for (AbstractCmdProcess cmd : commandList) {
            if (cmd.getName().equals(name)) return cmd;
        }
        return null;
    }

    public void setExecutor(AbstractCmdProcess cmdProcess) {
        PluginCommand pluginCommand = pluginBase.getCommand(cmdProcess.getName());
        if (pluginCommand == null) {
            Logger.getInstance().warning("can't register command");
            return;
        }
        pluginCommand.setExecutor(cmdProcess);
        commandList.add(cmdProcess);
    }
}

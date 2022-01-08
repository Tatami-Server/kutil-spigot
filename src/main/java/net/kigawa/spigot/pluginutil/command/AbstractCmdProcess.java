package net.kigawa.spigot.pluginutil.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.LinkedList;

public class AbstractCmdProcess extends BukkitCommand {
    private final AbstractCmd command;

    public AbstractCmdProcess(AbstractCmd command) {
        super(command.getName());
        this.command = command;
        command.setPermission(CommandManager.getInstance().getPluginBase().getName());
        CommandManager.getInstance().register(this);
    }

    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] subcommands) {
        LinkedList<String> strCmd = new LinkedList<>();
        CommandLine commandLine = new CommandLine(sender);
        Collections.addAll(strCmd, subcommands);
        commandLine.addCmd(command, label);
        sender.sendMessage(command.onCommand(strCmd, commandLine));
        return true;
    }

    private boolean error(String message) {
        Bukkit.broadcastMessage(message);
        return false;
    }

    public AbstractCmd getCommand() {
        return command;
    }

    @Override
    public @Nonnull
    String getName() {
        return command.getName();
    }
}

package net.kigawa.spigot.pluginutil.command;

import net.kigawa.log.Logger;
import net.kigawa.string.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class AbstractCmdProcess extends BukkitCommand {
    private final AbstractCmd command;

    public AbstractCmdProcess(AbstractCmd command) {
        super(command.getName());
        this.command = command;
        command.setPermission(CommandManager.getInstance().getPluginBase().getName());
        CommandManager.getInstance().register(this);
    }

    @Override
    @Nonnull
    public List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) {
        LinkedList<String> cmd = new LinkedList<>();
        Collections.addAll(cmd, args);
        return command.onTabComplete(cmd);
    }

    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] subcommands) {
        LinkedList<String> strCmd = new LinkedList<>();
        CommandLine commandLine = new CommandLine(sender);
        Collections.addAll(strCmd, subcommands);
        commandLine.addCmd(command, label);

        Logger.getInstance().anSyncLog(new Logger.Log() {
            @Override
            public String toString() {
                LinkedList<String> result = new LinkedList<>();
                result.add(label);
                result.addAll(strCmd);
                StringBuffer sb = new StringBuffer(sender.getName());
                sb.append(": /");
                return StringUtil.insertSymbol(sb, " ", result).toString();
            }
        }, Level.INFO);

        sender.sendMessage(command.onCommand(strCmd, commandLine));
        return true;
    }

    protected boolean error(String message) {
        Bukkit.broadcastMessage(message);
        return false;
    }

    public AbstractCmd getCommand() {
        return command;
    }

    public void addCommand(AbstractCmd... abstractCmd) {
        command.addCmd(abstractCmd);
    }

    @Override
    public @Nonnull
    String getName() {
        return command.getName();
    }
}

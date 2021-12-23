package net.kigawa.spigot.pluginutil.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AbstractCmdProcess implements CommandExecutor, CommandParent {
    private final Commands commands = new Commands();

    public AbstractCmdProcess() {
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        final CommandLine commandLine = new CommandLine(commandSender, command, s, strings);

        Commands commands = this.commands;
        AbstractCmd latest = null;
        CommandVars commandVars = commandLine.getCommandVars();

        for (String cmdStr : commandLine) {
            AbstractCmd abstractCmd = commands.getCommand(cmdStr);
            if (abstractCmd == null) break;
            if (abstractCmd.isVar())
                if (abstractCmd.allowValue(cmdStr))
                    abstractCmd.setValue(commandVars, cmdStr);
                else break;
            latest = abstractCmd;
            commands = abstractCmd.getCommands();
        }

        if (latest == null) return false;

        commandSender.sendMessage(latest.onCommand(commandLine));

        return true;
    }

    @Override
    public void addCommands(AbstractCmd... commands) {
        this.commands.addCommands(commands);
        for (AbstractCmd cmd : commands) {
            cmd.setCommandParent(this);
        }
    }
}

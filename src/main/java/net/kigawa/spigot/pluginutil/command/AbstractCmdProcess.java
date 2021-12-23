package net.kigawa.spigot.pluginutil.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AbstractCmdProcess implements CommandExecutor {
    private final AbstractCmd command;

    public AbstractCmdProcess(AbstractCmd command) {
        this.command = command;
        CommandManager.getInstance().setExecutor(this);
    }

    public String getName() {
        return command.getName();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        final CommandLine commandLine = new CommandLine(commandSender, command, s, strings);

        Commands commands = null;
        AbstractCmd latest = null;
        CommandVars commandVars = commandLine.getCommandVars();
        AbstractCmd cmd;

        for (String cmdStr : commandLine) {
            if (commands == null) cmd = this.command;
            else cmd = commands.getCommand(cmdStr);

            if (cmd == null) break;
            if (cmd.isVar())
                if (cmd.allowValue(cmdStr))
                    cmd.setValue(commandVars, cmdStr);
                else break;
            latest = cmd;
            commands = cmd.getCommands();
        }

        if (latest == null) return false;

        commandSender.sendMessage(latest.onCommand(commandLine));

        return true;
    }
}

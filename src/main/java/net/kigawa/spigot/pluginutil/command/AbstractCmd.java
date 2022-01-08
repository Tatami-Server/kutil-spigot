package net.kigawa.spigot.pluginutil.command;

import net.kigawa.log.LogSender;
import net.kigawa.log.Logger;
import net.kigawa.spigot.pluginutil.message.sender.ErrorSender;
import net.kigawa.string.StringUtil;
import org.bukkit.permissions.Permission;

import java.util.LinkedList;
import java.util.function.Function;
import java.util.logging.Level;

public abstract class AbstractCmd implements LogSender {
    private final String name;
    private final Function<CommandLine, String> function;
    private final Commands commands = new Commands();
    private Permission permission;
    private AbstractCmd commandParent;

    public AbstractCmd(String name, Function<CommandLine, String> function, AbstractCmd... commands) {
        this.name = name;
        this.function = function;
        if (commands != null) this.commands.addCommands(commands);
        permission = new Permission(name);
    }

    protected abstract boolean matchCommand(String command);

    protected abstract boolean isVar();

    protected abstract void setValue(CommandVars commandVars, String value);

    protected abstract boolean allowValue(String value);

    String onCommand(LinkedList<String> strCmd, CommandLine commandLine) {
        fine("onCommand " + name);
        if (strCmd.isEmpty()) {
            if (function == null) return error(commandLine);
            if (!hasPermission(commandLine)) return permissionError(commandLine);
            return logger.infoPass(function.apply(commandLine));
        }
        AbstractCmd cmd = commands.getCommand(strCmd.get(0));
        strCmd.remove(0);
        if (cmd != null) return cmd.onCommand(strCmd, commandLine);
        else return error(commandLine);
    }

    private boolean hasPermission(CommandLine commandLine) {
        return commandLine.getSender().hasPermission(permission);
    }

    private String permissionError(CommandLine commandLine) {
        return logger.infoPass(ErrorSender.getString("need permission: " + permission.getName()));
    }

    private String error(CommandLine commandLine) {
        LinkedList<String> cmd = new LinkedList<>(commandLine.strCmd);
        cmd.addAll(getSubCommandDescription());
        StringBuffer sb = new StringBuffer("/");
        StringUtil.insertSymbol(sb, " ", cmd);
        return logger.infoPass(ErrorSender.getString(sb.toString()));
    }

    private LinkedList<String> getSubCommandDescription() {
        LinkedList<String> cmd = new LinkedList<>();
        if (commands.size() == 0) return cmd;
        if (commands.size() >= 2) {
            cmd.add("<subcommand>");
            return cmd;
        }
        AbstractCmd abstractCmd = commands.get(0);
        cmd.add(abstractCmd.name);
        cmd.addAll(abstractCmd.getSubCommandDescription());

        return cmd;
    }

    public AbstractCmd getParent() {
        return commandParent;
    }

    public Commands getCommands() {
        return commands;
    }

    public void addCommands(AbstractCmd... commands) {
        this.commands.addCommands(commands);
        for (AbstractCmd cmd : commands) {
            cmd.setCommandParent(this);
        }
    }

    public String getName() {
        return name;
    }

    protected void setCommandParent(AbstractCmd commandParent) {
        this.commandParent = commandParent;
        setPermission(commandParent.permission.getName());
    }

    void setPermission(String parent) {
        permission = new Permission(parent + "." + name);
    }

    public boolean equals(Object o) {
        if (o instanceof AbstractCmd) return equals((AbstractCmd) o);
        return false;
    }

    public boolean equals(AbstractCmd command) {
        return command.getName().equals(getName());
    }
}

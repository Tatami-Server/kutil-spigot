package net.kigawa.spigot.pluginutil.command;

import org.bukkit.ChatColor;

import java.util.function.Function;

public abstract class AbstractCmd {
    private final String name;
    private final Function<CommandLine, String> function;
    private final Commands commands = new Commands();
    private AbstractCmd commandParent;

    public AbstractCmd(String name, Function<CommandLine, String> function, AbstractCmd... commands) {
        this.name = name;
        this.function = function;
        if (commands != null) this.commands.addCommands(commands);
    }

    protected abstract boolean matchCommand(String command);

    protected abstract boolean isVar();

    protected abstract void setValue(CommandVars commandVars, String value);

    protected abstract boolean allowValue(String value);

    protected String onCommand(CommandLine commandLine, int arg) {
        if (arg != commandLine.size()) return error(commandLine);
        if (function == null) return error(commandLine);

        String result = function.apply(commandLine);
        if (result == null) return error(commandLine);
        return result;
    }

    private String error(CommandLine commandLine) {
        AbstractCmd cmd = this;
        CommandBuilder commandBuilder = new CommandBuilder();
        do {
            if (cmd.isVar()) commandBuilder.addVarBefore(cmd.getName());
            else commandBuilder.addCmdBefore(cmd.getName());
        } while ((cmd = cmd.getParent()) != null);

        String subCommand = getSubCommandDescription();
        if (subCommand != null) commandBuilder.addCmd(subCommand);

        return ChatColor.RED + "Error: " + commandBuilder;
    }

    private String getSubCommandDescription() {
        if (commands.size() == 0) return null;
        if (commands.size() >= 2) return "<subcommand>";

        AbstractCmd cmd = commands.get(0);
        CommandBuilder commandBuilder = new CommandBuilder();

        if (cmd.isVar()) commandBuilder.addVar(cmd.getName());
        else commandBuilder.addCmd(cmd.getName());

        String subCommand = cmd.getSubCommandDescription();
        if (subCommand != null) commandBuilder.addCmd(subCommand);

        return commandBuilder.toString();
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
    }

    public boolean equals(Object o) {
        if (o instanceof AbstractCmd) return equals((AbstractCmd) o);
        return false;
    }

    public boolean equals(AbstractCmd command) {
        return command.getName().equals(getName());
    }
}

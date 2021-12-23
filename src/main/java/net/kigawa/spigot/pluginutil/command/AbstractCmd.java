package net.kigawa.spigot.pluginutil.command;

import java.util.function.Function;

public abstract class AbstractCmd implements CommandParent {
    private final String name;
    private final Function<CommandLine, String> function;
    private final Commands commands = new Commands();
    private CommandParent commandParent;

    public AbstractCmd(String name, Function<CommandLine, String> function, AbstractCmd... commands) {
        this.name = name;
        this.function = function;
        if (commands != null) this.commands.addCommands(commands);
    }

    protected abstract boolean matchCommand(String command);

    protected abstract boolean isVar();

    protected abstract void setValue(CommandVars commandVars, String value);

    protected abstract boolean allowValue(String value);

    protected String onCommand(CommandLine commandLine) {
        return function.apply(commandLine);
    }

    public Commands getCommands() {
        return commands;
    }

    @Override
    public void addCommands(AbstractCmd... commands) {
        this.commands.addCommands(commands);
        for (AbstractCmd cmd : commands) {
            cmd.setCommandParent(this);
        }
    }

    public String getName() {
        return null;
    }

    protected void setCommandParent(CommandParent commandParent) {
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

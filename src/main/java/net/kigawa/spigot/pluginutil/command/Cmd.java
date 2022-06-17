package net.kigawa.spigot.pluginutil.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Cmd extends AbstractCmd {
    private final List<String> list = new ArrayList<>();

    public Cmd(String name, Function<CommandLine, String> function, AbstractCmd... subcommands) {
        super(name, function, subcommands);
        list.add(name);
    }

    @Override
    protected boolean matchCommand(String command) {
        return command.equalsIgnoreCase(getName());
    }

    @Override
    protected List<String> completeVar() {
        return list;
    }
}

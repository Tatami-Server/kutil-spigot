package net.kigawa.spigot.pluginutil.command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CmdStrVar extends AbstractCmd {
    protected List<String> list = new ArrayList<>();

    public CmdStrVar(String name, Function<CommandLine, String> function, AbstractCmd... subcommands) {
        super(name, function, subcommands);
    }

    @Override
    protected boolean matchCommand(String command) {
        return true;
    }

    @Override
    protected List<String> completeVar() {
        return list;
    }
}

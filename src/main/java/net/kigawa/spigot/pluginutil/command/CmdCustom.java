package net.kigawa.spigot.pluginutil.command;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CmdCustom extends AbstractCmd {
    private Predicate<String> match;
    private Supplier<List<String>> complete;

    public CmdCustom(String name, Function<CommandLine, String> function, AbstractCmd... subcommands) {
        super(name, function, subcommands);
    }

    public CmdCustom setMatch(Predicate<String> match) {
        this.match = match;
        return this;
    }

    public CmdCustom setComplete(Supplier<List<String>> complete) {
        this.complete = complete;
        return this;
    }

    @Override
    protected boolean matchCommand(String command) {
        return match.test(command);
    }

    @Override
    protected List<String> completeVar() {
        return complete.get();
    }
}

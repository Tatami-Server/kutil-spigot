package net.kigawa.spigot.pluginutil.command;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CustomCmd extends AbstractCmd {
    private Predicate<String> match;
    private Supplier<List<String>> complete;

    public CustomCmd(String name, Function<CommandLine, String> function, AbstractCmd... subcommands) {
        super(name, function, subcommands);
    }

    public CustomCmd setMatch(Predicate<String> match) {
        this.match = match;
        return this;
    }

    public CustomCmd setComplete(Supplier<List<String>> complete) {
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

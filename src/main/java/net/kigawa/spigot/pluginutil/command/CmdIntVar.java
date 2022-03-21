package net.kigawa.spigot.pluginutil.command;

import net.kigawa.kutil.kutil.KutilString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CmdIntVar extends AbstractCmd {
    private final List<String> list = new ArrayList<>();
    private int max = Integer.MAX_VALUE;
    private int min = Integer.MIN_VALUE;

    public CmdIntVar(String name, Function<CommandLine, String> function, AbstractCmd... subcommands) {
        super(name, function, subcommands);
    }

    public CmdIntVar setLength(int max, int min) {
        this.max = max;
        this.min = min;
        return this;
    }

    public CmdIntVar setValue(int... ints) {
        for (int i : ints) {
            list.add(Integer.toString(i));
        }
        return this;
    }

    @Override
    protected boolean matchCommand(String command) {
        if (!KutilString.isInt(command)) return false;
        int value = Integer.parseInt(command);
        if (value >= max) return false;
        if (value <= min) return false;
        return true;
    }

    @Override
    protected List<String> completeVar() {
        return list;
    }
}

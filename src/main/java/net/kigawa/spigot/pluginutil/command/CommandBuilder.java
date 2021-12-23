package net.kigawa.spigot.pluginutil.command;

import net.kigawa.spigot.pluginutil.Parents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandBuilder {
    private final List<String> command = new ArrayList<>();
    private Parents parents = Parents.LESS_GREATER;

    public CommandBuilder(String... strings) {
        Collections.addAll(command, strings);
    }

    public CommandBuilder addCmd(Object o) {
        command.add(o.toString());
        return this;
    }

    public CommandBuilder addVar(Object o) {
        return addCmd(parents.start() + o + parents.end());
    }

    public CommandBuilder addCmdBefore(Object o) {
        command.set(0, o.toString());
        return this;
    }

    public CommandBuilder addVarBefore(Object o) {
        return addCmd(parents.start() + o + parents.end());
    }

    public void setParents(Parents parents) {
        this.parents = parents;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(command.get(0));
        for (int i = 1; i < command.size(); i++) {
            sb.append(" ").append(command.get(i));
        }
        return sb.toString();
    }
}

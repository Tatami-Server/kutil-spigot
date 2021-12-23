package net.kigawa.spigot.pluginutil.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Commands implements Iterable<AbstractCmd> {
    private final List<AbstractCmd> commands = new ArrayList<>();

    protected Commands() {
    }

    protected AbstractCmd getCommand(String label) {
        for (AbstractCmd cmd : commands) {
            if (cmd.matchCommand(label)) return cmd;
        }
        return null;
    }

    public AbstractCmd get(int i) {
        return commands.get(i);
    }

    public int size() {
        return commands.size();
    }

    public void addCommands(AbstractCmd... commands) {
        Collections.addAll(this.commands, commands);
    }

    public void removeCommands(AbstractCmd... commands) {
        for (AbstractCmd command : commands) {
            this.commands.remove(command);
        }
    }

    protected void execCmd(Consumer<AbstractCmd> abstractCmdConsumer) {
        for (AbstractCmd abstractCmd : commands) {
            abstractCmdConsumer.accept(abstractCmd);
        }
    }

    @Override
    public Iterator<AbstractCmd> iterator() {
        return commands.iterator();
    }
}

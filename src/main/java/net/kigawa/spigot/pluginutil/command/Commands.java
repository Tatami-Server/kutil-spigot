package net.kigawa.spigot.pluginutil.command;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public class Commands implements Iterable<AbstractCmd> {
    private final Set<AbstractCmd> commands = new HashSet<>();

    protected Commands() {
    }

    protected AbstractCmd getCommand(String label) {
        for (AbstractCmd cmd : commands) {
            if (cmd.matchCommand(label)) return cmd;
        }
        return null;
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

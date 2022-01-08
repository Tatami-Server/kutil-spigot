package net.kigawa.spigot.pluginutil.command;

import org.bukkit.command.CommandSender;

import java.util.Iterator;
import java.util.LinkedList;

public class CommandLine implements Iterable<AbstractCmd> {
    final LinkedList<String> strCmd = new LinkedList<>();
    final LinkedList<AbstractCmd> cmd = new LinkedList<>();
    private final CommandSender sender;

    protected CommandLine(CommandSender sender) {
        this.sender = sender;
    }

    void addCmd(AbstractCmd cmd, String strCmd) {
        this.cmd.add(cmd);
        this.strCmd.add(strCmd);
    }

    public int size() {
        return cmd.size();
    }

    public CommandSender getSender() {
        return sender;
    }

    @Override
    public Iterator<AbstractCmd> iterator() {
        return cmd.iterator();
    }
}

package net.kigawa.spigot.pluginutil.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.LinkedList;

public class CommandLine implements Iterable<AbstractCmd> {
    final LinkedList<String> strCmdList = new LinkedList<>();
    final LinkedList<AbstractCmd> cmdList = new LinkedList<>();
    private final CommandSender sender;

    protected CommandLine(CommandSender sender) {
        this.sender = sender;
    }

    void addCmd(AbstractCmd cmd, String strCmd) {
        this.cmdList.add(cmd);
        this.strCmdList.add(strCmd);
    }

    public String getString(String varName) {
        for (int i = 0; i < cmdList.size(); i++) {
            if (cmdList.get(i).getName().equalsIgnoreCase(varName)) return strCmdList.get(i);
        }
        return null;
    }

    public int getInt(String varName) {
        AbstractCmd cmd = getCmd(varName);
        if (cmd == null) return -1;
        if (cmd instanceof CmdIntVar) return -1;
        return Integer.parseInt(strCmdList.get(this.cmdList.indexOf(cmd)));
    }

    private AbstractCmd getCmd(String varName) {
        for (AbstractCmd cmd : cmdList) {
            if (cmd.getName().equalsIgnoreCase(varName)) return cmd;
        }
        return null;
    }

    public int size() {
        return cmdList.size();
    }

    public Player getPlayer() {
        if (sender instanceof Player) return (Player) sender;
        return null;
    }

    public CommandSender getSender() {
        return sender;
    }

    @Override
    public Iterator<AbstractCmd> iterator() {
        return cmdList.iterator();
    }
}

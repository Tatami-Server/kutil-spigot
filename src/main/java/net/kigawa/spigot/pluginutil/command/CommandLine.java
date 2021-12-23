package net.kigawa.spigot.pluginutil.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CommandLine implements Iterable<String> {
    private final List<String> commandLine = new ArrayList<>();
    private final CommandSender sender;
    private final Command command;
    private final CommandVars commandVars = new CommandVars();

    protected CommandLine(CommandSender sender, Command command, String label, String[] strings) {
        commandLine.add(label);
        Collections.addAll(commandLine, strings);
        this.sender = sender;
        this.command = command;
    }

    public int getInt(String key) {
        return commandVars.getInt(key);
    }

    public String getString(String key) {
        return commandVars.getString(key);
    }

    public boolean getBoolean(String key) {
        return commandVars.getBoolean(key);
    }

    public CommandVars getCommandVars() {
        return commandVars;
    }

    public List<String> getCommandLine() {
        return new ArrayList<>(commandLine);
    }

    public CommandSender getSender() {
        return sender;
    }

    public Command getCommand() {
        return command;
    }

    @Override
    public Iterator<String> iterator() {
        return commandLine.iterator();
    }
}

package net.kigawa.spigot.pluginutil.command;

import org.bukkit.command.CommandSender;

public interface CommandParent {
    void addCommand(Command command);

    StringBuffer getPermission(CommandSender sender);

    String getName();

    int getWordNumber();

    public CommandParent getCommandParent();
}

package net.kigawa.spigot.pluginutil.command;

import net.kigawa.spigot.pluginutil.PluginBase;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class Command extends TabList implements CommandParent {
    private PluginBase plugin;
    private List<Command> subCommands;
    private final CommandParent commandParent;


    public Command(PluginBase pluginBase, CommandParent commandParent) {
        super(pluginBase);
        plugin = pluginBase;
        this.commandParent = commandParent;

        subCommands = new ArrayList<>();
    }

    public abstract String onThisCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings);

    public abstract String errorMessage();

    public abstract boolean isDefault();

    public CommandParent getCommandParent() {
        return commandParent;
    }

    public String createErrormessage() {
        StringBuffer stringBuffer = new StringBuffer();
        CommandParent command = this;
        while (command != null) {
            stringBuffer.insert(0, " ");
            stringBuffer.insert(0, command.getName());
            command = command.getCommandParent();
        }
        stringBuffer.insert(0, "/");
        String error = errorMessage();
        if (error == null) {
            error = "<subcommand>";
        }
        stringBuffer.append(error);
        return stringBuffer.toString();
    }

    public String onSubcommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        plugin.logger(getName() + " onAlways");

        if (subCommands != null) {
            if (strings.length > getWordNumber()) {
                if (subCommands.contains(new EqualsCommand(strings[getWordNumber()]))) {
                    Command subCommand = subCommands.get(subCommands.indexOf(new EqualsCommand(strings[getWordNumber()])));
                    return subCommand.onSubcommand(commandSender, command, s, strings);
                }
            }
        }
        //check permission
        if (checkPermission(commandSender)) {
            plugin.logger(getName() + " onNotFoundSubcommand");
            String message = onThisCommand(commandSender, command, s, strings);
            if (message == null) {
                return createErrormessage();
            }
            return message;
        } else {
            return "need permission";
        }
    }

    @Override
    public void addCommand(Command subCommand) {
        subCommands.add(subCommand);
        addTabLists(subCommand);
    }

    public boolean checkPermission(CommandSender sender) {
        StringBuffer sb = getPermission(sender);
        return sb == null;
    }

    public StringBuffer getPermission(CommandSender sender) {
        plugin.logger("get permission...");
        StringBuffer sb = commandParent.getPermission(sender);
        if (sb == null) return null;
        sb.append(".").append(getName());
        if (sender.hasPermission(sb.toString()) | sender.hasPermission(sb + ".*")) return null;
        return sb;
    }

    public int getWordNumber() {
        if (commandParent == null) {
            return 0;
        }
        return commandParent.getWordNumber() + 1;
    }

    public PluginBase getPlugin() {
        return plugin;
    }

    public List<Command> getSubCommands() {
        return subCommands;
    }


}

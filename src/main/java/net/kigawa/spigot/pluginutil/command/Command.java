package net.kigawa.spigot.pluginutil.command;

import net.kigawa.spigot.pluginutil.PluginBase;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class Command extends TabList implements CommandParent {
    private PluginBase plugin;
    private List<Command> subCommands;
    private List<String> permissions;
    private Command parentCommand;


    public Command(PluginBase pluginBase, CommandParent commandParent) {
        super(pluginBase);
        plugin = pluginBase;

        commandParent.addCommand(this);
        subCommands = new ArrayList<>();
    }

    public abstract String onThisCommand(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings);

    public abstract String errorMessage();

    public abstract boolean isDefault();

    public String createErrormessage() {
        StringBuffer stringBuffer = new StringBuffer();
        Command command = this;
        while (command != null) {
            stringBuffer.insert(0, " ");
            stringBuffer.insert(0, command.getName());
            command = this.parentCommand;
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

    public void addSubcommands(Command subCommand) {
        subCommand.setParentCommand(this);
        subCommands.add(subCommand);
        addTabLists(subCommand);
    }

    public boolean checkPermission(CommandSender sender) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(permissions.get(0));
        for (int i = 1; i < permissions.size(); i++) {
            stringBuilder.append(".").append(permissions.get(i));
        }
        return sender.hasPermission(stringBuilder.toString()) | sender.hasPermission(stringBuilder + ".*") | isDefault();
    }

    public void setParentCommand(Command parentCommand) {
        this.parentCommand = parentCommand;
    }

    public void setPermission(List<String> permission) {
        List<String> permission1 = new ArrayList<>(permission);
        permission1.add(getName());
        List<Command> commands = getSubCommands();
        if (commands != null) {
            for (Command command : commands) {
                command.setPermission(permission1);
            }
        }
        this.permissions = permission1;
    }

    public int getWordNumber() {
        if (parentCommand == null) {
            return 0;
        }
        return parentCommand.getWordNumber() + 1;
    }

    public PluginBase getPlugin() {
        return plugin;
    }

    public List<Command> getSubCommands() {
        return subCommands;
    }


}

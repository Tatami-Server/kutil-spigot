package net.kigawa.spigot.pluginutil.command;

import net.kigawa.spigot.pluginutil.PluginBase;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.List;

public abstract class FirstCommand extends Command implements CommandExecutor, TabCompleter {
    PluginBase plugin;

    public FirstCommand(PluginBase plugin, CommandParent commandParent) {
        super(plugin, commandParent);
        this.plugin = plugin;

        plugin.addCommand(this);

        PluginCommand command = plugin.getCommand(getName());
        assert command != null;
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        return tabComplete(commandSender, command, s, strings);
    }

    @Override
    public void addCommand(Command subCommand) {
        getSubCommands().add(subCommand);
        addTabLists(subCommand);
    }

    @Override
    public boolean onCommand(
            CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        plugin.logger(getName() + " onAlways");
        if (strings.length > 0) {

            plugin.logger(getName() + " onIsContain");

            if (getSubCommands() != null) {
                if (getSubCommands().contains(new EqualsCommand(strings[0]))) {
                    plugin.logger(getName() + " onGetSubCommand");
                    Command subCommand = getSubCommands().get(getSubCommands().indexOf(new EqualsCommand(strings[0])));
                    commandSender.sendMessage(subCommand.onSubcommand(commandSender, command, s, strings));
                    return true;
                }
            }
        }
        //check permission
        if (checkPermission(commandSender)) {
            plugin.logger(getName() + " onNotFound");
            commandSender.sendMessage(onThisCommand(commandSender, command, s, strings));
            return true;
        } else {
            commandSender.sendMessage("need permission");
            return true;
        }
    }


    @Override
    public int getWordNumber() {
        return 0;
    }
}

package net.kigawa.spigot.pluginutil.game.command;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.spigot.pluginutil.command.CommandParent;
import net.kigawa.spigot.scotlandplugin.game.GameManagerBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CreateCommands<M extends GameManagerBase> extends GameCommandBase {
    public CreateCommands(PluginBase pluginBase, M manager, CommandParent commandParent) {
        super(pluginBase, manager, commandParent);
        addCommand(new Create(pluginBase, manager, this));
        addCommand(new Remove(pluginBase, manager, this));
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String onThisCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    @Override
    public String errorMessage() {
        return "/bordgame scotland create <subcommand>";
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public List<String> getTabStrings(CommandSender sender, Command command, String label, String[] strings) {
        return null;
    }
}

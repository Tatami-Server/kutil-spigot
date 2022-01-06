package net.kigawa.spigot.pluginutil.game.command;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.spigot.pluginutil.PluginUtil;
import net.kigawa.spigot.pluginutil.command.CommandParent;
import net.kigawa.spigot.scotlandplugin.game.GameManagerBase;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Create<M extends GameManagerBase> extends GameCommandBase<M> {
    public Create(PluginBase pluginBase, M manager, CommandParent commandParent) {
        super(pluginBase, manager, commandParent);
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String onThisCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        getPlugin().logger("on create command");
        World world = PluginUtil.getWorld(commandSender);
        if (strings.length == getWordNumber() + 1 && world != null) {
            return getManager().create(strings[getWordNumber()], world.getName());
        }
        return null;
    }

    @Override
    public String errorMessage() {
        return "/scotland create create <name>";
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

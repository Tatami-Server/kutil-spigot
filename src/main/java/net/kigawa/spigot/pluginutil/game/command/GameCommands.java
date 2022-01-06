package net.kigawa.spigot.pluginutil.game.command;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.spigot.pluginutil.command.CommandParent;
import net.kigawa.spigot.pluginutil.game.GameManagerBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GameCommands<M extends GameManagerBase> extends GameCommandBase<M> {
    public GameCommands(PluginBase plugin, M gameManagerBase, CommandParent commandParent) {
        super(plugin, gameManagerBase, commandParent);
        addCommand(new GameStart(plugin, gameManagerBase, this));
        addCommand(new GameEnd(plugin, gameManagerBase, this));
    }

    @Override
    public String getName() {
        return "game";
    }

    @Override
    public String onThisCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }

    @Override
    public String errorMessage() {
        return null;
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

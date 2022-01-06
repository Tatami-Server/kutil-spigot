package net.kigawa.spigot.pluginutil.game.command;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.spigot.pluginutil.command.CommandParent;
import net.kigawa.spigot.scotlandplugin.game.GameManagerBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GameStart extends GameCommandBase {
    public GameStart(PluginBase kigawaPlugin, GameManagerBase gameManagerBase, CommandParent commandParent) {
        super(kigawaPlugin, gameManagerBase, commandParent);
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String onThisCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == getWordNumber() + 1) {
            return getManager().start(strings[getWordNumber()]);
        }
        return null;
    }

    @Override
    public String errorMessage() {
        return "<name>";
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

package net.kigawa.spigot.pluginutil.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class CmdPlayer extends AbstractCmd {
    List<String> list = new LinkedList<>();

    public CmdPlayer(String name, Function<CommandLine, String> function, AbstractCmd... subcommands) {
        super(name, function, subcommands);
    }

    @Override
    protected boolean matchCommand(String command) {
        return true;
    }

    @Override
    protected List<String> completeVar() {
        list.clear();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            list.add(player.getName());
        }
        return list;
    }
}

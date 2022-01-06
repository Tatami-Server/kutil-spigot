package net.kigawa.spigot.pluginutil.game.command;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.spigot.pluginutil.command.CommandParent;
import net.kigawa.spigot.pluginutil.command.Subcommand;
import net.kigawa.spigot.pluginutil.game.GameManagerBase;

public abstract class GameCommandBase<M extends GameManagerBase> extends Subcommand {
    private final M manager;

    public GameCommandBase(PluginBase kigawaPlugin, M gameManagerBase, CommandParent commandParent) {
        super(kigawaPlugin, commandParent);
        manager = gameManagerBase;
    }

    public M getManager() {
        return manager;
    }
}

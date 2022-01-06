package net.kigawa.spigot.pluginutil.game;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.spigot.pluginutil.command.CommandParent;
import net.kigawa.spigot.pluginutil.message.sender.ErrorSender;
import net.kigawa.spigot.scotlandplugin.game.command.CreateCommands;
import net.kigawa.spigot.scotlandplugin.game.command.GameCommandBase;
import net.kigawa.spigot.scotlandplugin.game.command.GameCommands;
import net.kigawa.util.HasEnd;
import net.kigawa.util.Util;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public abstract class GameManagerBase<D extends GameDataBase, G extends GameBase> implements HasEnd, Listener {
    private final List<D> dataList;
    private final List<G> gameList = new ArrayList<>();
    private final PluginBase plugin;
    private final CommandParent command;
    private final CreateCommands createCommands;
    private final GameCommands gameCommands;

    public GameManagerBase(PluginBase plugin, CommandParent command) {
        dataList = plugin.getRecorder().loadAll(getDataClass(), getName());
        this.plugin = plugin;
        this.command = command;

        createCommands = new CreateCommands(plugin, this,command);
        gameCommands = new GameCommands(plugin, this,command);
        command.addCommand(createCommands);
        command.addCommand(gameCommands);

        plugin.addHasEnd(this);
        plugin.registerEvents(this);
    }

    public abstract String getName();

    public abstract D newData();

    public abstract G newGame(D data);

    public abstract Class<D> getDataClass();

    public abstract Class<G> getGameClass();

    public GameCommands getGameCommands() {
        return gameCommands;
    }

    public void execGame(Util.Process<G> process) {
        Util.executeIterable(getGameList(), process);
    }

    public void save(D data) {
        plugin.getRecorder().save(data, getName());
    }

    public void addGameCommand(GameCommandBase subcommand) {
        gameCommands.addCommand(subcommand);
    }

    public void addCreateCommand(GameCommandBase subcommand) {
        createCommands.addCommand(subcommand);
    }

    public void end() {
        for (G game : new ArrayList<>(gameList)) {
            if (game == null) continue;
            game.end();
        }
    }

    public String end(String name) {
        G game = getGame(name);
        if (game == null) return "game is not started";
        String end = game.end();
        return end;
    }

    public void removeGame(G game) {
        gameList.remove(game);
    }

    public String remove(String name) {
        D data = getData(name);
        if (data == null) return name + " is not exit";
        for (G game : gameList) {
            if (game.contain(data)) {
                game.end();
                break;
            }
        }
        dataList.remove(data);
        plugin.getRecorder().remove(data, getName());
        return name + "is removed";
    }

    public String create(String name, String world) {
        plugin.logger("create game...");
        D data = getData(name);
        if (data != null) return name + " is exit";
        data = newData();
        data.setName(name);
        data.setWorld(world);
        getDataList().add(data);
        save(data);
        return name + " is created";
    }

    public String start(String name) {
        G game = getGame(name);
        if (game != null) return ErrorSender.getString(name + "is already started");

        D data = getData(name);
        if (data == null) return ErrorSender.getString(name + "is not exit");

        game = newGame(data);
        gameList.add(game);
        return game.start();
    }

    public D getData(String name) {
        for (D data : dataList) {
            if (data.getName() == null) continue;
            if (data.getName().equals(name)) {
                return data;
            }
        }
        return null;
    }

    public G getGame(String name) {
        for (G gameBase : gameList) {
            if (gameBase.equals(name)) {
                return gameBase;
            }
        }
        return null;
    }

    public List<G> getGameList() {
        return gameList;
    }

    public List<D> getDataList() {
        return dataList;
    }

    public PluginBase getPlugin() {
        return plugin;
    }
}

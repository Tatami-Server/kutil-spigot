package net.kigawa.spigot.pluginutil.tool;

import net.kigawa.spigot.pluginutil.PluginBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;

public class ToolManager implements Listener {
    private final Set<Tool> toolSet = new HashSet<>();

    public ToolManager(PluginBase pluginBase) {
        pluginBase.registerEvents(this);
    }

    public void addTool(Tool tool) {
        toolSet.add(tool);
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        for (Tool tool : toolSet) tool.event(event);
    }
}
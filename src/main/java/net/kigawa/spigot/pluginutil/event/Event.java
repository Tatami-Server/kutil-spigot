package net.kigawa.spigot.pluginutil.event;

import net.kigawa.spigot.pluginutil.PluginBase;
import org.bukkit.event.Listener;

public abstract class Event implements Listener {
    PluginBase plugin;

    public Event(PluginBase pluginBase) {
        plugin = pluginBase;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}

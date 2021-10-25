package net.kigawa.spigot.pluginutil.event;

import net.kigawa.spigot.pluginutil.PluginBase;
import org.bukkit.event.Listener;

public class Event implements Listener {
    PluginBase plugin;

    public Event(PluginBase kigawaPlugin) {
        plugin = kigawaPlugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}

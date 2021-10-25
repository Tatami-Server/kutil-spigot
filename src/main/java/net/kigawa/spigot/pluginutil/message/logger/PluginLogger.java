package net.kigawa.spigot.pluginutil.message.logger;

import net.kigawa.util.Logger;
import net.kigawa.spigot.pluginutil.PluginBase;

public abstract class PluginLogger implements Logger {
    PluginBase plugin;

    public PluginLogger(PluginBase kigawaPlugin) {
        plugin = kigawaPlugin;
    }

    @Override
    public void logger(String message) {
        plugin.logger(message);
    }
}

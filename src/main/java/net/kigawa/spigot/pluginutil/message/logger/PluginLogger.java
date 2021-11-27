package net.kigawa.spigot.pluginutil.message.logger;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.util.InterfaceLogger;

/**
 * @deprecated
 */
public abstract class PluginLogger implements InterfaceLogger {
    PluginBase plugin;

    public PluginLogger(PluginBase kigawaPlugin) {
        plugin = kigawaPlugin;
    }

    @Override
    public void logger(String message) {
        plugin.logger(message);
    }

    @Override
    public void info(Object o) {
        plugin.info(o);
    }

    @Override
    public void warning(Object o) {
        plugin.warning(o);
    }

    @Override
    public void title(Object o) {
        plugin.title(o);
    }

    @Override
    public void debug(Object o) {
        plugin.debug(o);
    }
}

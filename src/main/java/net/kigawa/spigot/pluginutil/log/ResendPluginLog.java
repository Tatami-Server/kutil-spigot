package net.kigawa.spigot.pluginutil.log;

import net.kigawa.log.Formatter;
import net.kigawa.log.ResendLog;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class ResendPluginLog extends ResendLog {
    public ResendPluginLog(Logger logger, Level level) {
        super(logger, level);
        setFormatter(new Formatter());
        setLevel(Level.ALL);
    }

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel().intValue() < Level.INFO.intValue()) {
            super.publish(record);
        }
    }
}
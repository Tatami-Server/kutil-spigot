package net.kigawa.spigot.pluginutil.command;

import java.util.HashMap;
import java.util.Map;

public class CommandVars {
    private Map<String, Integer> integerMap = new HashMap<>();
    private Map<String, String> stringMap = new HashMap<>();
    private Map<String, Boolean> booleanMap = new HashMap<>();

    protected CommandVars() {
    }

    public int getInt(String key) {
        return integerMap.get(key);
    }

    public String getString(String key) {
        return stringMap.get(key);
    }

    public boolean getBoolean(String key) {
        return booleanMap.get(key);
    }

    protected void addInt(String key, int value) {
        integerMap.put(key, value);
    }

    protected void addString(String key, String value) {
        stringMap.put(key, value);
    }

    protected void addBoolean(String key, boolean value) {
        booleanMap.put(key, value);
    }

}

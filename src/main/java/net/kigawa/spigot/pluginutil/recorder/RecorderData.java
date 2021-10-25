package net.kigawa.spigot.pluginutil.recorder;

import net.kigawa.yamlutil.YamlData;

public class RecorderData extends YamlData {
    String name;

    public RecorderData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

package net.kigawa.spigot.pluginutil.game;

import net.kigawa.spigot.pluginutil.recorder.RecorderData;

public abstract class GameDataBase extends RecorderData {
    String world;

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }
}

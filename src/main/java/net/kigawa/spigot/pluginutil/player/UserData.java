package net.kigawa.spigot.pluginutil.player;

import net.kigawa.yamlutil.YamlData;
import org.bukkit.Location;

public class UserData extends YamlData {
    String[] showUUID;
    String[] hideUUID;
    double x;
    double y;
    double z;
    float yaw;
    float pith;
    String world;

    public UserData(String name, double x, double y, double z, float yaw, float pith, String world) {
        setName(name);
        setX(x);
        setY(y);
        setZ(z);
        setYaw(yaw);
        setPith(pith);
        setWorld(world);
    }

    public UserData() {
    }

    public boolean teleport(Location l) {
        setX(l.getX());
        setY(l.getY());
        setZ(l.getZ());
        setYaw(l.getYaw());
        setPith(l.getPitch());
        setWorld(l.getWorld().getName());
        return true;
    }

    public String[] getShowUUID() {
        return showUUID;
    }

    public void setShowUUID(String[] showUUID) {
        this.showUUID = showUUID;
    }

    public String[] getHideUUID() {
        return hideUUID;
    }

    public void setHideUUID(String[] hideUUID) {
        this.hideUUID = hideUUID;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public float getPith() {
        return pith;
    }

    public void setPith(float pith) {
        this.pith = pith;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}

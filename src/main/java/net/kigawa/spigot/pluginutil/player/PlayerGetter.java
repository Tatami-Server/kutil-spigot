package net.kigawa.spigot.pluginutil.player;

import net.kigawa.spigot.pluginutil.PluginBase;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerGetter {
    PluginBase plugin;

    public PlayerGetter(PluginBase kigawaPlugin) {
        plugin = kigawaPlugin;
    }

    public List<Player> room(String world, int x, int y, int z, int x1, int y1, int z1) {
        int sx;
        int bx;
        int sy;
        int by;
        int sz;
        int bz;
        if (x > x1) {
            sx = x1;
            bx = x;
        } else {
            sx = x;
            bx = x1;
        }
        if (y > y1) {
            sy = y1;
            by = y;
        } else {
            sy = y;
            by = y1;
        }
        if (z > z1) {
            sz = z1;
            bz = z;
        } else {
            sz = z;
            bz = z1;
        }
        if (sx < 0) sx--;
        if (sy < 0) sy--;
        if (sz < 0) sz--;
        if (bx > 0) bx++;
        if (by > 0) by++;
        if (bz > 0) bz++;
        World world1 = plugin.getServer().getWorld(world);
        Location location;
        List<Player> allPlayers = world1.getPlayers();
        plugin.logger("all player " + allPlayers.size());
        plugin.logger("sx " + sx);
        plugin.logger("bx " + bx);
        List<Player> inRoom = new ArrayList<>();
        Player player;
        boolean isIn;
        for (int i = 0; i < allPlayers.size(); i++) {
            isIn = false;
            player = allPlayers.get(i);
            plugin.logger(player.getName());
            location = player.getLocation();
            plugin.logger("x" + location.getX());
            plugin.logger(String.valueOf(location.getX() >= sx) + (location.getY() >= sy) + (location.getZ() >= sz));
            if (location.getX() >= sx && location.getY() >= sy && location.getZ() >= sz) {
                plugin.logger("check big");
                isIn = location.getX() <= bx && location.getY() <= by && location.getZ() <= bz;
            }
            if (isIn) {
                inRoom.add(player);
            }
        }
        plugin.logger("in room " + inRoom.size());
        return inRoom;
    }
}

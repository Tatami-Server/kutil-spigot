package net.kigawa.spigot.pluginutil.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class Teleporter {
    public void teleportPlayers(List<Player> players, Location location){
        for (Player player:players){
            player.teleport(location);
        }
    }
}


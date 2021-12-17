package net.kigawa.spigot.pluginutil.inventory;

import net.kigawa.spigot.pluginutil.player.User;
import org.bukkit.inventory.PlayerInventory;

public class PlayerStorage<U extends User<U>> extends Storage<PlayerInventory> {
    private final U user;

    public PlayerStorage(U user, String name, StorageManager storageManager) {
        super(user.getPlayer().getInventory(), name, storageManager);
        this.user = user;
    }
}
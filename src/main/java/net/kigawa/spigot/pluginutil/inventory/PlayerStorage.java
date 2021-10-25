package net.kigawa.spigot.pluginutil.inventory;

import net.kigawa.spigot.pluginutil.player.User;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

public abstract class PlayerStorage extends Storage<PlayerInventory> {

    public PlayerStorage(Player player, String name, StorageManager storageManager) {
        super(player.getInventory(), name, storageManager);
        storageManager.addPlayerStorage(this);
    }

    public PlayerStorage(User user, String name, StorageManager storageManager) {
        this(user.getPlayer(), name, storageManager);
    }

    public abstract void inventoryClickEvent(InventoryClickEvent event);

    public abstract void playerDropItemEvent(PlayerDropItemEvent event);

    public abstract void playerInteractEvent(PlayerInteractEvent event);
}

package net.kigawa.spigot.pluginutil.inventory;

import net.kigawa.spigot.pluginutil.player.User;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class PlayerStorage<U extends User<U>> extends Storage<PlayerInventory> {
    private final List<BiConsumer<U, InventoryClickEvent>> inventoryClickEvents = new ArrayList<>();
    private final List<BiConsumer<U, PlayerDropItemEvent>> playerDropItemEvents = new ArrayList<>();
    private final List<BiConsumer<U, PlayerInteractEvent>> playerInteractEvents = new ArrayList<>();
    private final U user;

    public PlayerStorage(U user, String name, StorageManager storageManager) {
        super(user.getPlayer().getInventory(), name, storageManager);
        this.user = user;
    }

    public void removePlayerInteractEvent(BiConsumer<U, PlayerInteractEvent> biConsumer) {
        playerInteractEvents.remove(biConsumer);
    }

    public void removePlayerInteractEvent(Predicate<BiConsumer<U, PlayerInteractEvent>> predicate) {
        playerInteractEvents.removeIf(predicate);
    }

    public void removePlayerDropEvent(BiConsumer<U, PlayerDropItemEvent> biConsumer) {
        playerDropItemEvents.remove(biConsumer);
    }

    public void removePlayerDropEvent(Predicate<BiConsumer<U, PlayerDropItemEvent>> predicate) {
        playerDropItemEvents.removeIf(predicate);
    }

    public void removeInventoryClickEvent(BiConsumer<U, InventoryClickEvent> biConsumer) {
        inventoryClickEvents.remove(biConsumer);
    }

    public void removeInventoryClickEvent(Predicate<BiConsumer<U, InventoryClickEvent>> predicate) {
        inventoryClickEvents.removeIf(predicate);
    }

    public void addInventoryClickEvent(BiConsumer<U, InventoryClickEvent> inventoryClickEvent) {
        inventoryClickEvents.add(inventoryClickEvent);
    }

    public void addPlayerDropEvent(BiConsumer<U, PlayerDropItemEvent> playerDropItemEvent) {
        playerDropItemEvents.add(playerDropItemEvent);
    }

    public void addPlayerInteractEvent(BiConsumer<U, PlayerInteractEvent> playerInteractEvent) {
        playerInteractEvents.add(playerInteractEvent);
    }

    public void inventoryClickEvent(InventoryClickEvent event) {
        for (BiConsumer<U, InventoryClickEvent> biConsumer : inventoryClickEvents) {
            biConsumer.accept(user, event);
        }
    }

    public void playerDropItemEvent(PlayerDropItemEvent event) {
        for (BiConsumer<U, PlayerDropItemEvent> biConsumer : playerDropItemEvents) {
            biConsumer.accept(user, event);
        }
    }

    public void playerInteractEvent(PlayerInteractEvent event) {
        for (BiConsumer<U, PlayerInteractEvent> biConsumer : playerInteractEvents) {
            biConsumer.accept(user, event);
        }
    }
}

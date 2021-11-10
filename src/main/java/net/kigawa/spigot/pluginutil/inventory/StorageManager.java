package net.kigawa.spigot.pluginutil.inventory;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.spigot.pluginutil.player.User;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class StorageManager implements Listener {
    private final List<Storage> storageList = new ArrayList<>();
    private final List<Menu> menuList = new ArrayList<>();
    private final List<PlayerStorage> playerStorageList = new ArrayList<>();
    private final PluginBase plugin;
    private final Server server;

    public StorageManager(PluginBase plugin) {
        this.plugin = plugin;
        server = plugin.getServer();

        plugin.registerEvents(this);
    }

    public void removeMenu(User user, String name) {
        Menu menu = getMenu(user, name);
        if (menu == null) return;
        menu.close();
        menuList.remove(menu);
    }

    public Menu getMenu(User user, String name) {
        for (Menu menu : menuList) {
            if (menu.equals(user) && menu.equals(name)) {
                return menu;
            }
        }
        return null;
    }

    public void removeMenu() {
        for (Menu menu:menuList){
            menu.close();
        }
        menuList.clear();
    }

    public void removeAllMenu(List<Menu> menus) {
        storageList.removeAll(menus);
        menuList.removeAll(menus);
    }

    public void addPlayerStorage(PlayerStorage playerStorage) {
        if (playerStorageList.contains(playerStorage)) return;
        playerStorageList.add(playerStorage);
    }

    public void addStorage(Storage storage) {
        if (storageList.contains(storage)) {
            return;
        }
        storageList.add(storage);
    }

    public void addMenu(Menu menu) {
        if (menuList.contains(menu)) {
            return;
        }
        menuList.add(menu);
    }

    public void removeMenu(Menu menu) {
        menuList.remove(menu);
        storageList.remove(menu);
    }

    public void removeStorage(Storage storage) {
        storageList.remove(storage);
        if (storage instanceof Menu) menuList.remove(storage);
        if (storage instanceof PlayerStorage) playerStorageList.remove(storage);
    }

    public List<Storage> getStorageGroup(String group) {
        List<Storage> storages = new ArrayList<>();
        for (Storage storage : storageList) {
            if (storage.containGroup(group)) {
                storages.add(storage);
            }
        }
        return storages;
    }

    public List<Menu> getMenuGroup(String group) {
        List<Menu> menus = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.containGroup(group)) {
                menus.add(menu);
            }
        }
        return menus;
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event) {
        plugin.logger("inventoryClickEvent(InventoryClickEvent event)");
        for (Menu menu : new ArrayList<>(menuList)) {
            menu.onClick(event);
        }
        for (PlayerStorage playerStorage : playerStorageList) {
            playerStorage.inventoryClickEvent(event);
        }
    }

    @EventHandler
    public void playerDropItemEvent(PlayerDropItemEvent event) {
        for (PlayerStorage storage : playerStorageList) {
            storage.playerDropItemEvent(event);
        }
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        for (PlayerStorage storage : playerStorageList) {
            storage.playerInteractEvent(event);
        }
    }
}

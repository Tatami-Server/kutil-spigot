package net.kigawa.spigot.pluginutil.inventory;

import com.google.common.collect.Lists;
import net.kigawa.spigot.pluginutil.player.User;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Storage<T extends Inventory> {
    private final T inventory;
    private final String name;
    private final List<String> group = new ArrayList<>();
    private final StorageManager storageManager;
    private int size;
    private int workIndex;
    private ItemStack workItemStack;
    private ItemMeta workItemMeta;
    private Material workMaterial;

    public Storage(Server server, InventoryHolder inventoryHolder, InventoryType inventoryType, String name, StorageManager storageManager) {
        this((T) server.createInventory(inventoryHolder, inventoryType), name, storageManager);
    }

    public Storage(Server server, InventoryHolder inventoryHolder, InventoryType inventoryType, String title, String name, StorageManager storageManager) {
        this((T) server.createInventory(inventoryHolder, inventoryType, title), name, storageManager);
    }

    public Storage(Server server, InventoryHolder inventoryHolder, int line, String name, StorageManager storageManager) {
        this((T) server.createInventory(inventoryHolder, line * 9), name, storageManager);
    }

    public Storage(Server server, InventoryHolder inventoryHolder, int line, String title, String name, StorageManager storageManager) {
        this((T) server.createInventory(inventoryHolder, line * 9, title), name, storageManager);
    }

    public Storage(T inventory, String name, StorageManager storageManager) {
        this.inventory = inventory;
        size = inventory.getSize();
        this.name = name;
        this.storageManager = storageManager;
        storageManager.addStorage(this);
    }

    public void close() {
        for (HumanEntity entity : new ArrayList<>(inventory.getViewers())) {
            entity.closeInventory();
        }
    }

    public boolean equals(String name) {
        return this.name.equals(name);
    }

    public void clear() {
        inventory.clear();
    }

    public void addGroup(String name) {
        if (group.contains(name)) return;
        group.add(name);
    }

    public void setDescription(String[] descriptions) {
        List<String> lore = new ArrayList<>();
        Lists.asList(lore, descriptions);
        workItemMeta.setLore(lore);
    }

    public void setDisplayName(String name) {
        workItemMeta.setDisplayName(name);
    }

    public void setType(Material material) {
        workItemStack.setType(material);
    }

    public void setType() {
        setType(workMaterial);
    }

    public void setItemMeta(int index, ItemMeta itemMeta) {
        workItemStack.setItemMeta(itemMeta);
        setItemStack(index, workItemStack);
    }

    public void setItemMeta() {
        setItemMeta(workItemMeta);
    }

    public void setItemStack(int index) {
        setItemStack(index, workItemStack);
    }

    public void setItemStack(ItemStack itemStack) {
        setItemStack(workIndex, itemStack);
    }

    public void setItemStack() {
        setItemStack(workItemStack);
    }

    public boolean containGroup(String group) {
        return this.group.contains(group);
    }

    public boolean setItemStack(int index, ItemStack itemStack) {
        if (checkIndex(index)) {
            inventory.setItem(index, itemStack);
            return true;
        }
        return false;
    }

    public boolean setDescription(int index, String[] descriptions) {
        if (checkIndex(index)) {
            getItemMeta(index);
            setDescription(descriptions);
            setItemMeta();
            setItemStack();
            return true;
        }
        return false;
    }

    public boolean setDisplayName(int index, String name) {
        if (checkIndex(index)) {
            getItemMeta(index);
            setDisplayName(name);
            setItemMeta();
            setItemStack();
            return true;
        }
        return false;
    }

    public boolean checkIndex(int index) {
        return index <= size && index >= 0;
    }

    public boolean setType(int index, Material material) {
        workMaterial = material;
        if (checkIndex(index)) {
            getItemStack(index);
            setType(material);
            setItemStack();
            return true;
        }
        return false;
    }

    public boolean fillType(int minIndex, int maxIndex, Material material) {
        for (int i = minIndex; i <= maxIndex; i++) {
            if (setType(i, material)) {
                return false;
            }
        }
        return true;
    }

    public boolean setAmount(int index, int amount) {
        if (checkIndex(index)) {
            getItemStack(index);
            workItemStack.setAmount(amount);
            setItemStack();
            return true;
        }
        return false;
    }

    public boolean fill(int minIndex, int maxIndex) {
        return fill(minIndex, maxIndex, workItemStack.clone());
    }

    public boolean fill(int minIndex, int maxIndex, ItemStack itemStack) {
        for (int i = minIndex; i <= maxIndex; i++) {
            if (!setItemStack(i, itemStack.clone())) {
                return false;
            }
        }
        return true;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks) {
        return inventory.addItem(itemStacks);
    }

    public HashMap<Integer, ItemStack> addItem() {
        return inventory.addItem(workItemStack);
    }

    public InventoryView open(User user) {
        return open(user.getPlayer());
    }

    public InventoryView open(HumanEntity entity) {
        return entity.openInventory(inventory);
    }

    public ItemMeta getItemMeta(int index) {
        getItemStack(index);
        return getItemMeta();
    }

    public ItemMeta getItemMeta() {
        workItemMeta = workItemStack.getItemMeta();
        return workItemMeta;
    }

    public void setItemMeta(ItemMeta itemMeta) {
        workItemStack.setItemMeta(itemMeta);
    }

    public ItemStack newItemStack(Material material) {
        workItemStack = new ItemStack(material);
        return workItemStack;
    }

    public ItemStack getItemStack(int index) {
        this.workIndex = index;
        this.workItemStack = inventory.getItem(index);
        if (workItemStack == null) {
            workItemStack = new ItemStack(Material.AIR);
        }
        return workItemStack;
    }

    public T getInventory() {
        return inventory;
    }
}
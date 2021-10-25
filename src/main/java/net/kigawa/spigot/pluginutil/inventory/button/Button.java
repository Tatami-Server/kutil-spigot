package net.kigawa.spigot.pluginutil.inventory.button;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class Button {

    public abstract Material getType();

    public abstract String getName();

    public abstract String[] descriptions();

    public abstract void onClick(InventoryClickEvent event);

    public abstract void leftClick(InventoryClickEvent event);

    public abstract void rightClick(InventoryClickEvent event);

}

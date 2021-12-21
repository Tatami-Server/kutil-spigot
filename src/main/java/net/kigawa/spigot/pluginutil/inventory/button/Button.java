package net.kigawa.spigot.pluginutil.inventory.button;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

public class Button extends ButtonBase {
    private final Material material;
    private final String name;
    private final int number;
    private BiConsumer<InventoryClickEvent, Button> inventoryClick;
    private BiConsumer<InventoryClickEvent, Button> leftClick;
    private BiConsumer<InventoryClickEvent, Button> rightClick;

    public Button(Material material, String name, BiConsumer<InventoryClickEvent, Button> consumer) {
        this(material, name, consumer, -1);
    }

    public Button(Material material, String name, BiConsumer<InventoryClickEvent, Button> consumer, int number) {
        this.material = material;
        this.name = name;
        this.inventoryClick = consumer;
        this.number = number;
    }

    public void setRightClick(BiConsumer<InventoryClickEvent, Button> rightClick) {
        this.rightClick = rightClick;
    }

    public void setLeftClick(BiConsumer<InventoryClickEvent, Button> leftClick) {
        this.leftClick = leftClick;
    }

    public void setInventoryClick(BiConsumer<InventoryClickEvent, Button> inventoryClick) {
        this.inventoryClick = inventoryClick;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public Material getType() {
        return material;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] descriptions() {
        return new String[0];
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        inventoryClick.accept(event, this);
    }

    @Override
    public void leftClick(InventoryClickEvent event) {
        if (leftClick == null) return;
        leftClick.accept(event, this);
    }

    @Override
    public void rightClick(InventoryClickEvent event) {
        if (rightClick == null) return;
        rightClick.accept(event, this);
    }
}

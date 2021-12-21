package net.kigawa.spigot.pluginutil.tool;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Tool {
    private final ItemStack itemStack;
    private final ToolManager toolManager;
    private final Consumer<PlayerInteractEvent> consumer;
    private boolean click;
    private boolean interact;
    private boolean drop;

    public Tool(ItemStack itemStack, ToolManager toolManager, Consumer<PlayerInteractEvent> consumer) {
        this.itemStack = itemStack;
        this.toolManager = toolManager;
        this.consumer = consumer;

        toolManager.addTool(this);
    }

    public void event(PlayerInteractEvent event) {
        if (event.getItem().equals(itemStack)) consumer.accept(event);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Consumer<PlayerInteractEvent> getConsumer() {
        return consumer;
    }

    public void setLore(String... lore) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, lore);
        setLore(list);
    }

    public void setLore(List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

    public void setDisplayName(String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }
}
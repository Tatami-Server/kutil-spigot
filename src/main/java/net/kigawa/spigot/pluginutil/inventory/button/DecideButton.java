package net.kigawa.spigot.pluginutil.inventory.button;

import net.kigawa.spigot.pluginutil.player.User;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class DecideButton extends Button {
    private final User user;

    public DecideButton(User user) {
        this.user = user;
    }

    @Override
    public Material getType() {
        return Material.EMERALD;
    }

    @Override
    public String getName() {
        return "決定";
    }

    @Override
    public String[] descriptions() {
        return null;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        user.decide();
    }

    @Override
    public void leftClick(InventoryClickEvent event) {

    }

    @Override
    public void rightClick(InventoryClickEvent event) {

    }
}

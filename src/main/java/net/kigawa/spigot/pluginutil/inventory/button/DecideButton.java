package net.kigawa.spigot.pluginutil.inventory.button;

import net.kigawa.spigot.pluginutil.player.User;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class DecideButton extends Button {
    private final User user;

    /**
     * @deprecated
     */
    public DecideButton(User user) {
        super(null, user.getName(), null);
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
    public void leftClick(InventoryClickEvent event) {

    }

    @Override
    public void rightClick(InventoryClickEvent event) {

    }
}

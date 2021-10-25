package net.kigawa.spigot.pluginutil.inventory.button;

import net.kigawa.spigot.pluginutil.inventory.Menu;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class PreviousPage extends Button {
    private Menu menu;

    public PreviousPage(Menu menu) {
        this.menu = menu;
    }

    @Override
    public Material getType() {
        return Material.ARROW;
    }

    @Override
    public String getName() {
        return "前のページ";
    }

    @Override
    public String[] descriptions() {
        return new String[0];
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        menu.getPrevious().open(event.getWhoClicked());
    }

    @Override
    public void leftClick(InventoryClickEvent event) {

    }

    @Override
    public void rightClick(InventoryClickEvent event) {

    }
}

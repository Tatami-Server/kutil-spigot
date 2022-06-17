package net.kigawa.spigot.pluginutil.inventory;

import net.kigawa.spigot.pluginutil.inventory.button.ButtonBase;
import net.kigawa.spigot.pluginutil.inventory.button.NextPage;
import net.kigawa.spigot.pluginutil.inventory.button.PreviousPage;
import net.kigawa.spigot.pluginutil.player.User;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class Menu extends Storage {
    private final int page;
    private final User user;
    private Menu next;
    private Menu previous;
    private ButtonBase[] buttons;

    public Menu(Server server, User user, String title, String name, StorageManager storageManager) {
        this(server, user, title, 0, name, storageManager);
    }

    public Menu(Server server, User user, String title, int page, String name, StorageManager storageManager) {
        super(server, user.getPlayer(), 6, title, name, storageManager);
        this.page = page;
        this.user = user;
        setSize(getInventory().getSize());
        setup();

        getStorageManager().addMenu(this);
    }

    public boolean equals(User user) {
        return this.user.equals(user);
    }

    public void setDecideButton(ButtonBase button) {
        setButton(40, button);
    }

    public ButtonBase[] getButtons() {
        return buttons;
    }

    public void resetMenu() {
        clear();
        setup();
    }

    public void setup() {
        buttons = new ButtonBase[56];
        getItemStack(0);
        setType(Material.RAIL);
        getItemMeta();
        setDisplayName("");
        setItemMeta();
        setItemStack();

        setItemStack(0);
        setItemStack(9);
        setItemStack(18);
        setItemStack(27);
        setItemStack(36);
        setItemStack(45);

        setItemStack(8);
        setItemStack(17);
        setItemStack(26);
        setItemStack(35);
        setItemStack(44);
        setItemStack(53);

        newItemStack(Material.IRON_BARS);
        getItemMeta();
        setDisplayName("");
        setItemMeta();

        fill(1, 7);
        fill(46, 52);

        setNextPage(next);
        setPreviousPage(previous);
    }

    public void onClick(InventoryClickEvent event) {
        if (getInventory() == event.getClickedInventory() && 0 <= event.getSlot()) {
            ButtonBase button = buttons[event.getSlot()];
            event.setCancelled(true);
            if (button == null) {
                return;
            }

            button.onClick(event);
            if (event.isLeftClick()) {
                button.leftClick(event);
            }
            if (event.isRightClick()) {
                button.rightClick(event);
            }
        }
    }

    public void setNextPage(Menu menu) {
        if (next != null) next.setPrevious(null);
        setNext(menu);
        if (menu == null) {
            return;
        }
        menu.setPrevious(this);
    }

    public void setPreviousPage(Menu menu) {
        if (previous != null) previous.setNext(null);
        setPrevious(menu);
        if (menu == null) {
            return;
        }
        menu.setNext(this);
    }

    public void setButton(int index, ButtonBase button) {
        buttons[index] = button;
        getItemStack(index);
        if (button.getType() != null) {
            setType(button.getType());
        }
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            newItemStack(Material.STONE);
            getItemMeta();
        }
        if (button.getName() != null) {
            setDisplayName(button.getName());
        }
        if (button.descriptions() != null) {
            setDescription(button.descriptions());
        }
        setItemMeta();
        setItemStack();
        buttons[index] = button;
    }

    public void removeButton(int index) {
        buttons[index] = null;
    }

    public Menu getPrevious() {
        return previous;
    }

    private void setPrevious(Menu previous) {
        this.previous = previous;
        if (previous != null) {
            ButtonBase button = new PreviousPage(this);
            setButton(39, button);
        } else {
            removeButton(39);
            getItemStack(39);
            setType(Material.AIR);
            setItemStack();
        }
    }

    public Menu getNext() {
        return next;
    }

    private void setNext(Menu next) {
        this.next = next;
        if (next != null) {
            ButtonBase button = new NextPage(this);
            setButton(41, button);
        } else {
            removeButton(41);
            getItemStack(41);
            setType(Material.AIR);
            setItemStack();
        }
    }

    public int getPage() {
        return page;
    }
}

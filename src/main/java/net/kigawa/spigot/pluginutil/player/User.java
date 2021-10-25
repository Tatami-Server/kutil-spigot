package net.kigawa.spigot.pluginutil.player;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.spigot.pluginutil.inventory.Storage;
import net.kigawa.spigot.pluginutil.message.Messenger;
import net.kigawa.util.Util;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID uuid;
    private final UserManager manager;
    private Player player;
    private boolean isOnline;
    private List<String> groupList = new ArrayList<>();
    private String name;

    private User(OfflinePlayer player, UserManager userManager, boolean isOnline) {
        this.isOnline = isOnline;
        this.manager = userManager;
        assert manager != null;
        manager.addUser(this);
        uuid = player.getUniqueId();
        name = player.getName();
    }

    public User(Player player, UserManager manager) {
        this(player, manager, true);
        this.player = player;
    }

    public User(OfflinePlayer player, UserManager manager) {
        this(player, manager, false);
    }

    public static void saveUserData(Player player) {
        UserData data = UserManager.loadData(player.getUniqueId().toString());
        Location l = player.getLocation();
        if (data == null) {
            data = new UserData(player.getUniqueId().toString(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), l.getWorld().getName());
        }
        data.setX(l.getX());
        data.setY(l.getY());
        data.setZ(l.getZ());
        data.setYaw(l.getYaw());
        data.setPith(l.getPitch());
        data.setWorld(l.getWorld().getName());
        UserManager.getUserYaml().save(data);
    }

    public static void playerJoinEvent(PlayerJoinEvent event, PluginBase plugin) {
        UserData data = UserManager.loadData(event.getPlayer().getUniqueId().toString());
        if (data != null) {
            teleportOnJoin(plugin, data, event);
            hideOrShowOnJoin(plugin, data, event);
        }
    }

    public static void playerQuitEvent(PlayerQuitEvent event) {
        saveUserData(event.getPlayer());
    }

    public static void onDisable(PluginBase plugin) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            saveUserData(player);
        }
    }

    public static void hideOrShowOnJoin(PluginBase plugin, UserData data, PlayerJoinEvent event) {
        String[] hides = data.getHideUUID();
        String[] shows = data.getShowUUID();
        Player eventPlayer = event.getPlayer();
        Server server = plugin.getServer();
        List<String> uuidList = new ArrayList<>();

        for (Player player : server.getOnlinePlayers()) {
            uuidList.add(player.getUniqueId().toString());
        }
        if (hides != null) {
            for (String hide : hides) {
                if (uuidList.contains(hide)) {
                    eventPlayer.hidePlayer(plugin, server.getPlayer(UUID.fromString(hide)));
                }
            }
        }
        if (shows != null) {
            for (String show : shows) {
                if (uuidList.contains(show)) {
                    eventPlayer.showPlayer(plugin, server.getPlayer(UUID.fromString(show)));
                }
            }
        }
    }

    public static void teleportOnJoin(PluginBase plugin, UserData data, PlayerJoinEvent event) {
        Location location = new Location(plugin.getServer().getWorld(data.getWorld()), data.getX(), data.getY(), data.getZ(), data.getYaw(), data.getPith());
        event.getPlayer().teleport(location);
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean equals(User user) {
        return equals(user.getUuid());
    }

    public void decide() {
    }

    public boolean isJoinTeam(String name) {
        return getTeam().getName().equals(name);
    }

    public void sendToolBar(StringBuffer sb) {
        Messenger.sendMessage(player, new TextComponent(sb.toString()), ChatMessageType.ACTION_BAR);
    }

    public void leaveTeam() {
        Team team = manager.getEntryTeam(name);
        if (team == null) return;
        team.removeEntry(name);
    }

    public Team getTeam() {
        return manager.getEntryTeam(name);
    }

    public void setTeam(String teamStr) {
        if (teamStr == null) return;
        Team team = manager.getTeam(teamStr);
        team.addEntry(name);
    }

    public void sendMessage(String title, String message) {
        sendMessage(new StringBuffer(title).append(ChatColor.WHITE).append(": ").append(message));
    }

    public void sendTitleMessage(String title) {
        sendStr(title, ChatColor.GREEN);
    }

    public void sendMessage(String message) {
        sendMessage(new StringBuffer(message));
    }

    public void sendStr(String str, ChatColor chatColor) {
        sendStr(new StringBuffer(str), chatColor);
    }

    public void sendMessage(StringBuffer sb) {
        sendStr(sb, ChatColor.BLUE);
    }

    public void sendStr(StringBuffer sb, ChatColor chatColor) {
        if (isOnline) {
            player.sendMessage(sb.insert(0, ChatColor.BOLD).insert(0, chatColor).toString());
        }
    }

    public void clearInventory() {
        player.getInventory().clear();
    }

    public void closeInventory() {
        if (!isOnline) return;
        player.closeInventory();
    }

    public void leaveEvent(PlayerQuitEvent event) {
        isOnline = false;
    }

    public void addTag(String name) {
        if (groupList.contains(name)) return;
        groupList.add(name);
    }

    public void removeGroup(String name) {
        if (groupList.contains(name)) {
            groupList.remove(name);
        }
    }

    public void show(Plugin plugin, User user) {
        if (isOnline && user.isOnline) {
            player.showPlayer(plugin, user.getPlayer());
            return;
        }
        UserData data = UserManager.loadData(getUuidStr());
        if (data.getShowUUID() == null) data.setShowUUID(new String[0]);
        data.setShowUUID(Util.addStrSet(data.getShowUUID(), user.getUuidStr()));
        UserManager.saveData(data);
    }

    public void hide(Plugin plugin, User user) {
        if (isOnline && user.isOnline) {
            player.hidePlayer(plugin, user.getPlayer());
            return;
        }

        UserData data = UserManager.loadData(getUuidStr());
        data.setHideUUID(Util.addStrSet(data.getHideUUID(), user.getUuidStr()));
        UserManager.saveData(data);
    }

    public boolean teleport(Location location) {
        if (isOnline) {
            return player.teleport(location);
        }
        UserData data = UserManager.loadData(uuid.toString());
        return data.teleport(location);
    }

    public boolean equals(Player player) {
        return equals(player.getUniqueId());
    }

    public boolean equals(UUID uuid) {
        return this.uuid.equals(uuid);
    }

    public boolean containGroup(String name) {
        return groupList.contains(name);
    }

    public void joinEvent(PlayerJoinEvent event) {
        player = event.getPlayer();
        isOnline = true;
        name = player.getName();
    }

    public Scoreboard getScoreboard() {
        return getManager().getScoreboard();
    }

    public String getUuidStr() {
        return uuid.toString();
    }

    public String getName() {
        return name;
    }

    public InventoryView openInventory(Inventory inventory) {
        if (isOnline) {
            return player.openInventory(inventory);
        }
        return null;
    }

    public InventoryView openInventory(Storage storage) {
        return openInventory(storage.getInventory());
    }

    public List<String> getGroupList() {
        return groupList;
    }

    public Player getPlayer() {
        if (isOnline) {
            return player;
        }
        return null;
    }

    public UserManager getManager() {
        return manager;
    }
}

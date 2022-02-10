package net.kigawa.spigot.pluginutil.player;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.spigot.pluginutil.inventory.PlayerStorage;
import net.kigawa.spigot.pluginutil.inventory.Storage;
import net.kigawa.spigot.pluginutil.inventory.StorageManager;
import net.kigawa.spigot.pluginutil.message.Messenger;
import net.kigawa.spigot.pluginutil.tool.Tool;
import net.kigawa.util.Util;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User<U extends User<U>> {
    private final UUID uuid;
    private final UserManager<U> manager;
    private Player player;
    private PlayerStorage<U> playerStorage;
    private boolean isOnline;
    private List<String> groupList = new ArrayList<>();
    private String name;
    private Scoreboard scoreboard;
    private boolean allowFly;
    private List<User> hideUser = new ArrayList<>();

    private User(OfflinePlayer player, UserManager userManager, boolean isOnline) {
        this.isOnline = isOnline;
        this.manager = userManager;
        assert manager != null;
        manager.addUser((U) this);
        uuid = player.getUniqueId();
        name = player.getName();
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public User(Player player, UserManager manager) {
        this(player, manager, true);
        this.player = player;
        allowFly = player.getAllowFlight();
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

    public Location getLocation() {
        if (isOnline) {
            return player.getLocation();
        }
        return null;
    }

    public void playSound(Location location, Sound sound, float volume, float pitch) {
        if (isOnline) {
            player.playSound(location, sound, volume, pitch);
        }
    }

    public void sendTitle(String title, String subTitle) {
        sendTitle(title, subTitle, 10, 15, 10);
    }

    public void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        if (isOnline) {
            player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
        }
    }

    public void addTool(Tool... tool) {
        ItemStack[] itemStacks = new ItemStack[tool.length];
        for (int i = 0; i < tool.length; i++) itemStacks[i] = tool[i].getItemStack();
        addItem(itemStacks);
    }

    public void addItem(ItemStack... itemStack) {
        player.getInventory().addItem(itemStack);
    }

    public U getUser() {
        return (U) this;
    }

    public PlayerStorage<U> getPlayerStorage(StorageManager storageManager) {
        if (playerStorage == null) playerStorage = new PlayerStorage<U>(getUser(), name, storageManager);
        return playerStorage;
    }

    public void setTeamDisplayName(String teamName, String displayName) {
        Team team = getTeam(teamName);
        team.setDisplayName(displayName);
    }

    public void setTeamColor(String teamName, ChatColor color) {
        Team team = getTeam(teamName);
        team.setColor(color);
    }

    public void joinEvent(PlayerJoinEvent event) {
        player = event.getPlayer();
        isOnline = true;
        name = player.getName();
        player.setScoreboard(scoreboard);
        player.setAllowFlight(allowFly);
        for (User user : hideUser) {
            hide(manager.getPlugin(), user);
        }
        for (U user : manager.getUserList()) {
            user.checkHide(this);
        }
    }

    public void checkHide(User user) {
        if (hideUser.contains(user)) {
            hide(manager.getPlugin(), user);
        }
    }

    public void setMainScoreBord() {
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        player.setScoreboard(scoreboard);
    }

    public void setNewScoreBord() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);
    }

    public Team getTeam() {
        return scoreboard.getEntryTeam(name);
    }

    public void setTeam(String teamStr) {
        setTeam(teamStr, name);
    }

    public void setTeam(String teamName, String name) {
        Team team = getTeam(teamName);
        team.addEntry(name);
    }

    public Team getTeam(String teamName) {
        Team team = scoreboard.getTeam(teamName);
        if (team == null) team = scoreboard.registerNewTeam(teamName);
        return team;
    }

    public void spawnParticle(Particle particle, Location location, int count) {
        player.spawnParticle(particle, location, count);
    }

    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ);
    }

    public void setAllowFly(boolean fly) {
        allowFly = fly;
        if (isOnline) {
            player.setAllowFlight(fly);
        }
    }

    public void setFly(boolean fly) {
        if (isOnline && player.getAllowFlight()) {
            player.setFlying(allowFly);
        }
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
        var team = getTeam();
        if (team == null) return false;
        return team.getName().equals(name);
    }

    public void sendToolBar(StringBuffer sb) {
        Messenger.sendMessage(player, new TextComponent(sb.toString()), ChatMessageType.ACTION_BAR);
    }

    public void leaveTeam() {
        Team team = manager.getEntryTeam(name);
        if (team == null) return;
        team.removeEntry(name);
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
            if (sb == null) sb = new StringBuffer();

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

    public boolean equals(String name) {
        return this.name.equals(name);
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
        hideUser.remove(user);
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
        hideUser.add(user);
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

    public Scoreboard getScoreboard() {
        return scoreboard;
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

    public UserManager<U> getManager() {
        return manager;
    }
}

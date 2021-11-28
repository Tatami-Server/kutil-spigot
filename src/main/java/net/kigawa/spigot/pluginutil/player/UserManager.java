package net.kigawa.spigot.pluginutil.player;

import net.kigawa.spigot.pluginutil.PluginBase;
import net.kigawa.util.Util;
import net.kigawa.yamlutil.Yaml;
import net.kigawa.yamlutil.YamlData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UserManager<U extends User> implements Listener {
    private static Yaml yaml;
    private final List<U> userList = new ArrayList<>();
    private final PluginBase plugin;
    private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    public UserManager(PluginBase plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        yaml = getUserYaml();
        this.plugin = plugin;
    }

    public static Yaml getUserYaml() {
        if (yaml == null) {
            File dir = Paths.get("").toAbsolutePath().toFile();
            File user = new File(dir, "user");
            yaml = new Yaml(user, new CustomClassLoaderConstructor(PluginBase.class.getClassLoader()));
        }
        return yaml;
    }

    public static UserData loadData(String name) {
        return getUserYaml().load(UserData.class, name);
    }

    public static void saveData(UserData data) {
        getUserYaml().save(data);
    }

    public <U extends User> void joinTeamRandomUser(String teamName, int max, List<U> userList) {
        int index;
        Random random = new Random();
        for (int i = 1; i < max; i++) {
            if (userList.isEmpty()) {
                break;
            }
            index = random.nextInt(userList.size());
            U user = userList.get(index);
            setTeamAll(teamName, user.getName());
            userList.remove(index);
        }
    }

    public void setTeamDisplayNameAll(String team, String displayName) {
        executeUser((U u) -> u.setTeamDisplayName(team, displayName));
    }

    public void setTeamColorAll(String team, ChatColor color) {
        executeUser((U u) -> u.setTeamColor(team, color));
    }

    public void setTeamAll(String team, String entry) {
        executeUser((U u) -> u.setTeam(team, entry));
    }

    public void setNewScoreboardAll() {
        executeUser(User::setNewScoreBord);
    }

    public void clearEveryInv() {
        executeUser(User::clearInventory);
    }

    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        executeUser((U u) -> u.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ));
    }

    public void spawnParticle(Particle particle, Location location, int count) {
        executeUser((U u) -> u.spawnParticle(particle, location, count));
    }

    public void setAllowFly(boolean fly) {
        executeUser((U u) -> u.setAllowFly(fly));
    }

    public void setFly(boolean fly) {
        executeUser((U u) -> u.setFly(fly));
    }

    public List<U> getTeamUsers(String team) {
        List<U> list = new ArrayList<>();
        for (U user : userList) {
            if (user.isJoinTeam(team)) list.add(user);
        }
        return list;
    }

    public void sendToolBar(StringBuffer sb) {
        executeUser((U u) -> u.sendToolBar(sb));
    }

    public Team getEntryTeam(String entry) {
        U user = getUser(entry);
        if (user == null) return scoreboard.getEntryTeam(entry);
        return user.getTeam();
    }

    public void removeTeam(String name) {
        for (U user : userList) {
            Team team = user.getTeam(name);
            if (team == null) continue;
            team.unregister();
        }
        Team team = scoreboard.getTeam(name);
        if (team == null) return;
        team.unregister();
    }

    public void executeUser(Util.Process<U> process) {
        Util.executeIterable(userList, process);
    }

    public void sendTitleMessage(String title) {
        executeUser((U u) -> u.sendTitleMessage(title));
    }

    public void sendMessage(StringBuffer sb) {
        executeUser((U u) -> u.sendMessage(sb));
    }

    public void sendMessage(String message) {
        executeUser((U u) -> u.sendMessage(message));
    }

    public void sendMessage(String title, String message) {
        executeUser((U u) -> u.sendMessage(title, message));
    }

    public void saveData(YamlData data) {
        yaml.save(data);
    }

    public void removeUser(U user) {
        userList.remove(user);
    }

    public boolean contain(Player player) {
        for (User user : userList) {
            if (user.equals(player)) return true;
        }
        return false;
    }

    public List<U> getTagUsers(String name) {
        List<U> userList = new ArrayList<>();
        for (U user : this.userList) {
            if (user.containGroup(name)) {
                userList.add(user);
            }
        }
        return userList;
    }

    public List<U> addUsers(List<U> userList) {
        this.userList.addAll(userList);
        return this.userList;
    }

    public List<U> getUserList() {
        return userList;
    }

    public U addUser(U user) {
        userList.add(user);
        return user;
    }

    public U getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public U getUser(UUID uuid) {
        for (U user : userList) {

            if (user.equals(uuid)) {
                return user;
            }
        }
        return null;
    }

    public U getUser(String name) {
        for (U user : userList) {
            if (user.equals(name)) {
                return user;
            }
        }
        return null;
    }

    public World getWorld(String name) {
        return plugin.getServer().getWorld(name);
    }

    public PluginBase getPlugin() {
        return plugin;
    }

    public Yaml getYaml() {
        return yaml;
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent event) {
        U user = getUser(event.getPlayer().getUniqueId());
        if (user == null) return;
        user.joinEvent(event);
    }

    @EventHandler
    public void leaveEvent(PlayerQuitEvent event) {
        U user = getUser(event.getPlayer().getUniqueId());
        if (user == null) return;
        user.leaveEvent(event);
    }
}

//

// Dynamic Ping Kicker (entry point)
// Copyright, 2020, Jericho Crosby <jericho.crosby227@gmail.com>

//

package jericraft.chalwk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;

public final class DynamicPingKicker extends JavaPlugin implements Listener {

    Map<UUID, int[]> timer = new HashMap<>();

    ConfigurationSection config = this.getConfig();
    String ignore_perm = config.getString("ignore-permission");

    int grade_period = config.getInt("grace-period");
    int interval = config.getInt("check-interval");
    int max_warnings = config.getInt("max-warnings");

    List<String> kick_messages = config.getStringList("messages.kick");
    List<String> warn_messages = config.getStringList("messages.warn");

    Set<String> limits = config.getConfigurationSection("limits").getKeys(false);

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, this::onTick, 0, 20L);
        for (Player p : this.getServer().getOnlinePlayers()) {
            InitPlayer(p);
        }
    }

    @Override
    public void onDisable() {
        // N/A
    }

    @EventHandler(
            ignoreCancelled = true
    )
    private void onPlayerJoin(PlayerJoinEvent p) {
        InitPlayer(p.getPlayer());
    }

    @EventHandler(
            ignoreCancelled = true
    )
    private void onPlayerQuit(PlayerQuitEvent p) {
        UUID uuid = p.getPlayer().getUniqueId();
        timer.remove(uuid);
    }

    public void InitPlayer(Player p) {
        UUID uuid = p.getPlayer().getUniqueId();
        int[] params = {0, 0, 0};
        timer.put(uuid, params);
    }

    private void onTick() {

        for (Player p : this.getServer().getOnlinePlayers()) {
            if (!p.hasPermission(ignore_perm)) {

                UUID uuid = p.getPlayer().getUniqueId();
                int[] params = timer.get(uuid);

                int time_lapsed = params[0];
                int strikes = params[1];
                time_lapsed++;

                int grace = params[2];
                grace++;

                if (grace >= grade_period) {
                    strikes = 0;
                }

                if (time_lapsed >= interval) {
                    time_lapsed = 0;
                    try {
                        Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
                        int ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
                        int current_limit = getPingLimit();
                        if (ping >= current_limit) {
                            grace = 0;
                            strikes++;
                            if (strikes < max_warnings) {
                                for (String str : warn_messages) {
                                    str = str.replaceAll(Pattern.quote("%limit%"), Integer.toString(current_limit));
                                    str = str.replaceAll(Pattern.quote("%ping%"), Integer.toString(ping));
                                    str = ChatColor.translateAlternateColorCodes('&', str);
                                    p.sendMessage(str);
                                }
                            } else {
                                Bukkit.getScheduler().runTask(this, () -> p.kickPlayer("&4&lHigh Ping"));
                                String name = p.getDisplayName();
                                for (String str : kick_messages) {
                                    str = str.replaceAll(Pattern.quote("%name%"), name);
                                    str = str.replaceAll(Pattern.quote("%limit%"), Integer.toString(current_limit));
                                    str = str.replaceAll(Pattern.quote("%ping%"), Integer.toString(ping));
                                    str = ChatColor.translateAlternateColorCodes('&', str);
                                    Bukkit.broadcastMessage(str);
                                }
                            }
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }

                int[] arr = {time_lapsed, strikes, grace};
                timer.put(uuid, arr);
            }
        }
    }

    private int getPingLimit() {
        int count = 0;
        for (String key : limits) {
            System.out.println("KEY: " + key);
            int max_ping = config.getInt("limits." + key + ".max-ping");
            int min = config.getInt("limits." + key + ".min-players");
            int max = config.getInt("limits." + key + ".max-players");
            for (Player ignored : this.getServer().getOnlinePlayers()) {
                count++;
            }
            if (count >= min && count <= max) {
                return max_ping;
            }
        }
        return 1000;
    }
}

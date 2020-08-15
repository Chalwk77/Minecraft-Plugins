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

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            onTick();
        }, 0, 20L);
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
            String ignore_perm = this.getConfig().getString("ignore-permission");
            if (!p.hasPermission(ignore_perm)) {

                UUID uuid = p.getPlayer().getUniqueId();
                int[] params = timer.get(uuid);

                int time_lapsed = params[0];
                int strikes = params[1];
                time_lapsed++;

                int grace = params[2];
                grace++;

                int grade_period = this.getConfig().getInt("grace-period");
                if (grace >= grade_period) {
                    strikes = 0;
                }

                int interval = this.getConfig().getInt("check-interval");
                if (time_lapsed >= interval) {
                    time_lapsed = 0;
                    try {
                        Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
                        int ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
                        int current_limit = getPingLimit();
                        if (ping >= current_limit) {
                            grace = 0;
                            strikes++;
                            int max_warnings = this.getConfig().getInt("max-warnings");
                            if (strikes < max_warnings) {
                                List<String> list = this.getConfig().getStringList("messages.warn");
                                for (String str : list) {
                                    str = str.replaceAll(Pattern.quote("%limit%"), Integer.toString(current_limit));
                                    str = str.replaceAll(Pattern.quote("%ping%"), Integer.toString(ping));
                                    str = ChatColor.translateAlternateColorCodes('&', str);
                                    p.sendMessage(str);
                                }
                            } else {
                                Bukkit.getScheduler().runTask(this, () -> p.kickPlayer("&4&lHigh Ping"));
                                List<String> l2 = this.getConfig().getStringList("messages.kick");
                                String name = p.getDisplayName();
                                for (String str : l2) {
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
        ConfigurationSection Conf = this.getConfig().getConfigurationSection("limits");
        if (Conf != null) {
            Set<String> keys = Conf.getKeys(false);
            for (String key : keys) {
                int max_ping = Conf.getInt("limits." + key + ".max-ping");
                int min = Conf.getInt("limits." + key + ".min-players");
                int max = Conf.getInt("limits." + key + ".max-players");
                for (Player ignored : this.getServer().getOnlinePlayers()) {
                    count++;
                }
                if (count >= min && count <= max) {
                    return max_ping;
                }
            }
        }
        return 1000;
    }
}

// Minecraft Plugin Developed by Jericho Crosby (Chalwk)
// Copyright (c) 2022, Jericho Crosby <jericho.crosby227@gmail.com>

package com.jericho.jericraft;

import com.jericho.jericraft.commands.Feed;
import com.jericho.jericraft.commands.Heal;
import com.jericho.jericraft.handlers.TorchHandler;
import com.jericho.jericraft.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {

        instance = this;

        Bukkit.getLogger().info("The JeriCraft plugin has been enabled!");

        ConfigUtil config = new ConfigUtil(this, "config.yml");
        config.getConfig().options().copyDefaults(true);
        config.save();

        if (config.getConfig().getBoolean("Torch-Placement-Enabled")) {
            Bukkit.getLogger().warning("Torch-Placement-Enabled is set to true!");
            new TorchHandler(this);
        }

        Set<String> commands = config.getConfig().getConfigurationSection("Commands").getKeys(false);
        for (String key : commands) {
            boolean enabled = config.getConfig().getBoolean("Commands." + key);
            if (key.equalsIgnoreCase("feed") && enabled) {
                new Feed();
                Bukkit.getLogger().info("Feed command is enabled!");
            }
            if (key.equalsIgnoreCase("heal") && enabled) {
                new Heal();
                Bukkit.getLogger().info("Heal command is enabled!");
            }
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("JeriCraft has been disabled!");
    }

    // Returns the plugin instance:
    public static Main getInstance() {
        return instance;
    }

    // Reloads all configuration files:
    public static void ReloadConfig() {
        instance.reloadConfig();
    }
}

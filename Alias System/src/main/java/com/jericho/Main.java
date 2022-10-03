package com.jericho;

import com.jericho.commands.CheckAliases;
import com.jericho.listeners.JoinListener;
import com.jericho.util.ConfigUtil;
import com.jericho.util.Cprint;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;

import java.io.IOException;

import static com.jericho.util.FileIO.loadJSONArray;

public final class Main extends JavaPlugin {

    public static JSONArray database;
    private static Main instance;

    // Returns the plugin instance:
    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        Cprint.info("++++++++++++++++++++++++++++++++++++++++++++");
        Cprint.info("Alias System plugin has been enabled!");
        Cprint.info("++++++++++++++++++++++++++++++++++++++++++++");

        ConfigUtil config = new ConfigUtil(this, "config.yml");
        config.getConfig().options().copyDefaults(true);

        config.getConfig().addDefault("Base Command", "alias");
        config.getConfig().addDefault("Permission Node", "alias.check");

        // save config:
        config.save();

        // register commands:
        new CheckAliases();

        // register listeners:
        getServer().getPluginManager().registerEvents(new JoinListener(), this);

        try {
            database = loadJSONArray("aliases.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {

    }
}

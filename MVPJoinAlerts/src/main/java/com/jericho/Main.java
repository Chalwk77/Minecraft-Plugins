// MVPJoinAlert entry point
// Copyright 2020-2022, Jericho Crosby, <jericho.crosby227@gmail.com>

package com.jericho;

import com.jericho.listeners.Join;
import com.jericho.listeners.Reload;
import com.jericho.util.Cprint;
import com.jericho.util.Message;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Main extends JavaPlugin implements Listener {

    public final HashMap<String, Message> joinMessages = new HashMap<>();

    @Override
    public void onEnable() {

        Cprint.info("++++++++++++++++++++++++++++++++++++++++++++");
        Cprint.info("Alias System plugin has been enabled!");
        Cprint.info("++++++++++++++++++++++++++++++++++++++++++++");

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new Join(this), this);

        getCommand("mvp_reload").setExecutor(new Reload(this));

        reloadMessages();
    }

    public void reloadMessages() {
        ConfigurationSection config = this.getConfig().getConfigurationSection("JoinMessage");
        if (config != null) {

            Set<String> keys = config.getKeys(false);
            String rank;
            String permission_node;

            for (String key : keys) {
                rank = key;
                permission_node = this.getConfig().getString("JoinMessage." + rank + ".permission");
                List<String> messages = this.getConfig().getStringList("JoinMessage." + rank + ".message");
                Message message = new Message(messages);
                this.joinMessages.put(permission_node, message);
            }
        }
    }
}

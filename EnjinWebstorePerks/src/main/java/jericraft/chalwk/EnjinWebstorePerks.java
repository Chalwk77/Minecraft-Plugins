// Entry Point for EnjinWebstorePerks plugin.
// Copyright (c) 2020, Jericho Crosby <jericho.crosby227@gmail.com>

package jericraft.chalwk;

import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public final class EnjinWebstorePerks extends JavaPlugin implements Listener {

    public static FileConfiguration config;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        config = this.getConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        try {
            final Field bukkitCommandMap = this.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(this.getServer());
            String cmd = config.getString("command");
            commandMap.register(cmd, new CommandClass(cmd, this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

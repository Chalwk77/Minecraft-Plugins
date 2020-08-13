package jericraft.chalwk;

import jericraft.chalwk.command.HealMeCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class HealMe extends JavaPlugin {

    public static FileConfiguration config;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new HealMeCommand(), this);
        HealMeCommand commands = new HealMeCommand();
        getCommand("heal").setExecutor(commands);
        getCommand("feed").setExecutor(commands);
        config = this.getConfig();
        this.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

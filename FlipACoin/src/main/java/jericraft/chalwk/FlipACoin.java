package jericraft.chalwk;

import org.bukkit.plugin.java.JavaPlugin;

public class FlipACoin extends JavaPlugin {
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getCommand("flip").setExecutor(new CoinFlipper(this));
    }
}

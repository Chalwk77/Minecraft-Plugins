package jericraft.chalwk;

import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TacInsert extends JavaPlugin implements Listener {

    public static FileConfiguration config;

    Map<UUID, int[]> insertions = new HashMap<>();

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

            for (Player p : this.getServer().getOnlinePlayers()) {
                InitPlayer(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent p) {
        InitPlayer(p.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerQuit(PlayerQuitEvent p) {
        UUID uuid = p.getPlayer().getUniqueId();
        insertions.remove(uuid);
    }

    public void InitPlayer(Player p) {
        UUID uuid = p.getPlayer().getUniqueId();
        int remaining_insertions = config.getInt("insertions-per-life");
        int[] params = {0, 0, 0, remaining_insertions};
        insertions.put(uuid, params);
    }
}

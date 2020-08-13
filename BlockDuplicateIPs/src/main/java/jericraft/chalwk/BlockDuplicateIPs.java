package jericraft.chalwk;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class BlockDuplicateIPs extends JavaPlugin implements Listener {

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {

    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player NewPlayer = event.getPlayer();
        UUID p1uuid = NewPlayer.getUniqueId();
        String NewPlayerIP = getIP(NewPlayer);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            UUID p2uuid = p.getUniqueId();
            if (p2uuid != p1uuid) {
                String ip = getIP(p);
            }
        }
    }

    public static String getIP(Player p) {
        return p.getAddress().getAddress().getHostAddress();
    }
}

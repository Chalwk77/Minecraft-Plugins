package jericraft.chalwk;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class BlockDuplicateIPs extends JavaPlugin implements Listener {

    public void onEnable() {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("BLOCK DUPLICATE IP ADDRESSES ENABLED");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");

    }

    public void onDisable() {

    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            String ip = getIP(player);
            System.out.println(player.getDisplayName() + " IP ADDRESS: " + ip);
        }
    }

    public static String getIP(Player player) {
        return player.getAddress().getHostName();
    }
}

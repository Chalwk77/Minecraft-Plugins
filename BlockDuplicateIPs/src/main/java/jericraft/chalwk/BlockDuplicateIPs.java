package jericraft.chalwk;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BlockDuplicateIPs extends JavaPlugin implements Listener {

    ArrayList<String> whitelist = new ArrayList<>(1000);

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.saveDefaultConfig();
        this.SaveWhitelist();
    }

    public void onDisable() {

    }

    public void SaveWhitelist() {
        List<String> whitelistedIP = this.getConfig().getStringList("settings.whitelist");
        for (String IP : whitelistedIP) {
            whitelist.add(IP);
        }
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player NewPlayer = event.getPlayer();
        UUID newPlayerUUID = NewPlayer.getUniqueId();
        String NewPlayerIP = getIP(NewPlayer);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getUniqueId() != newPlayerUUID) {
                String ip = getIP(p);
                if (ip.equals(NewPlayerIP)) {
                    boolean whitelisted = false;
                    for (String whitelistedIP : whitelist) {
                        if (NewPlayerIP.equals(whitelistedIP)) {
                            whitelisted = true;
                        }
                    }
                    if (!whitelisted) {
                        String reason = this.getConfig().getString("reason");
                        String action = this.getConfig().getString("default-action");
                        if (action.equals("kick")) {
                            NewPlayer.kickPlayer(reason);
                        } else if (action.equals("ban")) {
                            banPlayer(NewPlayer, reason, null);
                        }
                    }
                }
            }
        }
    }

    static void banPlayer(Player player, String reason, String source) {

        BanList bans = Bukkit.getServer().getBanList(BanList.Type.NAME);
        bans.addBan(player.getName(), reason, null, source);

        if (player.isOnline()) {
            player.kickPlayer(reason);
        }
    }

    public static String getIP(Player p) {
        return p.getAddress().getAddress().getHostAddress();
    }
}

package jericraft.chalwk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class CommandClass extends BukkitCommand implements Listener {
    private final TacInsert plugin;
    private final FileConfiguration config;

    public CommandClass(String cmd, TacInsert plugin) {

        super(cmd);
        this.description = "Set your next Spawn Point with a custom command!";
        this.usageMessage = "/" + cmd;

        this.plugin = plugin;
        this.config = plugin.getConfig();

        String perm = config.getString("permission-node");
        this.setPermission(perm);

        List<String> list = config.getStringList("command-aliases");
        this.setAliases(list);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            final Player p = e.getPlayer();
            UUID uuid = p.getPlayer().getUniqueId();

            int[] params = plugin.insertions.get(uuid);
            int max_uses = config.getInt("insertions-per-life");
            if (params[3] < max_uses) {
                String world = p.getWorld().getName();

                int x = params[0];
                int y = params[1];
                int z = params[2];
                Location loc = new Location(Bukkit.getWorld(world), x, y, z);
                p.teleport(loc);

                params[3] = max_uses;
                plugin.insertions.put(uuid, params);
            }

        }, 1L);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (sender instanceof Player) {

            String perm = this.getPermission();
            assert perm != null;

            if (!sender.hasPermission(perm)) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to execute that command");
                return true;
            } else {

                Player p = (Player) sender;
                Location loc = p.getLocation();

                double x = loc.getX();
                int xInt = (int) x;

                double y = loc.getY();
                int yInt = (int) y;

                double z = loc.getZ();
                int zInt = (int) z;

                UUID uuid = p.getPlayer().getUniqueId();
                int[] params = plugin.insertions.get(uuid);

                int insertions_remaining = params[3];
                if (insertions_remaining <= 0) {
                    String str = config.getString("no-inserts-left");
                    str = ChatColor.translateAlternateColorCodes('&', str);
                    sender.sendMessage(str);
                    return true;
                }

                insertions_remaining--;
                int[] arr = {xInt, yInt, zInt, insertions_remaining};
                plugin.insertions.put(uuid, arr);

                String str = config.getString("on-execute");
                str = str.replaceAll(Pattern.quote("%x%"), String.valueOf(xInt));
                str = str.replaceAll(Pattern.quote("%y%"), String.valueOf(yInt));
                str = str.replaceAll(Pattern.quote("%z%"), String.valueOf(zInt));

                str = ChatColor.translateAlternateColorCodes('&', str);
                sender.sendMessage(str);

                for (Player i : plugin.getServer().getOnlinePlayers()) {
                    if (i != p) {
                        String bmsg = config.getString("broadcast-message");
                        bmsg = ChatColor.translateAlternateColorCodes('&', bmsg);
                        bmsg = bmsg.replaceAll(Pattern.quote("%name%"), sender.getName());
                        i.sendMessage(bmsg);
                    }
                }
            }
        } else {
            sender.sendMessage("Only players can execute that command!");
        }

        return true;
    }
}
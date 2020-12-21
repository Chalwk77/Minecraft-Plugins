// command class for EnjinWebstorePerks plugin.
// Copyright (c) 2020, Jericho Crosby <jericho.crosby227@gmail.com>

package jericraft.chalwk;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

//import ru.tehkode.permissions.PermissionUser;
//import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommandClass extends BukkitCommand implements Listener {

    private final FileConfiguration config;
    public CommandClass(String cmd, EnjinWebstorePerks plugin) {

        super(cmd);
        this.description = "View all purchased webstore perks";
        this.usageMessage = "/" + cmd;
        this.config = plugin.getConfig();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if (sender instanceof Player) {

            //Player p = (Player) sender;
            //PermissionUser user = PermissionsEx.getUser(p.getName());
            //List<String> perms = user.getOwnPermissions(p.getWorld().getName());

            int total = 0;
            sender.sendMessage(ChatColor.GREEN + "========== [PERKS] ==========");
            for (Map<?, ?> opt : config.getMapList("nodes")) {
                List<String> node = new ArrayList<>((Collection<? extends String>) opt.keySet());
                for (String s : node) {
                    if (sender.hasPermission(s)) {
                        List<String> perks = new ArrayList<>((Collection<? extends String>) opt.values());
                        sender.sendMessage(ChatColor.AQUA + perks.get(0));
                        total = total + Integer.parseInt(perks.get(1));
                    }
                }
            }

            if (total > 0) {
                sender.sendMessage(ChatColor.GREEN + "Total amount of money spend: $" + total);
            } else {
                sender.sendMessage(ChatColor.RED + "You have not purchased any perks!");
            }
        } else {
            sender.sendMessage("Only players can execute that command!");
        }
        return true;
    }
}
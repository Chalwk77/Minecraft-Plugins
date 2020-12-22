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

        if (args.length >= 1) {
            if (sender.hasPermission(config.getString("RELOAD_PERMISSION"))) {
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m==========================="));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("ONRELOAD")));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m==========================="));
                    EnjinWebstorePerks.ReloadConfig();
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Invalid Command Syntax"));
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Usage: &8Usage: /" + config.getString("commands") + " reload"));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("NO_PERMISSION")));
            }
        } else if (sender instanceof Player) {

            //Player p = (Player) sender;
            //PermissionUser user = PermissionsEx.getUser(p.getName());
            //List<String> perms = user.getOwnPermissions(p.getWorld().getName());

            int total = 0;
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("HEADER")));
            String content = config.getString("CONTENT");

            for (Map<?, ?> opt : config.getMapList("nodes")) {
                List<String> node = new ArrayList<>((Collection<? extends String>) opt.keySet());
                for (String s : node) {
                    if (sender.hasPermission(s)) {
                        List<String> perks = new ArrayList<>((Collection<? extends String>) opt.values());
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', content.replace("%perks%", perks.get(0))));
                        total = total + Integer.parseInt(perks.get(1));
                    }
                }
            }

            if (total > 0) {
                String footer = config.getString("FOOTER");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', footer.replace("%total%", Integer.toString(total))));
            } else {
                String str = config.getString("NO_PURCHASES");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
            }
        } else {
            sender.sendMessage("Only players can execute that command!");
        }

        return true;
    }
}
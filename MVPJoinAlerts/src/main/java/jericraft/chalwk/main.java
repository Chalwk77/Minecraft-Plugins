package jericraft.chalwk;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {
    private HashMap<String, Message> joinMessages = new HashMap();

    public void onEnable() {
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.reloadMessages();
    }

    public void reloadMessages() {
        ConfigurationSection joinConf = this.getConfig().getConfigurationSection("JoinMessage");
        String rank;
        String permission_node;
        if (joinConf != null) {
            Set<String> keys = joinConf.getKeys(false);
            Iterator var3 = keys.iterator();

            while (var3.hasNext()) {
                rank = (String) var3.next();
                permission_node = this.getConfig().getString("JoinMessage." + rank + ".permission");
                List<String> messages = this.getConfig().getStringList("JoinMessage." + rank + ".message");
                Message message = new Message(messages);
                this.joinMessages.put(permission_node, message);
            }
        }
    }

    @EventHandler(
            ignoreCancelled = true
    )
    public void onPlayerJoin(PlayerJoinEvent event) {

        List<String> messages = null;
        Iterator var15 = this.joinMessages.keySet().iterator();
        while (var15.hasNext()) {
            String perm = (String) var15.next();
            if (event.getPlayer().hasPermission(perm)) {
                messages = this.joinMessages.get(perm).getMessages();
                break;
            }

            // Player doesn't have permission node for any MVP tier. Set default join message
            if (messages == null) {
                Message message = this.joinMessages.getOrDefault("none", new Message(Arrays.asList(event.getJoinMessage(), event.getJoinMessage())));
                messages = message.getMessages();
            }
        }

        Collections.shuffle(messages);
        String message = messages.get(0);
        String Str = message.replaceAll("%name%", event.getPlayer().getName());
        String msg = ChatColor.translateAlternateColorCodes('&', Str);

        this.getServer().getScheduler().runTaskLater(this, () -> {
            getServer().broadcastMessage(msg);
        }, 20L);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("mvp")) {
            if (sender.hasPermission("mvp.reload")) {
                if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
                    this.reloadConfig();
                    this.reloadMessages();
                    sender.sendMessage(ChatColor.GREEN + "Configuration Reloaded!");
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Invalid Syntax. &8Usage: /mvp reload"));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You do not have permission to execute that command."));
            }
            return true;
        } else {
            return false;
        }
    }
}

// MVPJoinAlert entry point
// Copyright 2020, Jericho Crosby, <jericho.crosby227@gmail.com>

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

public class mvpjoinalerts extends JavaPlugin implements Listener {
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

        boolean proceed = true;

        List<String> messages = null;
        Iterator var15 = this.joinMessages.keySet().iterator();
        while (var15.hasNext()) {
            String perm = (String) var15.next();

            if (event.getPlayer().isOp()) {
                event.getPlayer().setOp(false);
                if (event.getPlayer().hasPermission(perm)) {
                    messages = this.joinMessages.get(perm).getMessages();
                    event.getPlayer().setOp(true);
                    break;
                } else {
                    event.getPlayer().setOp(true);
                }
            } else if (event.getPlayer().hasPermission(perm)) {
                messages = this.joinMessages.get(perm).getMessages();
                break;
            }

            if (messages == null) {
                boolean enabled = this.getConfig().getBoolean("JoinMessage.default.enabled");
                if (enabled == proceed) {
                    Message message = this.joinMessages.getOrDefault("none", new Message(Arrays.asList(event.getJoinMessage(), event.getJoinMessage())));
                    messages = message.getMessages();
                } else {
                    proceed = false;
                }
            }
        }

        if (proceed) {

            Collections.shuffle(messages);
            String message = messages.get(0);
            String Str = message.replace("%name%", event.getPlayer().getName());
            String msg = ChatColor.translateAlternateColorCodes('&', Str);

            this.getServer().getScheduler().runTaskLater(this, () -> {
                getServer().broadcastMessage(msg);
            }, 20L);
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("mvp")) {
            if (sender.hasPermission("mvp.reload")) {

                if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
                    this.reloadConfig();
                    this.reloadMessages();
                    sender.sendMessage(ChatColor.GREEN + "&8[&3MVPJoinAlerts&8] &aConfiguration Reloaded!");
                    System.out.println("[MVPJoinAlerts] configuration reloaded");
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

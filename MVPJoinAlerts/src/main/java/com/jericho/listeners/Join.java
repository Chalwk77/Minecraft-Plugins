package com.jericho.listeners;

import com.jericho.Main;
import com.jericho.util.Message;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class Join implements Listener {

    public final Main instance;

    public Join(Main plugin) {
        this.instance = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {

        boolean proceed = true;

        List<String> messages = null;
        for (String perm : instance.joinMessages.keySet()) {
            if (event.getPlayer().isOp()) {
                event.getPlayer().setOp(false);
                if (event.getPlayer().hasPermission(perm)) {
                    messages = instance.joinMessages.get(perm).getMessages();
                    event.getPlayer().setOp(true);
                    break;
                } else {
                    event.getPlayer().setOp(true);
                }
            } else if (event.getPlayer().hasPermission(perm)) {
                messages = instance.joinMessages.get(perm).getMessages();
                break;
            }

            if (messages == null) {
                boolean enabled = instance.getConfig().getBoolean("JoinMessage.default.enabled");
                if (enabled == proceed) {
                    Message message = instance.joinMessages.getOrDefault("none", new Message(Arrays.asList(event.getJoinMessage(), event.getJoinMessage())));
                    messages = message.getMessages();
                } else {
                    proceed = false;
                }
            }
        }

        if (proceed && messages != null) {

            Collections.shuffle(messages);
            String message = messages.get(0);
            String Str = message.replace("$name", event.getPlayer().getName());
            String msg = ChatColor.translateAlternateColorCodes('&', Str);

            instance.getServer().getScheduler().runTaskLater(instance, () -> getServer().broadcastMessage(msg), 20L);
        }
    }
}

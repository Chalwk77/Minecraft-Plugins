package jericraft.chalwk;

import java.util.*;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin implements Listener {

    private HashMap<String, Message> expressions = new HashMap();

    public void onEnable() {
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "+----------------------------------------+");
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "|       " + ChatColor.WHITE + "Expressions " + this.getDescription().getVersion() + " - Enabled" + ChatColor.GREEN + "       |");
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "+----------------------------------------+");
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.LoadSettings();
    }

    public void LoadSettings() {
        ConfigurationSection config = this.getConfig().getConfigurationSection("settings");
        String type;
        String trigger;
        if (config != null) {
            Set<String> keys = config.getKeys(false);
            for (String key : keys) {
                type = key;
                trigger = this.getConfig().getString("settings." + type + ".trigger");
                List<String> messages = this.getConfig().getStringList("settings." + type + ".messages");
                Message message = new Message(messages);
                this.expressions.put(trigger, message);
            }
        }
    }

    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "+----------------------------------------+");
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "|       " + ChatColor.WHITE + "Expressions " + this.getDescription().getVersion() + " - Disabled" + ChatColor.RED + "      |");
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "+----------------------------------------+");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        String message = event.getMessage();
        String[] arr = message.split(" ", 2);
        String word = arr[0];

        List<String> messages;
        for (String trigger : this.expressions.keySet()) {
            if (word.equals(trigger)) {
                messages = this.expressions.get(trigger).getMessages();
                Collections.shuffle(messages);
                String msg = ChatColor.translateAlternateColorCodes('&', messages.get(0));
                event.setMessage(msg);
                break;
            }
        }
    }
}

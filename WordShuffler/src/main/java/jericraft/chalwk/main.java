package jericraft.chalwk;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin implements Listener {

    public void onEnable() {
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "+----------------------------------------+");
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "|       " + ChatColor.WHITE + "Word Shuffler " + this.getDescription().getVersion() + " - Enabled" + ChatColor.GREEN + "       |");
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "+----------------------------------------+");

        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(this, (this));
    }

    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "+----------------------------------------+");
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "|       " + ChatColor.WHITE + "Word Shuffler " + this.getDescription().getVersion() + " - Disabled" + ChatColor.RED + "      |");
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "+----------------------------------------+");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Random r = new Random();
        event.setMessage(scramble(r, message));
    }

    public static String scramble(Random random, String inputString) {
        char a[] = inputString.toCharArray();

        for (int i = 0; i < a.length; i++) {
            int j = random.nextInt(a.length);
            char temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }

        return new String(a);
    }
}

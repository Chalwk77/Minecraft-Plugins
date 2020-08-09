//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package jericraft.chalwk;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class HealthRegeneration extends JavaPlugin implements Listener {

    int rate = this.getConfig().getInt("rate");
    int delay = this.getConfig().getInt("delay");

    public void onEnable() {
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "+----------------------------------------+");
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "|       " + ChatColor.WHITE + "Health Regen " + this.getDescription().getVersion() + " - Enabled" + ChatColor.GREEN + "       |");
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "+----------------------------------------+");
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {

            for (Player p : this.getServer().getOnlinePlayers()) {
                if (p.getHealth() != 20.0D) {
                    if (p.getHealth() + (double) this.rate > 20.0D) {
                        p.setHealth(20.0D);
                    } else {
                        p.setHealth(p.getHealth() + (double) this.rate);
                    }
                }
            }

        }, 0, delay * 20);
    }

    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "+----------------------------------------+");
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "|       " + ChatColor.WHITE + "Health Regen " + this.getDescription().getVersion() + " - Disabled" + ChatColor.RED + "      |");
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "+----------------------------------------+");
    }
}

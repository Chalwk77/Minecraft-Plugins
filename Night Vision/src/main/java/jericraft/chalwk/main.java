package jericraft.chalwk;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class main extends JavaPlugin {

    public String toggle_on = this.getConfig().getString("settings.toggle_on");
    public String toggle_off = this.getConfig().getString("settings.toggle_off");
    public String permission_node = this.getConfig().getString("settings.permission_node");
    public String no_permission = this.getConfig().getString("settings.no_permission");

    public void onEnable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    public void onDisable() {

    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("nv") && sender != null) {
            if (player.hasPermission(permission_node)) {

                Location loc = player.getLocation();

                player.playSound(loc, Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F, 0.0F);
                loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 2004);

                if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.toggle_off));
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    return false;
                }

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.toggle_on));
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100000, 1));
                return true;
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.no_permission));
            }
        }
        return false;
    }
}

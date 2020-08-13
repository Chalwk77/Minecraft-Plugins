package jericraft.chalwk;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NightVision extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "+----------------------------------------+");
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "|       " + ChatColor.WHITE + "Night Vision " + this.getDescription().getVersion() + " - Enabled" + ChatColor.GREEN + "       |");
        this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "+----------------------------------------+");
        loadConfig();
    }

    @Override
    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "+----------------------------------------+");
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "|       " + ChatColor.WHITE + "Night Vision " + this.getDescription().getVersion() + " - Disabled" + ChatColor.RED + "      |");
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "+----------------------------------------+");
    }

    public void loadConfig() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public String toggle_on = this.getConfig().getString("settings.toggle_on");
    public String toggle_off = this.getConfig().getString("settings.toggle_off");
    public String permission_node = this.getConfig().getString("settings.permission_node");
    public String no_permission = this.getConfig().getString("settings.no_permission");
    public String custom_command = this.getConfig().getString("settings.command");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase(custom_command)) {

                if (player.hasPermission(permission_node)) {

                    Location loc = player.getLocation();
                    player.playSound(loc, Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F, 0.0F);
                    loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 2004);

                    if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.toggle_off));
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        return true;
                    }

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.toggle_on));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100000, 1));
                    return true;
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.no_permission));
                    return true;
                }
            }
        }
        return true;
    }
}

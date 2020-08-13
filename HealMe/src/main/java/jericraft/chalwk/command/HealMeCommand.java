package jericraft.chalwk.command;

import jericraft.chalwk.HealMe;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class HealMeCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute that command!");
            return true;
        }

        Player player = (Player) sender;
        boolean effect = false;

        if (cmd.getName().equalsIgnoreCase("heal")) {
            double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue();
            player.setHealth(maxHealth);
            String response = HealMe.config.getString("heal_response");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', response));
            effect = true;
        } else if (cmd.getName().equalsIgnoreCase("feed")) {
            player.setFoodLevel(20);
            String response = HealMe.config.getString("feed_response");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', response));
            effect = true;
        }

        if (effect) {
            Location loc = player.getLocation();
            player.playSound(loc, Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1.0F, 0.0F);
            loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 2004);
        }

        return true;
    }
}

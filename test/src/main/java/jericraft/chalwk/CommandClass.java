package jericraft.chalwk;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClass implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("mycommand")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length < 1) {
                    sender.sendMessage(ChatColor.GREEN + " Please include how much you want to be healed by");
                    return true;
                } else if (args.length == 1) {
                    try {
                        double phealth = player.getHealth();
                        double addhealth = Double.parseDouble(args[0]);
                        if (phealth < 20) {
                            player.setHealth(phealth + addhealth);
                            player.sendMessage(ChatColor.GRAY + "You have been healed by " + ChatColor.GREEN + addhealth + " health");
                            return true;
                        } else {
                            player.sendMessage(ChatColor.YELLOW + "If you take some damage...  I can heal you!");
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Please input a real number");
                        return true;
                    }
                }

            } else {
                // command was executed by Console
            }
        }
        return true;
    }
}

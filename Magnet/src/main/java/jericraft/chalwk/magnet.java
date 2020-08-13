package jericraft.chalwk;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class magnet extends JavaPlugin {
    private double maxDistance = 8.0D;
    private final List<Player> magnetPlayers = new CopyOnWriteArrayList();

    public void onEnable() {
        this.saveDefaultConfig();
        this.maxDistance = this.getConfig().getInt("max-distance");
        magnet.ItemSearch itemSearch = new magnet.ItemSearch();
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, itemSearch, 5L, 5L);
    }

    String cmdStr = this.getConfig().getString("command");
    String permNode = this.getConfig().getString("permission-node");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(cmdStr)) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
            } else {
                Player player = (Player) sender;
                if (player.hasPermission(permNode)) {
                    if (args.length == 1) {
                        String arg = args[0].toLowerCase();
                        if (arg.equals("on") || arg.equals("1") || arg.equals("true")) {
                            if (!this.magnetPlayers.contains(player)) {
                                this.magnetPlayers.add(player);
                                String str = this.getConfig().getString("messages.toggle-on");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
                            } else {
                                String str = this.getConfig().getString("messages.already-on");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
                            }
                        } else if (arg.equals("off") || arg.equals("0") || arg.equals("false")) {
                            if (this.magnetPlayers.contains(player)) {
                                this.magnetPlayers.remove(player);
                                String str = this.getConfig().getString("messages.toggle-off");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
                            } else {
                                String str = this.getConfig().getString("messages.already-off");
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', str));
                            }
                        }
                    } else {
                        sender.sendMessage("Usage: /" + cmdStr + " <on|off>");
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private class ItemSearch implements Runnable {
        private ItemSearch() {

        }

        public void run() {
            Iterator i$x = magnet.this.getServer().getWorlds().iterator();

            label59:
            while (i$x.hasNext()) {
                World world = (World) i$x.next();
                Iterator i$xx = world.getEntities().iterator();

                while (true) {
                    Item item;
                    Location location;
                    do {
                        ItemStack stack;
                        do {
                            do {
                                Entity entity;
                                do {
                                    if (!i$xx.hasNext()) {
                                        continue label59;
                                    }

                                    entity = (Entity) i$xx.next();
                                } while (!(entity instanceof Item));

                                item = (Item) entity;
                                stack = item.getItemStack();
                                location = item.getLocation();
                            } while (stack.getAmount() <= 0);
                        } while (item.isDead());
                    } while (item.getPickupDelay() > item.getTicksLived());

                    Player closestPlayer = null;
                    double distanceSmall = magnet.this.maxDistance;

                    for (Player player : magnet.this.magnetPlayers) {
                        if (player != null && player.getWorld().getName().equals(world.getName())) {
                            double playerDistance = player.getLocation().distance(location);
                            if (playerDistance < distanceSmall) {
                                distanceSmall = playerDistance;
                                closestPlayer = player;
                            }
                        }
                    }

                    if (closestPlayer != null) {
                        item.setVelocity(closestPlayer.getLocation().toVector().subtract(item.getLocation().toVector()).normalize());
                    }
                }
            }

        }
    }
}

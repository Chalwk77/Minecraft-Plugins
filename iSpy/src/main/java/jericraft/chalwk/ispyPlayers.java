package jericraft.chalwk;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ispyPlayers implements Listener {
    private final ispy plugin;
    private final FileConfiguration config;

    public ispyPlayers(ispy plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        Player player = event.getPlayer();
        Iterator var4 = this.config.getStringList("HiddenCommands").iterator();

        boolean comcont;
        boolean comblock;
        do {
            if (!var4.hasNext()) {
                this.plugin.logger.logCommand(command, player);

                if (this.config.getBoolean("ConsoleCommands")) {
                    final String prefix = this.config.getString("PrefixConsoleCommand");
                    final String cmd = this.config.getString("ConsoleNoticeCmd");
                    System.out.println(String.format("%s%s", prefix, cmd.replace("%player%", player.getName()).replace("%cmd%", command)));
                }

                var4 = getOnlinePlayers().iterator();

                while (var4.hasNext()) {
                    Player p = (Player) var4.next();
                    if (this.config.getBoolean("LogCommands") && !p.equals(player) && p.hasPermission("ispy.noticecmd") && !this.plugin.iSpyEnabled.containsKey(p)) {
                        final String prefix = this.config.getString("PrefixCommand");
                        final String cmd = this.config.getString("PlayerUsedCommand");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("%s%s", prefix, cmd.replace("%player%", player.getName()).replace("%cmd%", command))));
                    }
                }

                return;
            }

            String s = (String) var4.next();
            comcont = command.contains(s.toLowerCase() + " ");
            comblock = s.toLowerCase().equals(command);
        } while (!(comblock ^ comcont));
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void onBook(PlayerEditBookEvent e) {
        Player player = e.getPlayer();
        String tmp = e.getNewBookMeta().toString().substring(0, e.getNewBookMeta().toString().length() - 1);
        if (this.config.getBoolean("ConsoleBooks")) {
            final String prefix = this.config.getString("PrefixConsoleBook");
            final String content = this.config.getString("ConsoleNoticeBook");
            System.out.println(String.format("%s%s", prefix, content.replace("%player%", player.getName()).replace("%text%", tmp.substring(33))));
        }

        Iterator var4 = getOnlinePlayers().iterator();

        while (var4.hasNext()) {
            Player p = (Player) var4.next();
            if (this.config.getBoolean("LogBooks") && !p.equals(player) && p.hasPermission("ispy.noticebook") && !this.plugin.iSpyEnabled.containsKey(p)) {
                final String prefix = this.config.getString("prefixBook");
                final String content = this.config.getString("BookOutput");
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("%s%s", prefix, content.replace("%player%", player.getName()).replace("%text%", tmp.substring(33)))));
            }
        }
    }

    public static List<Player> getOnlinePlayers() {
        List<Player> list = Lists.newArrayList();
        Iterator var1 = Bukkit.getWorlds().iterator();

        while (var1.hasNext()) {
            World world = (World) var1.next();
            list.addAll(world.getPlayers());
        }

        return Collections.unmodifiableList(list);
    }

    @EventHandler
    public void onInteract(SignChangeEvent sign) {
        Player player = sign.getPlayer();
        if (this.config.getBoolean("ConsoleSigns")) {
            final String prefix = this.config.getString("PrefixConsoleSign");
            final String content = this.config.getString("ConsoleNoticeSigns");
            System.out.println(String.format("%s%s", prefix, content.replace("%player%", player.getName()).replace("%line1%", sign.getLine(0)).replace("%line2%", sign.getLine(1)).replace("%line3%", sign.getLine(2)).replace("%line4%", sign.getLine(3))));
        }

        Iterator var3 = getOnlinePlayers().iterator();

        while (var3.hasNext()) {
            Player p = (Player) var3.next();
            if (!p.equals(player) && p.hasPermission("ispy.noticesign") && this.config.getBoolean("LogSigns") && !this.plugin.iSpyEnabled.containsKey(p)) {
                final String prefix = this.config.getString("PrefixSign");
                final String content = this.config.getString("PlayerPlaceSign");
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("%s%s", prefix, content.replace("%player%", player.getName()).replace("%line1%", sign.getLine(0)).replace("%line2%", sign.getLine(1)).replace("%line3%", sign.getLine(2)).replace("%line4%", sign.getLine(3)))));
            }
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )

    public void onInventoryClick(InventoryClickEvent e) {
        if (!e.isCancelled() && this.config.getBoolean("LogAnvils")) {
            HumanEntity ent = e.getWhoClicked();
            if (ent instanceof Player) {
                Player player = (Player) ent;
                Inventory inv = e.getInventory();
                if (inv instanceof AnvilInventory) {
                    InventoryView view = e.getView();
                    int rawSlot = e.getRawSlot();
                    if (rawSlot == view.convertSlot(rawSlot) && rawSlot == 2) {
                        ItemStack item = e.getCurrentItem();
                        if (item != null) {
                            ItemMeta meta = item.getItemMeta();
                            if (meta != null && meta.hasDisplayName()) {
                                String displayName = meta.getDisplayName();
                                if (this.config.getBoolean("ConsoleAnvils")) {
                                    final String prefix = this.config.getString("PrefixConsoleAnvil");
                                    final String content = this.config.getString("ConsoleNoticeAnvils");
                                    System.out.println(String.format("%s%s", prefix, content.replace("%player%", player.getName()).replace("%item%", e.getCurrentItem().getData().getItemType().toString()).replace("%renamed%", displayName)));
                                }

                                Iterator var10 = getOnlinePlayers().iterator();

                                while (var10.hasNext()) {
                                    Player p = (Player) var10.next();
                                    if (!p.equals(player) && p.hasPermission("ispy.noticeanvil") && !this.plugin.iSpyEnabled.containsKey(p)) {
                                        final String prefix = this.config.getString("PrefixAnvil");
                                        final String content = this.config.getString("PlayerUsedAnvil");
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("%s%s", prefix, content.replace("%player%", player.getName()).replace("%item%", e.getCurrentItem().getData().getItemType().toString()).replace("%renamed%", displayName))));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}

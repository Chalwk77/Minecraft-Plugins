//
// Entry point for iSpy Spigot Minecraft plugin
// Copyright 2020 Jericho Crosby <jericho.crosby227@gmail.com>
//

package jericraft.chalwk;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ispy extends JavaPlugin {
    protected ispyFiles logger = null;
    public HashMap<CommandSender, String> iSpyEnabled = new HashMap();


    public void onEnable() {
        this.logger = new ispyFiles();
        this.getServer().getPluginManager().registerEvents(new ispyPlayers(this), this);
        new ispyPlayers(this);
        this.saveDefaultConfig();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("ispy")) {
            if (sender instanceof Player) {
                String prefix = this.getConfig().getString("Prefix");
                if (sender.hasPermission("ispy.cmds")) {

                    if (args.length >= 1) {

                        if (args[0].equalsIgnoreCase("cmd")) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m==========================="));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("HelpCommandsLabel")));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m==========================="));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("HelpInfo")));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("HelpCmd")));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("HelpVersion")));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("HelpReload")));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("HelpAllow")));
                        } else if (args[0].equalsIgnoreCase("version")) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m==========================="));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&liSpy &8/ &7Version: &a" + this.getDescription().getVersion()));
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m==========================="));
                        } else if (args[0].equalsIgnoreCase("reload")) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("ReloadPlugin")));
                            this.reloadConfig();

                        } else if (args[0].equalsIgnoreCase("enable")) {
                            String state = iSpyEnabled.get(sender);
                            if (state != "enabled") {
                                iSpyEnabled.put(sender, "enabled");
                                String msg = this.getConfig().getString("onEnable");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("%s%s", prefix, msg)));
                            } else {
                                String msg = this.getConfig().getString("alreadyEnabled");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("%s%s", prefix, msg)));
                            }

                        } else if (args[0].equalsIgnoreCase("disable")) {
                            String state = iSpyEnabled.get(sender);
                            if (state != "disabled") {
                                iSpyEnabled.put(sender, "disabled");
                                String msg = this.getConfig().getString("onDisable");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("%s%s", prefix, msg)));
                            } else {
                                String msg = this.getConfig().getString("alreadyDisabled");
                                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("%s%s", prefix, msg)));
                            }

                        } else {
                            args[0].equalsIgnoreCase("signs");
                        }

                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m==========================="));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3&liSpy &8/ &7Version: &a" + this.getDescription().getVersion()));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m==========================="));
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("HelpCommands")));
                    }
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + this.getConfig().getString("DontHavePerm")));
                }
            }
            return true;
        } else {
            return false;
        }
    }
}

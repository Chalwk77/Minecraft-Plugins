package com.jericho.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Msg {
    public static void send(CommandSender sender, String str) {
        send(sender, str, "&a");
    }

    public static void send(CommandSender sender, String str, String prefix) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + str));
    }
}

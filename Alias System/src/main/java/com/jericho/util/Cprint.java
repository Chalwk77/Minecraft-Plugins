package com.jericho.util;

import org.bukkit.Bukkit;

public class Cprint {

    public static void info(String str) {
        Bukkit.getLogger().info(str);
    }

    public static void warning(String str) {
        Bukkit.getLogger().warning(str);
    }

    public static void severe(String str) {
        Bukkit.getLogger().severe(str);
    }
}

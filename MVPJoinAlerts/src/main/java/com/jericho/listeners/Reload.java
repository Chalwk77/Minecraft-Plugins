package com.jericho.listeners;

import com.jericho.Main;
import com.jericho.util.Msg;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {

    public final Main instance;

    public Reload(Main plugin) {
        this.instance = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("mvp_reload")) {
            return false;
        } else if (!sender.hasPermission("mvp.reload")) {
            Msg.send(sender, "&8[&3MVPJoinAlerts&8] &cYou do not have permission to execute that command.");
            return false;
        }
        instance.reloadConfig();
        instance.reloadMessages();
        Msg.send(sender, "&8[&3MVPJoinAlerts&8] &aConfiguration Reloaded!");
        return true;
    }
}

package com.jericho.jericraft.commands;

import com.jericho.jericraft.util.CommandHandler;
import com.jericho.jericraft.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Heal {

    public Heal() {
        new CommandHandler("heal", true) {

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                Player player = (Player) sender;
                player.setHealth(20.0d);
                Msg.send(player, "You have been healed!");
                return true;
            }

            @Override
            public List<String> getAliases() {
                List<String> aliases = new ArrayList<>();
                aliases.add("cure");
                return aliases;
            }

            @Override
            public String getUsage() {
                return "/heal";
            }

            @Override
            public String getDescription() {
                return "Heal yourself!";
            }

            @Override
            public String getPermission() {
                return "jericraft.heal";
            }

            @Override
            public String getPermissionMessage() {
                String node = getPermission();
                return "&cInsufficient Permission.\nYou need the permission &a" + node;
            }


        }.enabledDelay(2);
    }
}

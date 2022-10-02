package com.jericho.jericraft.commands;

import com.jericho.jericraft.util.CommandHandler;
import com.jericho.jericraft.util.Msg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Feed {

    // command, minArguments, maxArguments, playerOnly

    public Feed() {
        new CommandHandler("feed", true) {

            @Override
            public boolean onCommand(CommandSender sender, String[] arguments) {
                Player player = (Player) sender;
                player.setFoodLevel(20);
                Msg.send(player, "You have been fed!");
                return true;
            }

            // Returns a list of active aliases for this command:
            @Override
            public List<String> getAliases() {
                List<String> aliases = new ArrayList<>();
                aliases.add("eat");
                return aliases;
            }

            // Returns the command usage:
            @Override
            public String getUsage() {
                return "/feed";
            }

            // Returns the command description:
            @Override
            public String getDescription() {
                return "Feed yourself!";
            }

            // Returns the command permission node:
            @Override
            public String getPermission() {
                return "jericraft.feed";
            }

            // Returns the command permission message:
            @Override
            public String getPermissionMessage() {
                String node = getPermission();
                return "&cInsufficient Permission.\nYou need the permission &a" + node;
            }

        }.enabledDelay(2);
    }
}

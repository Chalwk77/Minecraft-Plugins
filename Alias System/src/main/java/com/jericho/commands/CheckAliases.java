package com.jericho.commands;

import com.jericho.util.CommandHandler;
import com.jericho.util.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.jericho.Main.database;
import static com.jericho.Main.getInstance;

public class CheckAliases {

    public CheckAliases() {

        String baseCommand = getInstance().getConfig().getString("Base Command");
        new CommandHandler(baseCommand, 1, 1, false) {

            @Override
            public boolean onCommand(CommandSender sender, String[] args) {

                Player player = (Player) sender;
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {
                    Msg.send(player, "&cPlayer not found.");
                    return true;
                }

                String uuid = target.getUniqueId().toString();
                Msg.send(player, "&aAliases for &e" + target.getName() + "&a:");

                for (int i = 0; i < database.length(); i++) {

                    JSONObject obj = database.getJSONObject(i);
                    JSONArray aliases = obj.getJSONArray(uuid);

                    List<Object> aliasesList = aliases.toList();
                    List<List<Object>> aliasesListList = new ArrayList<>();
                    for (int j = 0; j < aliasesList.size(); j += 5) {
                        aliasesListList.add(aliasesList.subList(j, Math.min(j + 5, aliasesList.size())));
                    }
                    for (List<Object> list : aliasesListList) {
                        Msg.send(player, list.toString());
                    }
                }

                return true;
            }

            // Returns a list of active aliases for this command:
            @Override
            public @NotNull List<String> getAliases() {
                List<String> aliases = new ArrayList<>();
                aliases.add("aliases");
                return aliases;
            }

            // Returns the command usage:
            @Override
            public @NotNull String getUsage() {
                return "/alias [name]";
            }

            // Returns the command description:
            @Override
            public @NotNull String getDescription() {
                return "Check a player's aliases!";
            }

            // Returns the command permission node:
            @Override
            public String getPermission() {
                return getInstance().getConfig().getString("Check Permission Node");
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

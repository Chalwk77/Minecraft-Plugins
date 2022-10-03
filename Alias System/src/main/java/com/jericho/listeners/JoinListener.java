package com.jericho.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static com.jericho.Main.database;
import static com.jericho.util.Handler.newAlias;
import static com.jericho.util.Handler.newPlayer;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {

        Player player = event.getPlayer();
        String name = player.getName();
        String uuid = player.getUniqueId().toString();

        if (database.length() == 0) {
            newPlayer(uuid, name);
        } else {

            for (int i = 0; i < database.length(); i++) {

                // Add UUID to database if it doesn't exist:
                if (!database.getJSONObject(i).has(uuid)) {
                    newPlayer(uuid, name);
                    return;
                } else {

                    // Link alias to this UUID in the database if it doesn't exist:
                    JSONObject obj = database.getJSONObject(i);
                    JSONArray aliases = obj.getJSONArray(uuid);
                    if (!aliases.toList().contains(name)) {
                        newAlias(aliases, name);
                        return;
                    }
                }
            }
        }
    }
}

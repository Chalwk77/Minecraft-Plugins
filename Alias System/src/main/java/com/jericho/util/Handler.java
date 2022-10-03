package com.jericho.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static com.jericho.Main.database;
import static com.jericho.util.FileIO.updateDatabase;

public class Handler {

    public static void newPlayer(String uuid, String name) throws IOException {

        JSONObject obj = new JSONObject();
        JSONArray names = new JSONArray();
        names.put(name);

        obj.put(uuid, names);
        database.put(obj);

        updateDatabase(database, "aliases.json");
    }

    public static void newAlias(JSONArray aliases, String name) throws IOException {
        aliases.put(name);
        updateDatabase(database, "aliases.json");
    }
}

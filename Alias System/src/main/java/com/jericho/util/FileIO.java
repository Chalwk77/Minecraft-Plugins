// Copyright (c) 2022, Jericho Crosby <jericho.crosby227@gmail.com>

package com.jericho.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

import static com.jericho.Main.getInstance;

public class FileIO {

    public static String getConfigFolder() {
        return getInstance().getDataFolder().getAbsolutePath();
    }

    //
    // Load a JSON Array from file:
    //
    public static JSONArray loadJSONArray(String file) throws IOException {

        String path = getConfigFolder();

        File f = new File(path, file);
        if (!f.exists()) {
            f.createNewFile();
        }

        BufferedReader reader = new BufferedReader(new FileReader(f));
        String line = reader.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        while (line != null) {
            stringBuilder.append(line);
            line = reader.readLine();
        }
        String content = stringBuilder.toString();
        if (content.equals("")) {
            return new JSONArray();
        } else {
            return new JSONArray(content);
        }
    }

    //
    // Load a JSON Object from file:
    //
    public static JSONObject loadJSONObject(String file) throws IOException {
        String path = getConfigFolder();
        BufferedReader reader = new BufferedReader(new FileReader(path + "\\" + file));
        String line = reader.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        while (line != null) {
            stringBuilder.append(line);
            line = reader.readLine();
        }
        String content = stringBuilder.toString();
        if (content.equals("")) {
            return new JSONObject();
        } else {
            return new JSONObject(content);
        }
    }

    //
    // Write to JSON file:
    //
    public static void updateDatabase(JSONArray json, String file) throws IOException {
        String path = getConfigFolder();
        FileWriter fileWriter = new FileWriter(path + "\\" + file);
        fileWriter.write(json.toString(4));
        fileWriter.flush();
        fileWriter.close();
    }
}

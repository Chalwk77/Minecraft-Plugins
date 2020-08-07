package jericraft.chalwk;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.entity.Player;

public class ispyFiles {
    private final File logFile = new File("commands.log");

    public void logCommand(String command, Player player) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            FileWriter logWriter = new FileWriter(this.logFile, true);
            logWriter.write(String.format("%s: %s used command '%s'%n", dateFormat.format(date), player.getName(), command));
            logWriter.close();
        } catch (IOException var6) {
            var6.printStackTrace();
        }
    }
}

package jericraft.chalwk;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class CoinFlipper implements CommandExecutor {

    private final FileConfiguration config;

    public CoinFlipper(FlipACoin plugin) {
        this.config = plugin.getConfig();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("flip")) {
            if (sender.hasPermission("flipacoin.use")) {

                int headcount = 0;
                int tailcount = 0;
                int flips = config.getInt("flips");
                sender.sendMessage("Flipping " + flips + " times...");

                Random rand = new Random();

                Instant start = Instant.now();
                List<String> list = config.getStringList("output");
                for (int i = 0; i < flips; i++) {
                    int x = rand.nextInt(11);
                    if (x <= 4) {
                        headcount++;
                    } else {
                        tailcount++;
                    }
                }

                Instant end = Instant.now();
                Duration interval = Duration.between(start, end);

                int heads_percent = headcount * 100 / flips;
                int tails_percent = tailcount * 100 / flips;

                for (String str : list) {

                    str = str.replaceAll(Pattern.quote("%flips%"), Integer.toString(flips));
                    str = str.replaceAll(Pattern.quote("%headcount%"), Integer.toString(headcount));
                    str = str.replaceAll(Pattern.quote("%tailcount%"), Integer.toString(tailcount));
                    str = str.replaceAll(Pattern.quote("%heads_percent%"), Integer.toString(heads_percent));
                    str = str.replaceAll(Pattern.quote("%tails_percent%"), Integer.toString(tails_percent));
                    str = str.replaceAll(Pattern.quote("%time%"), String.valueOf(interval));

                    str = ChatColor.translateAlternateColorCodes('&', str);
                    sender.sendMessage(str);
                }
            } else {
                sender.sendMessage("You do not have permission to execute that command!");
            }
        }
        return true;
    }
}

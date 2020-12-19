package jericraft.chalwk;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandClass extends BukkitCommand implements Listener {

    private final EnjinWebstorePerks plugin;
    private final FileConfiguration config;

    public CommandClass(String cmd, EnjinWebstorePerks plugin) {

        super(cmd);
        this.description = "Set your next Spaw  Q34n Point with a custom command!";
        this.usageMessage = "/" + cmd;

        this.plugin = plugin;
        this.config = plugin.getConfig();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (sender instanceof Player) {

            String perm = this.getPermission();
            assert perm != null;

            if (!sender.hasPermission(perm)) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to execute that command");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Seems to be working!");
            }
        } else {
            sender.sendMessage("Only players can execute that command!");
        }
        return true;
    }
}
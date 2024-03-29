package com.jericho.util;

import com.jericho.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandHandler extends BukkitCommand implements CommandExecutor {

    private final int minArguments;
    private final int maxArguments;
    private final boolean playerOnly;
    private List<String> delayedPlayers = null;
    private int delay = 0;

    public CommandHandler(String command) {
        this(command, 0);
    }

    /**
     * @param command    The command name
     * @param playerOnly Whether the command can only be executed by a player
     */
    public CommandHandler(String command, boolean playerOnly) {
        this(command, 0, playerOnly);
    }

    /**
     * @param command          The command name
     * @param requireArguments The amount of arguments required to execute the command
     */
    public CommandHandler(String command, int requireArguments) {
        this(command, requireArguments, requireArguments);
    }

    /**
     * @param command      The command name
     * @param minArguments The minimum amount of arguments required to execute the command
     * @param maxArguments The maximum amount of arguments required to execute the command
     */
    public CommandHandler(String command, int minArguments, int maxArguments) {
        this(command, minArguments, maxArguments, false);
    }

    /**
     * @param command          The command name
     * @param requireArguments The amount of arguments required to execute the command
     * @param playerOnly       Whether the command can only be executed by a player
     */
    public CommandHandler(String command, int requireArguments, boolean playerOnly) {
        this(command, requireArguments, requireArguments, playerOnly);
    }

    /**
     * @param command      The command name
     * @param minArguments The minimum amount of arguments required to execute the command
     * @param maxArguments The maximum amount of arguments required to execute the command
     * @param playerOnly   Whether the command can only be executed by a player
     */
    public CommandHandler(String command, int minArguments, int maxArguments, boolean playerOnly) {
        super(command);

        this.minArguments = minArguments;
        this.maxArguments = maxArguments;
        this.playerOnly = playerOnly;

        CommandMap commandMap = getCommandMap();
        if (commandMap != null) {
            commandMap.register(command, this);
        }
    }

    /**
     * @return The command map
     */
    public CommandMap getCommandMap() {
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                return (CommandMap) field.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param delay Command delay in seconds
     */
    public CommandHandler enabledDelay(int delay) {
        this.delay = delay;
        this.delayedPlayers = new ArrayList<>();
        return this;
    }

    /**
     * @param player The player to remove the delay from
     */
    public void removeDelay(Player player) {
        String uuid = player.getUniqueId().toString();
        this.delayedPlayers.remove(uuid);
    }

    /**
     * @param sender The player to send a message to
     */
    public void sendUsage(CommandSender sender) {
        Msg.send(sender, getUsage());
    }

    /**
     * @param sender The command executor
     * @param args   The command arguments
     * @param alias  The command alias
     */
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (args.length < minArguments || args.length < maxArguments) {
            sendUsage(sender);
            return true;
        }

        if (playerOnly && !(sender instanceof Player)) {
            Msg.send(sender, "&cOnly players can execute this command.");
            return true;
        }

        String permission = getPermission();
        if (permission != null && !sender.hasPermission(permission)) {
            Msg.send(sender, getPermissionMessage());
            return true;
        }

        if (delayedPlayers != null && sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();
            if (delayedPlayers.contains(uuid)) {
                Msg.send(sender, "&cYou must wait " + delay + " seconds before using this command again.");
                return true;
            }
            delayedPlayers.add(uuid);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                delayedPlayers.remove(uuid);
            }, 20L * delay);
        }

        if (!onCommand(sender, args)) {
            sendUsage(sender);
        }

        return true;
    }

    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        return this.onCommand(sender, args);
    }

    public abstract boolean onCommand(CommandSender sender, String[] args);

    public abstract @NotNull String getUsage();

    public abstract @NotNull String getDescription();

    public abstract String getPermission();

    public abstract String getPermissionMessage();
}

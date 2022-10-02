package com.jericho.jericraft.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.jericho.jericraft.Main;
import org.bukkit.event.block.BlockPlaceEvent;

public class TorchHandler implements Listener {

    public TorchHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTorchPlace(BlockPlaceEvent event) {

        Block block = event.getBlock();

        if (block.getType() != Material.TORCH) {
            return;
        }

        String name = event.getPlayer().getName();
        Bukkit.broadcastMessage(name + " placed a torch!");
    }
}

package com.ggbounty.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Reputation is intentionally not changed for block breaking.
        // Only mob/player-based interactions should affect reputation.
    }
}

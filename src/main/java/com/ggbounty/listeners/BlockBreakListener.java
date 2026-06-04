package com.ggbounty.listeners;

import com.ggbounty.GGBountyPlugin;
import com.ggbounty.managers.ReputationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Set;

public class BlockBreakListener implements Listener {
    private final GGBountyPlugin plugin;
    private final ReputationManager reputationManager;

    public BlockBreakListener(GGBountyPlugin plugin, ReputationManager reputationManager) {
        this.plugin = plugin;
        this.reputationManager = reputationManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Set<String> protectedWorlds = Set.copyOf(plugin.getConfig().getStringList("settings.protected-worlds"));
        if (protectedWorlds.contains(player.getWorld().getName())) {
            reputationManager.addReputation(player.getUniqueId(), plugin.getConfig().getInt("reputation.grief-damage", -25));
        }
    }
}

package com.ggbounty.listeners;

import com.ggbounty.GGBountyPlugin;
import com.ggbounty.managers.ReputationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class PlayerHealListener implements Listener {
    private final GGBountyPlugin plugin;
    private final ReputationManager reputationManager;

    public PlayerHealListener(GGBountyPlugin plugin, ReputationManager reputationManager) {
        this.plugin = plugin;
        this.reputationManager = reputationManager;
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player healer)) return;
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) return;
        reputationManager.addReputation(healer.getUniqueId(), plugin.getConfig().getInt("reputation.healing-gain", 2));
    }
}

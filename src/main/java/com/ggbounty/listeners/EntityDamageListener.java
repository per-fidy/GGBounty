package com.ggbounty.listeners;

import com.ggbounty.GGBountyPlugin;
import com.ggbounty.managers.ReputationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener {
    private final GGBountyPlugin plugin;
    private final ReputationManager reputationManager;

    public EntityDamageListener(GGBountyPlugin plugin, ReputationManager reputationManager) {
        this.plugin = plugin;
        this.reputationManager = reputationManager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker) || !(event.getEntity() instanceof Player victim)) return;
        if (reputationManager.getReputation(victim.getUniqueId()) < 0) {
            reputationManager.addReputation(attacker.getUniqueId(), 2);
        } else {
            reputationManager.addReputation(attacker.getUniqueId(), -1);
        }
    }
}

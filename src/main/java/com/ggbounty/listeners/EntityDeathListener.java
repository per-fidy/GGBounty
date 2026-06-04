package com.ggbounty.listeners;

import com.ggbounty.GGBountyPlugin;
import com.ggbounty.managers.BountyManager;
import com.ggbounty.managers.ReputationManager;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {
    private final GGBountyPlugin plugin;
    private final ReputationManager reputationManager;
    private final BountyManager bountyManager;

    public EntityDeathListener(GGBountyPlugin plugin, ReputationManager reputationManager, BountyManager bountyManager) {
        this.plugin = plugin;
        this.reputationManager = reputationManager;
        this.bountyManager = bountyManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Player killer = entity instanceof org.bukkit.entity.LivingEntity living && living.getKiller() != null ? living.getKiller() : null;
        if (killer == null) return;

        if (entity instanceof Player victim) {
            int gain = reputationManager.getReputation(victim.getUniqueId());
            int delta = gain < 0 ? 8 : 5;
            reputationManager.addReputation(killer.getUniqueId(), delta);
            if (reputationManager.getReputation(victim.getUniqueId()) < plugin.getConfig().getInt("reputation.bounty-threshold", 100)) {
                bountyManager.createBounty(victim.getUniqueId(), killer.getUniqueId());
                killer.sendTitle("§6NEW BOUNTY", "§7Check /bounty", 10, 40, 10);
                String soundName = plugin.getConfig().getString("settings.bounty-alert-sound", "ENTITY_PLAYER_LEVELUP");
                Sound sound = Registry.SOUNDS.get(NamespacedKey.fromString(soundName));
                if (sound != null) {
                    killer.playSound(killer.getLocation(), sound, 1f, 1f);
                }
            }
            if (bountyManager.hasBounty(victim.getUniqueId()) && bountyManager.isAccepted(victim.getUniqueId(), killer.getUniqueId())) {
                int reward = bountyManager.getReward(victim.getUniqueId());
                reputationManager.addReputation(killer.getUniqueId(), reward / 10);
                killer.sendMessage("§aBounty reward claimed: " + reward);
                bountyManager.removeBounty(victim.getUniqueId());
            }
            return;
        }

        reputationManager.addReputation(killer.getUniqueId(), plugin.getConfig().getInt("reputation.mob-kill-gain", 1));
    }
}

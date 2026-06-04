package com.ggbounty.managers;

import com.ggbounty.GGBountyPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BountyManager {
    private final GGBountyPlugin plugin;
    private final ReputationManager reputationManager;
    private final File dataFile;
    private final YamlConfiguration data;
    private final Map<UUID, Long> claimCooldowns = new HashMap<>();

    public BountyManager(GGBountyPlugin plugin, ReputationManager reputationManager) {
        this.plugin = plugin;
        this.reputationManager = reputationManager;
        this.dataFile = new File(plugin.getDataFolder(), "bounties.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
        }
        this.data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public boolean hasBounty(UUID target) {
        return data.contains("bounties." + target);
    }

    public int getReward(UUID target) {
        return data.getInt("bounties." + target + ".reward", 0);
    }

    public UUID getIssuer(UUID target) {
        String issuer = data.getString("bounties." + target + ".issuer", "");
        return issuer.isEmpty() ? null : UUID.fromString(issuer);
    }

    public void createBounty(UUID target, UUID issuer) {
        int base = Math.max(10, Math.abs(reputationManager.getReputation(target)) * 2);
        data.set("bounties." + target + ".reward", base);
        data.set("bounties." + target + ".issuer", issuer.toString());
        save();
    }

    public void acceptBounty(UUID target, UUID accepter) {
        data.set("bounties." + target + ".acceptedBy", accepter.toString());
        save();
    }

    public boolean isAccepted(UUID target, UUID accepter) {
        return Objects.equals(data.getString("bounties." + target + ".acceptedBy", ""), accepter.toString());
    }

    public void removeBounty(UUID target) {
        data.set("bounties." + target, null);
        save();
    }

    public boolean canClaim(UUID player) {
        long cooldown = plugin.getConfig().getLong("settings.bounty-claim-cooldown-seconds", 30L) * 1000L;
        long last = claimCooldowns.getOrDefault(player, 0L);
        return System.currentTimeMillis() - last >= cooldown;
    }

    public void markClaim(UUID player) {
        claimCooldowns.put(player, System.currentTimeMillis());
    }

    public Map<UUID, Integer> getActiveBounties() {
        Map<UUID, Integer> result = new LinkedHashMap<>();
        if (data.contains("bounties")) {
            for (String key : data.getConfigurationSection("bounties").getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                result.put(uuid, data.getInt("bounties." + key + ".reward", 0));
            }
        }
        return result;
    }

    private void save() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save bounty data: " + e.getMessage());
        }
    }
}

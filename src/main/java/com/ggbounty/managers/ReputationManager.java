package com.ggbounty.managers;

import com.ggbounty.GGBountyPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ReputationManager {
    private final GGBountyPlugin plugin;
    private final File dataFile;
    private final YamlConfiguration data;

    public ReputationManager(GGBountyPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "reputations.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
        }
        this.data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public int getReputation(UUID uuid) {
        return data.getInt("players." + uuid, plugin.getConfig().getInt("reputation.start", 0));
    }

    public void setReputation(UUID uuid, int amount) {
        data.set("players." + uuid, amount);
        save();
    }

    public void addReputation(UUID uuid, int amount) {
        setReputation(uuid, getReputation(uuid) + amount);
    }

    public void save() {
        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save reputation data: " + e.getMessage());
        }
    }

    public List<Map.Entry<UUID, Integer>> getTopPlayers(int limit) {
        if (data.getConfigurationSection("players") == null) {
            return List.of();
        }
        return data.getConfigurationSection("players").getKeys(false).stream()
                .map(UUID::fromString)
                .map(uuid -> Map.entry(uuid, getReputation(uuid)))
                .sorted(Map.Entry.<UUID, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toList());
    }
}

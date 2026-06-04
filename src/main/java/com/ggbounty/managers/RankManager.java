package com.ggbounty.managers;

import com.ggbounty.GGBountyPlugin;

public class RankManager {
    private final GGBountyPlugin plugin;

    public RankManager(GGBountyPlugin plugin) {
        this.plugin = plugin;
    }

    public String getRankName(int reputation) {
        int saint = plugin.getConfig().getInt("reputation.ranks.saint", 100);
        int hero = plugin.getConfig().getInt("reputation.ranks.hero", 50);
        int neutral = plugin.getConfig().getInt("reputation.ranks.neutral", 0);
        int outlaw = plugin.getConfig().getInt("reputation.ranks.outlaw", -50);

        if (reputation >= saint) return "Saint";
        if (reputation >= hero) return "Hero";
        if (reputation >= neutral) return "Neutral";
        if (reputation >= outlaw) return "Outlaw";
        return "Villain";
    }

    public int getThreshold(String rank) {
        return switch (rank.toLowerCase()) {
            case "saint" -> plugin.getConfig().getInt("reputation.ranks.saint", 100);
            case "hero" -> plugin.getConfig().getInt("reputation.ranks.hero", 50);
            case "neutral" -> plugin.getConfig().getInt("reputation.ranks.neutral", 0);
            case "outlaw" -> plugin.getConfig().getInt("reputation.ranks.outlaw", -50);
            case "villain" -> plugin.getConfig().getInt("reputation.ranks.villain", -100);
            default -> 0;
        };
    }
}

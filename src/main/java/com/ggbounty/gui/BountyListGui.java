package com.ggbounty.gui;

import com.ggbounty.GGBountyPlugin;
import com.ggbounty.managers.BountyManager;
import com.ggbounty.managers.RankManager;
import com.ggbounty.managers.ReputationManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public final class BountyListGui {
    private static final int PAGE_SIZE = 45;

    public static void open(Player player, GGBountyPlugin plugin) {
        BountyManager bountyManager = plugin.getBountyManager();
        ReputationManager reputationManager = plugin.getReputationManager();
        RankManager rankManager = plugin.getRankManager();

        Map<UUID, Integer> targets = bountyManager.getActiveBounties();
        List<UUID> entries = new ArrayList<>(targets.keySet());
        int page = 1;
        int totalPages = Math.max(1, (int) Math.ceil(entries.size() / (double) PAGE_SIZE));

        Inventory inv = Bukkit.createInventory(null, 54, "§6Bounty Targets §8(Page 1 / " + totalPages + ")");
        fillPage(inv, page, entries, bountyManager, reputationManager, rankManager, plugin, totalPages);
        player.openInventory(inv);
    }

    private static void fillPage(Inventory inv, int page, List<UUID> entries, BountyManager bountyManager,
                                 ReputationManager reputationManager, RankManager rankManager,
                                 GGBountyPlugin plugin, int totalPages) {
        inv.clear();
        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(entries.size(), start + PAGE_SIZE);

        for (int i = start; i < end; i++) {
            UUID target = entries.get(i);
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            OfflinePlayer offline = Bukkit.getOfflinePlayer(target);
            meta.setOwningPlayer(offline);
            meta.setDisplayName("§e" + (offline.getName() == null ? target.toString() : offline.getName()));
            List<String> lore = new ArrayList<>();
            lore.add("§7Reputation: §f" + reputationManager.getReputation(target));
            lore.add("§7Rank: §f" + rankManager.getRankName(reputationManager.getReputation(target)));
            lore.add("§7Reward: §f" + bountyManager.getReward(target));
            lore.add("§8Click to accept bounty contract.");
            meta.setLore(lore);
            skull.setItemMeta(meta);
            inv.addItem(skull);
        }

        ItemStack next = new ItemStack(Material.ARROW);
        ItemMeta nextMeta = next.getItemMeta();
        nextMeta.setDisplayName("§aNext Page");
        next.setItemMeta(nextMeta);
        inv.setItem(53, next);

        ItemStack back = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName("§cClose");
        back.setItemMeta(backMeta);
        inv.setItem(49, back);
    }
}

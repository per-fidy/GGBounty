package com.ggbounty.commands;

import com.ggbounty.GGBountyPlugin;
import com.ggbounty.managers.RankManager;
import com.ggbounty.managers.ReputationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReputationCommand implements CommandExecutor {
    private final GGBountyPlugin plugin;
    private final ReputationManager reputationManager;
    private final RankManager rankManager;

    public ReputationCommand(GGBountyPlugin plugin, ReputationManager reputationManager, RankManager rankManager) {
        this.plugin = plugin;
        this.reputationManager = reputationManager;
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use /rep.");
            return true;
        }
        int rep = reputationManager.getReputation(player.getUniqueId());
        String rank = rankManager.getRankName(rep);
        player.sendMessage(plugin.getConfig().getString("messages.rep-format", "&7Reputation: &f%rep% &8Rank: &f%rank%")
                .replace("%rep%", String.valueOf(rep))
                .replace("%rank%", rank)
                .replace('&', '§'));
        return true;
    }
}

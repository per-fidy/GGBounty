package com.ggbounty.commands;

import com.ggbounty.GGBountyPlugin;
import com.ggbounty.gui.BountyListGui;
import com.ggbounty.managers.BountyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyCommand implements CommandExecutor {
    private final GGBountyPlugin plugin;
    private final BountyManager bountyManager;
    public BountyCommand(GGBountyPlugin plugin, BountyManager bountyManager) {
        this.plugin = plugin;
        this.bountyManager = bountyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use bounty commands.");
            return true;
        }
        if (args.length == 0) {
            player.sendMessage("Usage: /bounty list | accept <player> | claim");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "list" -> {
                BountyListGui.open(player, plugin);
                return true;
            }
            case "accept" -> {
                if (args.length < 2) {
                    player.sendMessage("Usage: /bounty accept <player>");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage("Player not found.");
                    return true;
                }
                bountyManager.acceptBounty(target.getUniqueId(), player.getUniqueId());
                player.sendMessage("§aBounty accepted for " + target.getName());
                return true;
            }
            case "claim" -> {
                if (!bountyManager.canClaim(player.getUniqueId())) {
                    player.sendMessage("§cPlease wait before claiming another bounty reward.");
                    return true;
                }
                if (bountyManager.hasBounty(player.getUniqueId())) {
                    int reward = bountyManager.getReward(player.getUniqueId());
                    bountyManager.markClaim(player.getUniqueId());
                    bountyManager.removeBounty(player.getUniqueId());
                    player.sendMessage("§aClaimed bounty reward: " + reward + " (configurable economy hook can be added by your server).");
                } else {
                    player.sendMessage("§cNo bounty reward to claim.");
                }
                return true;
            }
            default -> {
                player.sendMessage("Unknown bounty subcommand.");
                return true;
            }
        }
    }
}

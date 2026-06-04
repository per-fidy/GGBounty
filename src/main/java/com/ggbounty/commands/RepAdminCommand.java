package com.ggbounty.commands;

import com.ggbounty.GGBountyPlugin;
import com.ggbounty.managers.ReputationManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RepAdminCommand implements CommandExecutor {
    private final GGBountyPlugin plugin;
    private final ReputationManager reputationManager;

    public RepAdminCommand(GGBountyPlugin plugin, ReputationManager reputationManager) {
        this.plugin = plugin;
        this.reputationManager = reputationManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ggbounty.admin")) {
            sender.sendMessage("§cYou do not have permission to manage reputation.");
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage("Usage: /repadmin <set|add> <player> <amount>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }
        UUID uuid = target.getUniqueId();
        int amount = Integer.parseInt(args[2]);
        if (args[0].equalsIgnoreCase("set")) {
            reputationManager.setReputation(uuid, amount);
        } else if (args[0].equalsIgnoreCase("add")) {
            reputationManager.addReputation(uuid, amount);
        } else {
            sender.sendMessage("Unknown subcommand.");
            return true;
        }
        sender.sendMessage("§aUpdated reputation for " + target.getName());
        return true;
    }
}

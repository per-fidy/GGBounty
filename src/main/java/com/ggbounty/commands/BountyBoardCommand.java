package com.ggbounty.commands;

import com.ggbounty.GGBountyPlugin;
import com.ggbounty.managers.HologramManager;
import com.ggbounty.managers.RankManager;
import com.ggbounty.managers.ReputationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BountyBoardCommand implements CommandExecutor {
    private final GGBountyPlugin plugin;
    private final HologramManager hologramManager;
    private final ReputationManager reputationManager;
    private final RankManager rankManager;

    public BountyBoardCommand(GGBountyPlugin plugin, HologramManager hologramManager, ReputationManager reputationManager, RankManager rankManager) {
        this.plugin = plugin;
        this.hologramManager = hologramManager;
        this.reputationManager = reputationManager;
        this.rankManager = rankManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can manage bounty boards.");
            return true;
        }
        if (!player.hasPermission("ggbounty.board.manage")) {
            player.sendMessage("§cYou do not have permission to manage bounty boards.");
            return true;
        }
        if (args.length < 2) {
            player.sendMessage("Usage: /bountyboard create <high|low> | /bountyboard delete");
            return true;
        }
        if (args[0].equalsIgnoreCase("create") && args.length >= 2) {
            hologramManager.createBoard(player.getLocation(), args[1]);
            player.sendMessage("§aCreated a " + args[1].toUpperCase() + " bounty board at your location.");
            return true;
        }
        if (args[0].equalsIgnoreCase("delete")) {
            hologramManager.getBoards().forEach(board -> {
                if (board.location().getWorld().equals(player.getWorld()) && board.location().distanceSquared(player.getLocation()) < 25) {
                    hologramManager.deleteBoard(board.id());
                }
            });
            player.sendMessage("§aDeleted nearby bounty board(s).");
        }
        return true;
    }
}

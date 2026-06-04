package com.ggbounty.managers;

import com.ggbounty.GGBountyPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class HologramManager {
    private final GGBountyPlugin plugin;
    private final ReputationManager reputationManager;
    private final RankManager rankManager;
    private final Map<UUID, BoardData> boards = new LinkedHashMap<>();
    private BukkitTask updateTask;

    public HologramManager(GGBountyPlugin plugin, ReputationManager reputationManager, RankManager rankManager) {
        this.plugin = plugin;
        this.reputationManager = reputationManager;
        this.rankManager = rankManager;
        startAutoUpdate();
    }

    public void createBoard(Location location, String type) {
        UUID id = UUID.randomUUID();
        List<ArmorStand> lines = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, 0.25 * (6 - i), 0), EntityType.ARMOR_STAND);
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setMarker(true);
            stand.setCustomNameVisible(true);
            lines.add(stand);
        }
        boards.put(id, new BoardData(id, location, type.toUpperCase(Locale.ROOT), lines));
        updateBoard(id);
    }

    public void deleteBoard(UUID id) {
        BoardData board = boards.remove(id);
        if (board == null) return;
        for (ArmorStand stand : board.lines()) {
            stand.remove();
        }
    }

    public void clearAll() {
        new ArrayList<>(boards.keySet()).forEach(this::deleteBoard);
        if (updateTask != null) {
            updateTask.cancel();
        }
    }

    private void startAutoUpdate() {
        long interval = plugin.getConfig().getLong("settings.board-update-interval-seconds", 120L) * 20L;
        updateTask = Bukkit.getScheduler().runTaskTimer(plugin, this::updateAll, interval, interval);
    }

    private void updateAll() {
        for (UUID id : new ArrayList<>(boards.keySet())) {
            updateBoard(id);
        }
    }

    private void updateBoard(UUID id) {
        BoardData board = boards.get(id);
        if (board == null) return;
        Location base = board.location().clone();
        String header = board.type().equals("HIGH") ? "§6Top Reputation" : "§cLow Reputation";
        board.lines().get(0).customName(Component.text(header));

        List<Map.Entry<UUID, Integer>> ranked = new ArrayList<>(reputationManager.getTopPlayers(5));
        if (board.type().equals("LOW")) {
            ranked.sort(Comparator.comparingInt(Map.Entry::getValue));
        }

        for (int i = 0; i < 5; i++) {
            String line = "§7";
            if (i < ranked.size()) {
                UUID uuid = ranked.get(i).getKey();
                int rep = ranked.get(i).getValue();
                String name = Bukkit.getOfflinePlayer(uuid).getName() != null ? Bukkit.getOfflinePlayer(uuid).getName() : "Unknown";
                line = "§e" + (i + 1) + ". §f" + name + " §8(" + rep + " / " + rankManager.getRankName(rep) + ")";
            }
            board.lines().get(i + 1).customName(Component.text(line));
            board.lines().get(i + 1).teleport(base.clone().add(0, 0.25 * (5 - i), 0));
        }
    }

    public Collection<BoardData> getBoards() {
        return boards.values();
    }

    public record BoardData(UUID id, Location location, String type, List<ArmorStand> lines) {}
}

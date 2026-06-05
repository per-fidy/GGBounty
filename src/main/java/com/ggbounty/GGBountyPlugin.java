package com.ggbounty;

import com.ggbounty.commands.BountyCommand;
import com.ggbounty.commands.RepAdminCommand;
import com.ggbounty.commands.ReputationCommand;
import com.ggbounty.commands.BountyBoardCommand;
import com.ggbounty.listeners.EntityDeathListener;
import com.ggbounty.listeners.EntityDamageListener;
import com.ggbounty.listeners.GuiClickListener;
import com.ggbounty.listeners.PlayerHealListener;
import com.ggbounty.managers.BountyManager;
import com.ggbounty.managers.HologramManager;
import com.ggbounty.managers.ReputationManager;
import com.ggbounty.managers.RankManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class GGBountyPlugin extends JavaPlugin {
    private ReputationManager reputationManager;
    private RankManager rankManager;
    private BountyManager bountyManager;
    private HologramManager hologramManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.reputationManager = new ReputationManager(this);
        this.rankManager = new RankManager(this);
        this.bountyManager = new BountyManager(this, reputationManager);
        this.hologramManager = new HologramManager(this, reputationManager, rankManager);

        registerCommands();
        registerListeners();

        getLogger().info("GGBounty enabled with modular reputation, bounty, and hologram systems.");
    }

    @Override
    public void onDisable() {
        if (hologramManager != null) {
            hologramManager.clearAll();
        }
        getLogger().info("GGBounty disabled.");
    }

    private void registerCommands() {
        getCommand("rep").setExecutor(new ReputationCommand(this, reputationManager, rankManager));
        getCommand("bounty").setExecutor(new BountyCommand(this, bountyManager));
        getCommand("bountyboard").setExecutor(new BountyBoardCommand(this, hologramManager, reputationManager, rankManager));
        getCommand("repadmin").setExecutor(new RepAdminCommand(this, reputationManager));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new EntityDeathListener(this, reputationManager, bountyManager), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this, reputationManager), this);
        getServer().getPluginManager().registerEvents(new PlayerHealListener(this, reputationManager), this);
        getServer().getPluginManager().registerEvents(new GuiClickListener(this), this);
    }

    public ReputationManager getReputationManager() { return reputationManager; }
    public RankManager getRankManager() { return rankManager; }
    public BountyManager getBountyManager() { return bountyManager; }
    public HologramManager getHologramManager() { return hologramManager; }
}

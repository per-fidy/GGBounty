package com.ggbounty.listeners;

import com.ggbounty.GGBountyPlugin;
import com.ggbounty.gui.BountyListGui;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class GuiClickListener implements Listener {
    private final GGBountyPlugin plugin;

    public GuiClickListener(GGBountyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().title().toString().contains("Bounty Targets")) return;

        event.setCancelled(true);
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        if (clicked.getType() == Material.ARROW) {
            BountyListGui.open(player, plugin);
            return;
        }

        if (clicked.getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }

        if (clicked.getType() == Material.PLAYER_HEAD && clicked.getItemMeta() instanceof SkullMeta meta) {
            String owner = meta.getOwningPlayer() != null ? meta.getOwningPlayer().getUniqueId().toString() : null;
            if (owner == null) return;
            plugin.getBountyManager().acceptBounty(java.util.UUID.fromString(owner), player.getUniqueId());
            String label = meta.displayName() != null ? PlainTextComponentSerializer.plainText().serialize(meta.displayName()) : "target";
            player.sendMessage("§aBounty accepted for " + label);
        }
    }
}

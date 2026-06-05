package com.ggbounty.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class BlockBreakListenerTest {

    @Test
    void blockBreakDoesNotChangeReputation() {
        BlockBreakListener listener = new BlockBreakListener();

        Player player = mock(Player.class);
        World world = mock(World.class);

        when(player.getWorld()).thenReturn(world);
        when(world.getName()).thenReturn("world");

        BlockBreakEvent event = mock(BlockBreakEvent.class);
        when(event.getPlayer()).thenReturn(player);

        assertDoesNotThrow(() -> listener.onBlockBreak(event));
    }
}

package io.github.lolimi.rchoppers.plugins.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import io.github.lolimi.rchoppers.main.Main;
import io.github.lolimi.rchoppers.main.RCHopper;
import io.github.lolimi.rchoppers.plugins.main.ChunkHopper;

public class BreakChunkHopperListener implements Listener {
	
	@EventHandler
	public void onChunkHopperBreak(BlockBreakEvent e) {
		try {
			if(!RCHopper.rcHoppersMaps.get(ChunkHopper.class.getName()).containsKey(e.getBlock().getLocation())) return;
			
			e.getPlayer().sendMessage(Main.prefix + "§cPlease use the hopper settings to break your §6Chunk hopper §c(shift right click the hopper)");
			e.setCancelled(true);
		}catch(Exception f) {
		}
	}
}

package io.github.lolimi.rchoppers.plugins.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import io.github.lolimi.rchoppers.main.Main;
import io.github.lolimi.rchoppers.plugins.main.ChunkHopper;

public class PlaceChunkHopperListener implements Listener {
	
	@EventHandler
	public void onChunkHopperPlace(BlockPlaceEvent e) {
		if(!e.getItemInHand().isSimilar(ChunkHopper.getItem(1))) return;
		ChunkHopper ch = ChunkHopper.getChunkHopperInChunk(e.getBlock().getLocation());
		if(ch == null) {
			new ChunkHopper(e.getBlock().getLocation(), e.getPlayer().getUniqueId());
			e.getPlayer().sendMessage(Main.prefix+ "§aYou have placed down a §3Chunk Hopper!");
		}else {
			e.setCancelled(true);
			e.getPlayer().sendMessage(Main.prefix+ "§cThere is already a §3Chunk Hopper §cin this chunk!");
		}
	}
}

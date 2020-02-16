package io.github.lolimi.rchoppers.plugins.listeners;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import io.github.lolimi.rchoppers.plugins.main.ChunkHopper;

public class PickupItemListener implements Listener {
	
	@EventHandler
	public void onItemDrop(ItemSpawnEvent e) {
		ItemStack droppedItem = e.getEntity().getItemStack();
		ChunkHopper ch = ChunkHopper.getChunkHopperInChunk(e.getLocation());
		if(ch == null) return;
		boolean inFilter = false;
		if(ch.getTier() == 1) {
			inFilter = true;
		}else {
			for(ItemStack i : ch.getFilter()) {
				if(i.getType().equals(droppedItem.getType())) {
					inFilter = true;
					break;
				}
			}
			inFilter = ch.getWhitelist() ? inFilter : !inFilter;
		}
		if(!inFilter) return;
		int chestNumber = 0;
		Location chestLoc = ch.getLocation().clone();
		for(int i = 1; i<255; i++) {
			Material m = chestLoc.add(0,-1,0).getBlock().getType();
			if(m.equals(Material.CHEST) || m.equals(Material.TRAPPED_CHEST) || 
					m.equals(Material.getMaterial("BARREL")) || m.equals(Material.getMaterial("SHULKER_BOX"))) chestNumber++;
			else break;
		}
		chestLoc.add(0,1,0);
		
		Container cont = null;
		for(int i = chestNumber; i>=0; i--) {
			cont = (Container) chestLoc.getBlock().getState();
			HashMap<Integer, ItemStack> remaining = cont.getInventory().addItem(droppedItem);
			if(remaining.isEmpty()) {
				e.setCancelled(true);
				break;
			}
			droppedItem = remaining.get(0);
			if(i == chestNumber) {
				e.getEntity().getItemStack().setAmount(droppedItem.getAmount());
			}
			chestLoc.add(0,1,0);
		}
	}

}

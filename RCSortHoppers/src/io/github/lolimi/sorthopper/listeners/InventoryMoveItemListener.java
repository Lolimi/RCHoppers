package io.github.lolimi.sorthopper.listeners;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.lolimi.rchoppers.main.RCHopper;
import io.github.lolimi.sorthopper.main.SortingHopper;

public class InventoryMoveItemListener implements Listener {
	
	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent e) {
		if(!e.getDestination().getType().equals(InventoryType.HOPPER)) return;
		if(!RCHopper.rcHoppersMaps.get(SortingHopper.class.getName()).containsKey(e.getDestination().getLocation())) return;
		SortingHopper sh = (SortingHopper) RCHopper.rcHoppersMaps.get(SortingHopper.class.getName()).get(e.getDestination().getLocation());	
		if(sh == null || !sh.exists()) return;
		ItemStack item = e.getItem();
		Container notSorted;
		if(!item.isSimilar(sh.getItem())) {
			Hopper h = (Hopper) e.getDestination().getLocation().getBlock().getBlockData();
			try {
				notSorted = (Container) sh.getLocation().clone().add(h.getFacing().getModX(), h.getFacing().getModY(), h.getFacing().getModZ()).getBlock().getState();
				if(notSorted.getLocation().equals(e.getSource().getLocation())) {
					e.setCancelled(true);
					return;
				}
			}catch(ClassCastException f) {
				e.setCancelled(true);
				return;
			}
			InventoryMoveItemEvent event = new InventoryMoveItemEvent(e.getSource(), item, notSorted.getInventory(), true);
			if(!event.isCancelled()) {
				HashMap<Integer, ItemStack> remaining = notSorted.getInventory().addItem(item);
				if(!remaining.isEmpty()) {
					e.getSource().addItem(remaining.get(0));
				}
			}else {
				e.setCancelled(true);
			}
			
		}else {
			Container c;
			try {
				c = (Container) sh.getLocation().clone().add(0,-1,0).getBlock().getState();
			}catch(ClassCastException f) {
				e.setCancelled(true);
				return;
			}
			
			sort(c, item, e.getDestination(), e.getSource());			
			
		}
		
	}
	
	private void sort(Container c, ItemStack item, Inventory dest, Inventory sor) {
		int chestNumber = 0;
		Location chestLoc = c.getLocation().clone();
		for (int i = 1; i < 255; i++) {
			Material m = chestLoc.getBlock().getType();
			if (m.equals(Material.CHEST) || m.equals(Material.TRAPPED_CHEST) || m.equals(Material.BARREL)
					|| m.equals(Material.SHULKER_BOX)) {
				chestNumber++;
				chestLoc.add(0, -1, 0);
			} else {
				chestLoc.add(0, 1, 0);
				break;
			}
		}
		if(chestNumber == 0) {
			c.getInventory().addItem(item);
			
		}else {
			for(int i = chestNumber; i>=0; i--) {
				HashMap<Integer, ItemStack> remaining = ((Container) chestLoc.getBlock().getState()).getInventory().addItem(item);
				if(remaining.isEmpty()) {
					break;
				}
				item = remaining.get(0);
				if(i == 1) {
					sor.addItem(item);
				}
				chestLoc.add(0,1,0);
			}
			
		}
		
	}
	
	
	

}

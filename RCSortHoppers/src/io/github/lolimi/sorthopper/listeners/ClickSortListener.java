package io.github.lolimi.sorthopper.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.block.data.type.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.lolimi.rchoppers.main.RCHopper;
import io.github.lolimi.sorthopper.main.SortingHopper;

public class ClickSortListener implements Listener {
	
	@EventHandler
	public void onClickSort(InventoryClickEvent e) {
		if(!e.getClickedInventory().getType().equals(InventoryType.HOPPER)) return;
		if(!RCHopper.rcHoppersMaps.get(SortingHopper.class.getName()).containsKey(e.getInventory().getLocation())) return;
		SortingHopper sh = (SortingHopper) RCHopper.rcHoppersMaps.get(SortingHopper.class.getName()).get(e.getInventory().getLocation());	
		if(sh == null || !sh.exists()) return;
		
		ItemStack item = e.getCursor();
		Container c;
		if(!item.isSimilar(sh.getItem())) {
			Hopper h = (Hopper) e.getClickedInventory().getHolder();
			try {
				c = (Container) sh.getLocation().clone().add(h.getFacing().getModX(), h.getFacing().getModY(), h.getFacing().getModZ()).getBlock().getState();
			}catch(ClassCastException f) {
				e.setCancelled(true);
				return;
			}
			if(!sort(c, item, e.getClickedInventory(), false)) 
				e.setCancelled(true);
			return;
		}else {
			try {
				c = (Container) sh.getLocation().clone().add(0,-1,0).getBlock().getState();
			}catch(ClassCastException f) {
				e.setCancelled(true);
				return;
			}
			
			if(!sort(c, item, e.getClickedInventory(), true)) {
				e.setCancelled(true);
			}
		}
	}
	
	private boolean sort(Container c, ItemStack item, Inventory shInv, boolean inFilter){
		if(inFilter) {
			int chestNumber = 0;
			Location chestLoc = c.getLocation().clone();
			for (int i = 1; i < 255; i++) {
				Material m = chestLoc.getBlock().getType();
				if (m.equals(Material.CHEST) || m.equals(Material.TRAPPED_CHEST) || m.equals(Material.BARREL)
						|| m.equals(Material.SHULKER_BOX))
					chestNumber++;
				else
					break;
			}
			if(chestNumber == 0) {
				return false;
			}
			chestLoc.add(0, 1, 0);
			for (int i = chestNumber; i >= 0; i--) {
				c = (Container) chestLoc.getBlock().getState();
				InventoryMoveItemEvent e2 = new InventoryMoveItemEvent(shInv, item, c.getInventory(), true);
				if(e2.isCancelled()) {
					continue;
				}
				ItemStack remaining = c.getInventory().addItem(item).get(0);
				if (remaining == null) {
					return true;
				}
				item = remaining;
				if (i == chestNumber) {
					return false;
				}
				chestLoc.add(0, 1, 0);
			}
			return false;
		}
		
		InventoryMoveItemEvent e2 = new InventoryMoveItemEvent(shInv, item, c.getInventory(), true);
		if(e2.isCancelled()) {
			return false;
		}
		if(c.getInventory().firstEmpty() == -1) {
			return false;
		}
		c.getInventory().addItem(item);
		return true;
	}

}

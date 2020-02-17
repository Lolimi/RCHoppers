package io.github.lolimi.sorthopper.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import io.github.lolimi.rchoppers.main.Main;
import io.github.lolimi.sorthopper.main.SortingHopper;

public class PlaceSortHopperListener implements Listener {
	
	@EventHandler
	public void onSortHopperPlace(BlockPlaceEvent e) {
		if(!e.getItemInHand().isSimilar(SortingHopper.getSorterItem(1))) return;
		SortingHopper sh = new SortingHopper(e.getBlock().getLocation(), e.getPlayer());
		e.getPlayer().sendMessage(Main.prefix+ "§aPlease choose what item should be sorted and thus always go below the §3Sorting Hopper §ayou just placed!");
		e.getPlayer().openInventory(sh.setupInventory());
	}

}

package io.github.lolimi.sorthopper.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import io.github.lolimi.rchoppers.main.RCHopper;
import io.github.lolimi.sorthopper.main.SortingHopper;

public class SetupInventoryClickListener implements Listener {
	
	@EventHandler
	public void onSetupInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory().equals(e.getView().getTopInventory())) return;
		if(!e.getView().getTopInventory().getType().equals(InventoryType.HOPPER)) return;
		try{
			if(!e.getView().getTopInventory().getItem(2).getType().equals(Material.BOOK)) return;
		}catch(NullPointerException f) {
			return;
		}
		ItemStack book = e.getView().getTopInventory().getItem(2);
		if(!book.hasItemMeta()) return;
		if(!book.getItemMeta().getLocalizedName().startsWith("§3SortHopper.DoNothing")) return;
		
		String[] locString = book.getItemMeta().getLocalizedName().split("\\.")[2].split(",");
		Location loc = new Location(Bukkit.getWorld(locString[0]), Double.parseDouble(locString[1]),Double.parseDouble(locString[2]),Double.parseDouble(locString[3]));
		SortingHopper sh = (SortingHopper) RCHopper.rcHoppersMaps.get(SortingHopper.class.getName()).get(loc);
		sh.initialize(e.getCurrentItem());
		e.getWhoClicked().closeInventory();
	}

}

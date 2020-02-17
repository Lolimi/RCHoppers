package io.github.lolimi.sorthopper.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;

import io.github.lolimi.rchoppers.main.RCHopper;
import io.github.lolimi.sorthopper.main.Main;
import io.github.lolimi.sorthopper.main.SortingHopper;

public class NoInteractWithSortHopper implements Listener {
	
	@EventHandler
	public void onShMoveItem(InventoryMoveItemEvent e) {
		if(!e.getSource().getType().equals(InventoryType.HOPPER)) return;
		if(!RCHopper.rcHoppersMaps.get(SortingHopper.class.getName()).containsKey(e.getSource().getLocation())) return;
		SortingHopper sh = (SortingHopper) RCHopper.rcHoppersMaps.get(SortingHopper.class.getName()).get(e.getSource().getLocation());	
		if(sh == null || !sh.exists()) return;
		e.setCancelled(true);
		e.getSource().clear();
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				e.getSource().clear();
			}
		}, 1);
	}
	
	@EventHandler
	public void onShInvClick(InventoryClickEvent e) {
		if(!e.getView().getTopInventory().getLocation().getBlock().getType().equals(Material.HOPPER)) return;
		if(!RCHopper.rcHoppersMaps.get(SortingHopper.class.getName()).containsKey(e.getInventory().getLocation())) return;
		SortingHopper sh = (SortingHopper) RCHopper.rcHoppersMaps.get(SortingHopper.class.getName()).get(e.getInventory().getLocation());	
		if(sh == null || !sh.exists()) return;
		e.setCancelled(true);
		if(e.getCurrentItem().getType() != Material.AIR)
			e.getWhoClicked().sendMessage(io.github.lolimi.rchoppers.main.Main.prefix + "§cYou cannot interact with the §3Sorting Hopper§c!");
	}
	
	@EventHandler
	public void onShPickup(InventoryPickupItemEvent e) {
		if(!e.getInventory().getLocation().getBlock().getType().equals(Material.HOPPER)) return;
		
		if(!RCHopper.rcHoppersMaps.get(SortingHopper.class.getName()).containsKey(e.getInventory().getLocation())) return;
		SortingHopper sh = (SortingHopper) RCHopper.rcHoppersMaps.get(SortingHopper.class.getName()).get(e.getInventory().getLocation());	
		if(sh == null || !sh.exists()) return;
		e.setCancelled(true);
	}

}

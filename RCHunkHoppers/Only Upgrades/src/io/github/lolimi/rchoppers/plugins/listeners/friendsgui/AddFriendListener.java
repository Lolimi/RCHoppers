package io.github.lolimi.rchoppers.plugins.listeners.friendsgui;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import io.github.lolimi.rchoppers.plugins.main.ChunkHopper;

public class AddFriendListener implements Listener {
	
	@EventHandler
	public void onAddFriendClick(InventoryClickEvent e) {
		if(e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta() || e.getCurrentItem().getItemMeta().getLocalizedName() == null) return;
		if(e.getCurrentItem().getItemMeta().getLocalizedName().contains("§1RCH.ChunkHopper.Friends.AddFriend")) {
			String[] l = e.getCurrentItem().getItemMeta().getLocalizedName().split("\\.");
			Location loc = new Location(Bukkit.getWorld(l[4]), Double.parseDouble(l[5]), Double.parseDouble(l[6]), Double.parseDouble(l[7]));
			ChunkHopper ch = ChunkHopper.getChunkHopperInChunk(loc);
			if(!ch.getLocation().equals(loc)) {
				Bukkit.broadcastMessage("something went wrong friends gui");
				return;
			}
			for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
				if(p.getName().equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
					ch.addFriend(p);
					e.getWhoClicked().closeInventory();
					return;
				}
			}
		}
	}

}

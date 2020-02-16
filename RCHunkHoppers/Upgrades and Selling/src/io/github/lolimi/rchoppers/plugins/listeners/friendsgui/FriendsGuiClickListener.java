package io.github.lolimi.rchoppers.plugins.listeners.friendsgui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import io.github.lolimi.rchoppers.main.Main;
import io.github.lolimi.rchoppers.plugins.main.ChunkHopper;

public class FriendsGuiClickListener implements Listener {
	
	@EventHandler
	public void onFriendsGUIClick(InventoryClickEvent e) {
		try {
			if(e.getInventory().getItem(9*5+3) == null) return;
		}catch(ArrayIndexOutOfBoundsException f) {
			return;
		}
		if(!e.getInventory().getItem(9*5+3).hasItemMeta()) return;
		if(!e.getInventory().getItem(9*5+3).getType().equals(Material.BOOK)) return;
		String locName = e.getInventory().getItem(9*5+3).getItemMeta().getLocalizedName();
		if(locName == null) return;
		String[] locNameArray = locName.split("\\.");
		if(!locNameArray[0].equals("§1RCH") || !locNameArray[1].equals("ChunkHopper") || !locNameArray[2].equals("Location")) return;
		e.setCancelled(true);
		World w = Bukkit.getWorld(locNameArray[3]);
		if(w == null) {
			Bukkit.getConsoleSender().sendMessage("§4World is null");
			return;
		}
		try {
			Location loc = new Location(w, Integer.valueOf(locNameArray[4]), Integer.valueOf(locNameArray[5]), Integer.valueOf(locNameArray[6]));
			ChunkHopper ch = ChunkHopper.getChunkHopperInChunk(loc);
			if(!loc.equals(ch.getLocation())) {
				Bukkit.getConsoleSender().sendMessage("§4Location wrong!");
				return;
			}
			switch (e.getCurrentItem().getItemMeta().getLocalizedName()) {
			case "§1RCH.ChunkHopper.Friends.Add":
				ChunkHopper.searchingFriend.put((Player) e.getWhoClicked(), ch);
				e.getWhoClicked().sendMessage(Main.prefix+ "§aType friends name in chat to search");
				e.getWhoClicked().closeInventory();
				break;
			case "§1RCH.ChunkHopper.Friends.Friend":
				ch.rmvFriend(Bukkit.getOfflinePlayer(UUID.fromString(e.getCurrentItem().getItemMeta().getLore().get(0).split(": ")[1])));
				e.setCancelled(true);
				e.getCurrentItem().setAmount(0);
				
				break;
			default:
				break;
			}
		}catch(NumberFormatException f) {
			Bukkit.getConsoleSender().sendMessage("§4NumberFormatException:");
			f.printStackTrace();
			return;
		}catch(NullPointerException f) {
			return;
		}
		
	}
	

}

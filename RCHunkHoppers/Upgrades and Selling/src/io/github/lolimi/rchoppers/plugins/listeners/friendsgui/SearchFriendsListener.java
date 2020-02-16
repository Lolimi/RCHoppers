package io.github.lolimi.rchoppers.plugins.listeners.friendsgui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;

import io.github.lolimi.rchoppers.main.Main;
import io.github.lolimi.rchoppers.plugins.main.ChunkHopper;
import io.github.lolimi.rchoppers.util.ItemBuilder;

public class SearchFriendsListener implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		ArrayList<OfflinePlayer> list = new ArrayList<OfflinePlayer>();
		if(ChunkHopper.searchingFriend.containsKey(e.getPlayer())) {
			
			e.setCancelled(true);
			for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
				if(p.getName().toLowerCase().contains(e.getMessage().toLowerCase())) {
					list.add(p);
				}
			}
			if(list.size() < 1)
				e.getPlayer().sendMessage(Main.prefix+ "§cPlayer not found");
			else {
				Inventory inv = Bukkit.createInventory(null, 9*6, "§3Choose your friend");
				ChunkHopper ch = ChunkHopper.searchingFriend.get(e.getPlayer());
				for(OfflinePlayer p : list) {
					if(p.equals(Bukkit.getOfflinePlayer(ch.getPlacer()))) continue;
					String loc = ch.getLocation().getWorld().getName() + "." + ch.getLocation().getBlockX() + "."+ ch.getLocation().getBlockY() + "." + ch.getLocation().getBlockZ();
					inv.addItem(new ItemBuilder(Material.PLAYER_HEAD).setName(p.getName()).setLocalName("§1RCH.ChunkHopper.Friends.AddFriend." +  loc).build());
				}
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
					e.getPlayer().openInventory(inv);
				}, 1);
				ChunkHopper.searchingFriend.remove(e.getPlayer());
			}
		}
		
	}

}

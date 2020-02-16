package io.github.lolimi.rchoppers.plugins.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import io.github.lolimi.rchoppers.main.RCHopper;
import io.github.lolimi.rchoppers.plugins.main.ChunkHopper;
import io.github.lolimi.rchoppers.plugins.main.GUI;

public class ShiftClickChunkHopperListener implements Listener {
	
	@EventHandler
	public void onShiftClick(PlayerInteractEvent e) {
		if(!e.getPlayer().isSneaking() || !e.getClickedBlock().getType().equals(Material.HOPPER) || !e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
		if(!RCHopper.rcHoppersMaps.get(ChunkHopper.class.getName()).containsKey(e.getClickedBlock().getLocation())) return;
		
		ChunkHopper ch = (ChunkHopper) RCHopper.rcHoppersMaps.getOrDefault(ChunkHopper.class.getName(), null).getOrDefault(e.getClickedBlock().getLocation(), null);
		if(ch == null) return;
		if(!ch.exists()) return;
		boolean friend = ch.getFriends().contains(e.getPlayer());
		e.setCancelled(true);
		if(e.getPlayer().isOp())
			friend = false;
		if(!ch.getPlacer().equals(e.getPlayer().getUniqueId()) && !friend) {
			if(e.getPlayer().isOp())
				e.getPlayer().openInventory(GUI.getSettings(false,ch));
			else {
				e.getPlayer().sendMessage("§cThis is not your §6Chunk Hopper!");
				return;
			}
		}else
			e.getPlayer().openInventory(GUI.getSettings(friend,ch));
		ChunkHopper.settingsGuiOpen.put(e.getPlayer(), ch);
		
	}

}

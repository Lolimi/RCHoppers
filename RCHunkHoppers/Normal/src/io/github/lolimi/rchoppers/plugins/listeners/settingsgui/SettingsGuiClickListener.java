package io.github.lolimi.rchoppers.plugins.listeners.settingsgui;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import io.github.lolimi.rchoppers.main.Main;
import io.github.lolimi.rchoppers.plugins.main.ChunkHopper;
import io.github.lolimi.rchoppers.plugins.main.GUI;

public class SettingsGuiClickListener implements Listener {
	
	@EventHandler
	public void onSettingsGuiClick(InventoryClickEvent e) {
		if(!e.getView().getTitle().equals(Main.getPlugin().getConfig().getString("GUI.Settings.Name", "§3Your Chunk Hopper settings:"))) return;
		if(!ChunkHopper.settingsGuiOpen.containsKey(e.getWhoClicked())) return;
		e.setCancelled(true);
		if(!e.getCurrentItem().hasItemMeta() || e.getCurrentItem().getItemMeta().getLocalizedName() == null) return;
		
		switch (e.getCurrentItem().getItemMeta().getLocalizedName()) {
		case "§1RCH.ChunkHopper.Settings.Friends":
			e.getWhoClicked().openInventory(GUI.getFriends(ChunkHopper.settingsGuiOpen.get(e.getWhoClicked())));
			ChunkHopper.settingsGuiOpen.remove(e.getWhoClicked());
			break;
		case "§1RCH.ChunkHopper.Settings.Filter":
			e.getWhoClicked().openInventory(GUI.getFilter(ChunkHopper.settingsGuiOpen.get(e.getWhoClicked())));
			ChunkHopper.settingsGuiOpen.remove(e.getWhoClicked());
			break;
		case "§1RCH.ChunkHopper.Settings.Remove":
			ChunkHopper ch = ChunkHopper.settingsGuiOpen.get(e.getWhoClicked());
			ch.getLocation().getBlock().setType(Material.AIR);
			ch.remove();
			ChunkHopper.settingsGuiOpen.remove(e.getWhoClicked());
			e.getWhoClicked().sendMessage(Main.prefix+ "§cYou have broken your Chunk Hopper!");
			e.getWhoClicked().closeInventory();
			
			break;
		}
	}

}

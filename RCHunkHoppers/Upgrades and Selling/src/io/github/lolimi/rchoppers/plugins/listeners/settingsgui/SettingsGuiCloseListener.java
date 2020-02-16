package io.github.lolimi.rchoppers.plugins.listeners.settingsgui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import io.github.lolimi.rchoppers.main.Main;
import io.github.lolimi.rchoppers.plugins.main.ChunkHopper;

public class SettingsGuiCloseListener implements Listener {
	
	@EventHandler
	public void onSettingsGuiClose(InventoryCloseEvent e) {
		if(!e.getView().getTitle().equals(Main.getPlugin().getConfig().getString("GUI.Settings.Name", "§3Your Chunk Hopper settings:"))) return;
		if(!ChunkHopper.settingsGuiOpen.containsKey(e.getPlayer())) return;
		ChunkHopper.settingsGuiOpen.remove(e.getPlayer());
	}

}

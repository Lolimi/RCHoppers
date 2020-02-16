package io.github.lolimi.rchoppers.plugins.listeners.settingsgui;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import io.github.lolimi.rchoppers.main.Main;
import io.github.lolimi.rchoppers.plugins.main.ChunkHopper;
import io.github.lolimi.rchoppers.plugins.main.GUI;

public class SettingsGuiClickListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSettingsGuiClick(InventoryClickEvent e) {
		if(!e.getView().getTitle().equals(Main.getPlugin().getConfig().getString("GUI.Settings.Name", "§3Your Chunk Hopper settings:"))) return;
		if(!ChunkHopper.settingsGuiOpen.containsKey(e.getWhoClicked())) return;
		if(!e.getCurrentItem().isSimilar(ChunkHopper.getUpgradeItem(1, 2)) && !e.getCursor().isSimilar(ChunkHopper.getUpgradeItem(1,2)) &&
				!e.getCurrentItem().isSimilar(ChunkHopper.getUpgradeItem(1, 3)) && !e.getCursor().isSimilar(ChunkHopper.getUpgradeItem(1,3)))
			e.setCancelled(true);
		
		if(!e.getCurrentItem().hasItemMeta() || e.getCurrentItem().getItemMeta().getLocalizedName() == null) return;
		ChunkHopper ch = ChunkHopper.settingsGuiOpen.get(e.getWhoClicked());
		
		switch (e.getCurrentItem().getItemMeta().getLocalizedName()) {
		
		case "§1RCH.ChunkHopper.Settings.Friends":
			e.getWhoClicked().openInventory(GUI.getFriends(ch));
			ChunkHopper.settingsGuiOpen.remove(e.getWhoClicked());
			break;
			
		case "§1RCH.ChunkHopper.Settings.Filter":
			e.getWhoClicked().openInventory(GUI.getFilter(ch,false));
			ChunkHopper.settingsGuiOpen.remove(e.getWhoClicked());
			break;
			
		case "§1RCH.ChunkHopper.Settings.SellingFilter":
			e.getWhoClicked().openInventory(GUI.getFilter(ch,true));
			ChunkHopper.settingsGuiOpen.remove(e.getWhoClicked());
			break;
			
		case "§1RCH.ChunkHopper.Settings.Remove":
			ch.getLocation().getBlock().setType(Material.AIR);
			ch.remove();
			ChunkHopper.settingsGuiOpen.remove(e.getWhoClicked());
			e.getWhoClicked().sendMessage(Main.prefix+ "§cYou have broken your Chunk Hopper!");
			e.getWhoClicked().closeInventory();
			break;
			
		case "§1RCH.ChunkHopper.Settings.Upgrade":
			if(e.getCursor().isSimilar(ChunkHopper.getUpgradeItem(1, ch.getTier() + 1))) {
				e.getWhoClicked().getInventory().addItem(ChunkHopper.getUpgradeItem(e.getCursor().getAmount() - 1, ch.getTier() + 1));
				e.setCursor(null);
				e.getWhoClicked().closeInventory();
				e.getWhoClicked().sendMessage(Main.prefix + "§aYou have upgraded your §6Chunk Hopper §ato §clevel "+(ch.getTier()+1) +"§a!");
				ch.upgrade();
				ChunkHopper.settingsGuiOpen.remove(e.getWhoClicked());
				break;
			}
		}
	}

}

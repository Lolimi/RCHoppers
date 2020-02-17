package io.github.lolimi.sorthopper.main;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.lolimi.rchoppers.main.RCHopper;
import io.github.lolimi.rchoppers.util.ItemBuilder;

public class SortingHopper extends RCHopper {
	
	private ItemStack item;
	
	public SortingHopper(Location loc, Player placer) {
		this.location = loc;
		if(placer == null) {
			setConfigFile(Main.getPlugin());
			this.placer = UUID.fromString(config.getString("Placer", null));
			if(this.placer == null) 
				return;
			item = config.getItemStack("Item", null);
		}else {
			super.placer = placer.getUniqueId();
			toSave = false;
			item = null;
		}
		
		register(this, Main.getPlugin());
	}
	
	public void initialize(ItemStack toSort) {
		item = toSort;
		toSave = true;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	@SuppressWarnings("unchecked")
	public static ItemStack getSorterItem(int amount) {
		ItemStack i = new ItemStack(Material.HOPPER);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(Main.getPlugin().getConfig().getString("SortingHopperItem.Name", "§3Sorting Hopper"));
		m.setLore((List<String>) Main.getPlugin().getConfig().getList("SortingHopperItem.Lore", Arrays.asList("§b§lThis is a Sorting Hopper", "§bIt lets you sort your items easier!")));
		m.setLocalizedName("§1ChunkHopper!");
		i.setItemMeta(m);
		i.setAmount(amount);
		return i;
	}
	
	public Inventory setupInventory() {
		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "");
		String loc = String.join(",", location.getWorld().getName(), String.valueOf(location.getBlockX()), String.valueOf(location.getBlockY()),String.valueOf(location.getBlockZ()));
		ItemStack i1 = new ItemBuilder(Material.BOOK).setName("Click any item in your inventory to set the filter to that item type!").setLocalName("§3SortHopper.DoNothing."+loc).build();
		inv.setItem(2, i1);
		
		return inv;
	}

}

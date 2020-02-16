package io.github.lolimi.rchoppers.plugins.main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.lolimi.rchoppers.main.RCHopper;

public class ChunkHopper extends RCHopper {
	
	private int tier;
	public ItemStack[] filter = new ItemStack[45];
	private boolean whitelist;
	
	public static HashMap<Player, ChunkHopper> settingsGuiOpen = new HashMap<Player, ChunkHopper>();
	public static HashMap<Player, ChunkHopper> searchingFriend = new HashMap<Player, ChunkHopper>();
	
	private final FileConfiguration pluginConf = Main.getPlugin().getConfig();

	public ChunkHopper(@Nonnull Location loc, UUID placer) {
		location = loc;
		if(placer == null) {
			setConfigFile(Main.getPlugin());
			this.tier = config.getInt("Tier", -1);
			this.placer = UUID.fromString(config.getString("Placer", null));
			if(this.placer == null) 
				return;
			whitelist = config.getBoolean("Whitelist", false);
			for(int i = 0; i< filter.length; i++) {
				filter[i] = config.getItemStack("Filter."+i, new ItemStack(Material.AIR));
			}
			if(config.getString("Friends") != null) {
				Set<String> amount = config.getConfigurationSection("Friends").getKeys(false);
				for(String i : amount) {
					friends.add(config.getOfflinePlayer("Friends."+Integer.parseInt(i)));
				}
			}
		}else{
			this.tier = 1;
			this.placer = placer;
			whitelist = pluginConf.getBoolean("DefaultFilters.Whitelist.Normal", false);
			for(int i = 0; i< filter.length; i++) {
				filter[i] = new ItemStack(Material.getMaterial(pluginConf.getString("DefaultFilters.Normal." + i, "AIR")));
			}
		}
		register(this, Main.getPlugin());
	}
	
	public int getTier() {
		return tier;
	}
	
	public ItemStack[] getFilter() {
		return filter;
	}
	
	public boolean getWhitelist() {
		return whitelist;
	}
	
	public void changeWhitelist() {
		whitelist = !whitelist;
	}
	
	public static ChunkHopper getChunkHopperInChunk(Location loc) {
		Set<Location> locs = rcHoppersMaps.get(ChunkHopper.class.getName()).keySet();
		for(Location l : locs) {
			if(l.getWorld().equals(loc.getWorld()) && l.getChunk().getX() == loc.getChunk().getX() && l.getChunk().getZ() == loc.getChunk().getZ()) 
				return (ChunkHopper) rcHoppersMaps.get(ChunkHopper.class.getName()).get(l);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static ItemStack getItem(int amount) {
		ItemStack i = new ItemStack(Material.HOPPER);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(Main.getPlugin().getConfig().getString("ChunkHopperItem.Name", "§3ChunkHopper"));
		m.setLore((List<String>) Main.getPlugin().getConfig().getList("ChunkHopperItem.Lore", Arrays.asList("§b§lThis is a Chunk Hopper", "§bIt picks up all the items in a chunk!")));
		m.setLocalizedName("§1ChunkHopper!");
		i.setItemMeta(m);
		i.setAmount(amount);
		return i;
	}
	
	@SuppressWarnings("unchecked")
	public static ItemStack getUpgradeItem(int amount) {
		ItemStack i = new ItemStack(Material.IRON_INGOT);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName(Main.getPlugin().getConfig().getString("UpgradeItem.Name", "§aChunk Hopper Upgrade"));
		m.setLore((List<String>) Main.getPlugin().getConfig().getList("UpgradeItem.Lore", Arrays.asList("§6Tier 1 §a➜ §6Tier 2")));
		m.setLocalizedName("§1ChunkHopperUpgrade!");
		i.setItemMeta(m);
		i.setAmount(amount);
		return i;
	}
	
	public void upgrade() {
		tier = 2;
	}
	
	public void resetFilter() {
		for(int i = 0; i< filter.length; i++) {
			filter[i] = new ItemStack(Material.getMaterial(pluginConf.getString("DefaultFilters.Normal." + i, "AIR")));
		}
		
	}

}

package io.github.lolimi.rchoppers.plugins.main;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import io.github.lolimi.rchoppers.util.ItemBuilder;

public class GUI {

	@SuppressWarnings("unchecked")
	public static Inventory getSettings(boolean friend) {
		FileConfiguration conf = Main.getPlugin().getConfig();
		Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER,
				conf.getString("GUI.Settings.Name", "§3Your Chunk Hopper settings:"));
		if (!friend) {
			ItemStack friends = new ItemBuilder(Material.PLAYER_HEAD)
					.setName(conf.getString("GUI.Settings.Items.FriendList.Name",
							"§2Click here to open your friends list"))
					.setLore(String.join(",",
							((List<String>) conf.getList("GUI.Settings.Items.FriendList.Lore", Arrays.asList(""))))
							.split(","))
					.setLocalName("§1RCH.ChunkHopper.Settings.Friends").build();
			inv.setItem(0, friends);
		}
		ItemStack filter = new ItemBuilder(Material.BOOK)
				.setName(conf.getString("GUI.Settings.Items.Filter.Name", "§aClick here to set your filter"))
				.setLore(String.join(",",
						((List<String>) conf.getList("GUI.Settings.Items.Filter.Lore",
								Arrays.asList("§3You can set a blacklist or a whitelist"))))
						.split(","))
				.setLocalName("§1RCH.ChunkHopper.Settings.Filter").build();
		ItemStack remove = new ItemBuilder(Material.BARRIER)
				.setName(conf.getString("GUI.Settings.Items.Break.Name", "§4Click here to break this Chunk Hopper"))
				.setLore(String
						.join(",", ((List<String>) conf.getList("GUI.Settings.Items.Break.Lore", Arrays.asList(""))))
						.split(","))
				.setLocalName("§1RCH.ChunkHopper.Settings.Remove").build();
		inv.setItem(2, filter);
		inv.setItem(4, remove);

		return inv;
	}

	@SuppressWarnings("unchecked")
	public static Inventory getFriends(ChunkHopper hopper) {
		FileConfiguration conf = Main.getPlugin().getConfig();
		Inventory inv = Bukkit.createInventory(null, 9 * 6,
				conf.getString("GUI.Friends.Name", "§3Your Chunk Hopper friends list:"));
		ItemStack add = new ItemBuilder(Material.EMERALD_BLOCK)
				.setName(conf.getString("GUI.Friends.Items.AddFriend.Name", "§aClick here to add a friend"))
				.setLore(String
						.join(",", ((List<String>) conf.getList("GUI.Friends.Items.AddFriend.Lore", Arrays.asList(""))))
						.split(","))
				.setLocalName("§1RCH.ChunkHopper.Friends.Add").build();
		ItemStack explain = new ItemBuilder(Material.BOOK)
				.setName(conf.getString("GUI.Friends.Items.Explain.Name",
						"§3Players in this list can access your Chunk Hopper settings"))
				.setLore(String.join(",",
						((List<String>) conf.getList("GUI.Friends.Items.Explain.Lore",
								Arrays.asList("§cClick on a skull to remove a friend",
										"§aClick on the Emerald Block to add a friend"))))
						.split(","))
				.setLocalName("§1RCH.ChunkHopper.Location." + hopper.getLocation().getWorld().getName() + "."
						+ hopper.getLocation().getBlockX() + "." + hopper.getLocation().getBlockY() + "."
						+ hopper.getLocation().getBlockZ())
				.build();

		int toAdd = 0;

		for (int i = 0; i < hopper.getFriends().size(); i++) {
			ItemStack it = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta m = (SkullMeta) it.getItemMeta();
			OfflinePlayer p = hopper.getFriends().get(i);
			if (p == null) {
				toAdd--;
				continue;
			}
			m.setOwningPlayer(p);
			m.setLocalizedName("§1RCH.ChunkHopper.Friends.Friend");
			m.setDisplayName("§3" + p.getName());
			m.setLore(Arrays.asList("§aUUID: " + p.getUniqueId().toString()));
			it.setItemMeta(m);
			inv.setItem(i + toAdd, it);
		}

		inv.setItem(9 * 5 + 3, explain);
		inv.setItem(9 * 5 + 5, add);
		return inv;
	}

	@SuppressWarnings("unchecked")
	public static Inventory getFilter(ChunkHopper h) {
		FileConfiguration conf = Main.getPlugin().getConfig();
		Inventory inv = Bukkit.createInventory(null, 9 * 6,
				conf.getString("GUI.Filter.Name", "§3Your Chunk Hopper filter:"));
		ItemStack book = new ItemBuilder(
				Material.BOOK)
						.setName(conf.getString("GUI.Filter.Items.Explain.Book.Name",
								"§3In here you can set a whitelist or a blacklist of max 45 item types."))
						.setLore(String
								.join(",",
										((List<String>) conf.getList("GUI.Filter.Items.Explain.Bool.Lore",
												Arrays.asList("§3Blacklisted items will not get picked up",
														"§3In whitelist mode only set items will get picked up"))))
								.split(","))
						.setLocalName("§1RCH.ChunkHopper.Location." + h.getLocation().getWorld().getName() + "."
								+ h.getLocation().getBlockX() + "." + h.getLocation().getBlockY() + "."
								+ h.getLocation().getBlockZ())
						.build();
		ItemStack add = new ItemBuilder(Material.EMERALD_BLOCK)
				.setName(conf.getString("GUI.Filter.Items.Explain.Emerald.Name", "§2To add an item, click it in your inventory"))
				.setLore(String.join(",",
						((List<String>) conf.getList("GUI.Filter.Items.Explain.Emerald.Lore", Arrays.asList(""))))
						.split(","))
				.setLocalName("§1RCH.ChunkHopper").build();
		ItemStack remove = new ItemBuilder(Material.REDSTONE_BLOCK)
				.setName(conf.getString("GUI.Filter.Items.Explain.Redstone.Name",
						"§4To remove an item, click it in the filter inventory"))
				.setLore(String.join(",",
						((List<String>) conf.getList("GUI.Filter.Items.Explain.Redstone.Lore", Arrays.asList(""))))
						.split(","))
				.setLocalName("§1RCH.ChunkHopper").build();

		ItemStack whitelist;
		if (h.getWhitelist()) {
			whitelist = new ItemBuilder(Material.WHITE_TERRACOTTA).setName("§7Whitelist").setLore(String
					.join(",",
							((List<String>) conf.getList("GUI.Filter.Items.Whitelist.Lore",
									Arrays.asList("§7Only picks up items set up in this filter", "§7Click to change"))))
					.split(",")).setLocalName("§1RCH.ChunkHopper.Filter.Whitelist").build();
		} else {
			whitelist = new ItemBuilder(Material.BLACK_TERRACOTTA).setName("§8Blacklist")
					.setLore(String.join(",",
							((List<String>) conf.getList("GUI.Filter.Items.Blacklist.Lore",
									Arrays.asList("§8Picks up all items but the ones set up in this filter",
											"§8Click to change"))))
							.split(","))
					.setLocalName("§1RCH.ChunkHopper.Filter.Blacklist").build();
		}

		ItemStack reset = new ItemBuilder(Material.BARRIER)
				.setName(conf.getString("GUI.Filter.Items.Reset.Name", "§cTo reset your filter to default click here"))
				.setLore(String
						.join(",", ((List<String>) conf.getList("GUI.Filter.Items.Reset.Lore", Arrays.asList(""))))
						.split(","))
				.setLocalName("§1RCH.ChunkHopper.Filter.Reset").build();

		for (int i = 0; i < h.getFilter().length; i++) {
			if (h.getFilter()[i].getType().equals(Material.AIR))
				continue;
			ItemStack it = h.getFilter()[i];
			ItemMeta m = it.getItemMeta();
			m.setLocalizedName("§1RCH.ChunkHopper.Item");
			it.setItemMeta(m);
			inv.setItem(i, it);
		}

		inv.setItem(9 * 5 + 0, book);
		inv.setItem(9 * 5 + 2, add);
		inv.setItem(9 * 5 + 4, remove);
		inv.setItem(9 * 5 + 6, whitelist);
		inv.setItem(9 * 5 + 8, reset);
		return inv;
	}

}

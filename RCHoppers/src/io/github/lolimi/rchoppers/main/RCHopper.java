package io.github.lolimi.rchoppers.main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class RCHopper {

	protected File configFile;
	public FileConfiguration config;
	protected UUID placer;
	protected Location location;
	protected RCHopper instance;
	protected ArrayList<OfflinePlayer> friends = new ArrayList<OfflinePlayer>();
	public boolean toSave = true;

	public static HashMap<String, HashMap<Location, RCHopper>> rcHoppersMaps = new HashMap<String, HashMap<Location, RCHopper>>();

	public static void registerType(Class<?> c) {
		if (rcHoppersMaps == null)
			rcHoppersMaps = new HashMap<String, HashMap<Location, RCHopper>>();
		rcHoppersMaps.put(c.getName(), new HashMap<Location, RCHopper>());
	}

	public static void registerHoppers(RCHopper[] existing, JavaPlugin plugin) {
		for (RCHopper h : existing) {
			h.register(h, plugin);
		}
	}

	public static void saveAll() {
		if (rcHoppersMaps == null)
			return;
		for (HashMap<Location, RCHopper> ma : rcHoppersMaps.values()) {
			Collection<RCHopper> hoppers = ma.values();
			for (RCHopper h : hoppers) {
				if(h.toSave) h.saveConfig();
			}
		}
	}

	private void saveConfig() {
		rcHoppersMaps.get(instance.getClass().getName()).put(location, instance);
		for (Method m : instance.getClass().getMethods()) {
			if (!m.getName().startsWith("get") || m.getName().startsWith("getLocation")
					|| m.getReturnType().equals(Class.class))
				continue;
			try {
				Class<?> c = m.getReturnType();
				if (c == UUID.class)
					config.set(m.getName().replace("get", ""),
							m.invoke(instance, (Object[]) null).toString());
				else if (c.isArray()) {
					Object[] o = (Object[]) m.invoke(instance, (Object[]) null);
					for (int i = 0; i < o.length; i++) {
						config.set(m.getName().replace("get", "") + "." + i, o[i]);
					}
				} else if (c == ArrayList.class) {
					@SuppressWarnings("unchecked")
					ArrayList<Object> o = (ArrayList<Object>) m.invoke(instance, (Object[]) null);
					config.set(m.getName().replace("get", ""), null);
					for (int i = 0; i < o.size(); i++) {
						config.set(m.getName().replace("get", "") + "." + i, o.get(i));
					}
				} else
					config.set(m.getName().replace("get", ""), m.invoke(instance, (Object[]) null));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NullPointerException e) {
			}
		}
		try {
			config.save(configFile);
		} catch (IOException e) {
		}
	}

	public final Location getLocation() {
		return location;
	}

	public static final Location getLocation(File file) {
		String[] name = file.getName().split("\\.");
		return new Location(Bukkit.getWorld(name[0]), Double.parseDouble(name[1]), Double.parseDouble(name[2]),
				Double.parseDouble(name[3]));
	}

	public final UUID getPlacer() {
		return placer;
	}

	protected void register(RCHopper instance, JavaPlugin plugin) {
		this.instance = instance;
		setConfigFile(plugin);
		saveConfig();
	}

	protected File setConfigFile(JavaPlugin plugin) {
		String name = new String(location.getWorld().getName());
		String dot = new String(".");
		name += dot;
		name += location.getBlockX();
		name += dot;
		name += location.getBlockY();
		name += dot;
		name += location.getBlockZ();
		name += new String(".yml");

		File file = new File(plugin.getDataFolder().getPath() + File.separator + "Data" + File.separator + name);
		configFile = file;
		config = YamlConfiguration.loadConfiguration(file);

		return file;
	}

	public boolean exists() {
		return placer != null || location != null;
	}

	public void remove() {
		configFile.delete();
		rcHoppersMaps.get(instance.getClass().getName()).remove(location);
		placer = null;
		location = null;
	}

	public ArrayList<OfflinePlayer> getFriends() {
		return friends;
	}

	public void addFriend(OfflinePlayer friend) {
		if (!friends.contains(friend))
			;
		friends.add(friend);
	}

	public void rmvFriend(OfflinePlayer notFriend) {
		if (friends.contains(notFriend))
			// friends.set(friends.indexOf(notFriend), null);
			friends.remove(notFriend);
	}

}

package io.github.lolimi.sorthopper.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.lolimi.rchoppers.main.RCHopper;
import io.github.lolimi.sorthopper.commands.GetSortHopperCommand;
import io.github.lolimi.sorthopper.commands.GetSorterTabComplete;
import io.github.lolimi.sorthopper.listeners.ClickSortListener;
import io.github.lolimi.sorthopper.listeners.InventoryMoveItemListener;
import io.github.lolimi.sorthopper.listeners.NoInteractWithSortHopper;
import io.github.lolimi.sorthopper.listeners.PlaceSortHopperListener;
import io.github.lolimi.sorthopper.listeners.SetupInventoryClickListener;

public class Main extends JavaPlugin {
	
	private static Main plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		createConfig();
		registerSortHoppers();
		
		getCommand("gsh").setExecutor(new GetSortHopperCommand());
		getCommand("gsh").setTabCompleter(new GetSorterTabComplete());
		
		PluginManager p = Bukkit.getPluginManager();
		p.registerEvents(new InventoryMoveItemListener(), this);
		p.registerEvents(new NoInteractWithSortHopper(), this);
		p.registerEvents(new ClickSortListener(), this);
		p.registerEvents(new PlaceSortHopperListener(), this);
		p.registerEvents(new SetupInventoryClickListener(), this);
	}
	
	
	public static Main getPlugin() {
		return plugin;
	}
	
	private void registerSortHoppers() {
		File[] files = new File(getDataFolder().getPath()+ File.separator + "Data").listFiles();
		if(files == null) return;
		SortingHopper[] hoppers = new SortingHopper[files.length];
		RCHopper.registerType(SortingHopper.class);
		for(int i = 0; i < files.length; i++) {
			if(files.length == 0) break;
			Location loc = RCHopper.getLocation(files[i]);
			hoppers[i] = new SortingHopper(loc, null);
		}
		RCHopper.registerHoppers(hoppers, this);
	}
	
	private void createConfig() {
		this.getDataFolder().mkdir();
		new File(getDataFolder().getPath()+ File.separator + "Data").mkdir();
		File f = new File(this.getDataFolder() + File.separator + "config.yml");
		if (!f.exists()) {
			try {
				f.createNewFile();
				OutputStream writer = new FileOutputStream(f);
				InputStream out = this.getResource("config.yml");
				byte[] linebuffer = new byte[out.available()];
				out.read(linebuffer);
				writer.write(linebuffer);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

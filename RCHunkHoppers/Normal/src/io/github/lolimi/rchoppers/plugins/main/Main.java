package io.github.lolimi.rchoppers.plugins.main;

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
import io.github.lolimi.rchoppers.plugins.commands.CommandTab;
import io.github.lolimi.rchoppers.plugins.commands.GetChunkHopperCommand;
import io.github.lolimi.rchoppers.plugins.commands.TestCommand;
import io.github.lolimi.rchoppers.plugins.listeners.BreakChunkHopperListener;
import io.github.lolimi.rchoppers.plugins.listeners.PickupItemListener;
import io.github.lolimi.rchoppers.plugins.listeners.PlaceChunkHopperListener;
import io.github.lolimi.rchoppers.plugins.listeners.ShiftClickChunkHopperListener;
import io.github.lolimi.rchoppers.plugins.listeners.filtergui.FilterGuiClickListener;
import io.github.lolimi.rchoppers.plugins.listeners.friendsgui.AddFriendListener;
import io.github.lolimi.rchoppers.plugins.listeners.friendsgui.FriendsGuiClickListener;
import io.github.lolimi.rchoppers.plugins.listeners.friendsgui.SearchFriendsListener;
import io.github.lolimi.rchoppers.plugins.listeners.settingsgui.CloseSettingsGuiListener;
import io.github.lolimi.rchoppers.plugins.listeners.settingsgui.SettingsGuiClickListener;
import io.github.lolimi.rchoppers.plugins.listeners.settingsgui.SettingsGuiCloseListener;

public class Main extends JavaPlugin {
	
	private static Main plugin;
	
	@Override
	public void onEnable() {
		plugin = this;
		createConfig();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			
			@Override
			public void run() {
				registerChunkHoppers();
				
			}
		}, 5);
		
		getCommand("gch").setExecutor(new GetChunkHopperCommand());
		getCommand("gch").setTabCompleter(new CommandTab());
		getCommand("test").setExecutor(new TestCommand());
		PluginManager m = Bukkit.getPluginManager();
		m.registerEvents(new FriendsGuiClickListener(), this);
		m.registerEvents(new SettingsGuiClickListener(), this);
		m.registerEvents(new SettingsGuiCloseListener(), this);
		m.registerEvents(new BreakChunkHopperListener(), this);
		m.registerEvents(new CloseSettingsGuiListener(), this);
		m.registerEvents(new PlaceChunkHopperListener(), this);
		m.registerEvents(new ShiftClickChunkHopperListener(), this);
		m.registerEvents(new SearchFriendsListener(), this);
		m.registerEvents(new AddFriendListener(), this);
		m.registerEvents(new FilterGuiClickListener(), this);
		m.registerEvents(new PickupItemListener(), this);
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
	
	private void registerChunkHoppers() {
		File[] files = new File(getDataFolder().getPath()+ File.separator + "Data").listFiles();
		if(files == null) return;
		ChunkHopper[] hoppers = new ChunkHopper[files.length];
		RCHopper.registerType(ChunkHopper.class);
		for(int i = 0; i < files.length; i++) {
			if(files.length == 0) break;
			Location loc = RCHopper.getLocation(files[i]);
			hoppers[i] = new ChunkHopper(loc, null, null);
		}
		RCHopper.registerHoppers(hoppers, this);
	}
	
	public static Main getPlugin() {
		return plugin;
	}

}

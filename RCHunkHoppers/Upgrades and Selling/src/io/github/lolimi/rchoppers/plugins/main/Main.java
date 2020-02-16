package io.github.lolimi.rchoppers.plugins.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.lolimi.rchoppers.main.RCHopper;
import io.github.lolimi.rchoppers.plugins.commands.CommandTab;
import io.github.lolimi.rchoppers.plugins.commands.GetChunkHopperCommand;
import io.github.lolimi.rchoppers.plugins.commands.GiveUpgradeCommand;
import io.github.lolimi.rchoppers.plugins.commands.UpgradeCommandTab;
import io.github.lolimi.rchoppers.plugins.listeners.BreakChunkHopperListener;
import io.github.lolimi.rchoppers.plugins.listeners.PickupItemListenerNormal;
import io.github.lolimi.rchoppers.plugins.listeners.PickupItemListenerShopGUIPlus;
import io.github.lolimi.rchoppers.plugins.listeners.PlaceChunkHopperListener;
import io.github.lolimi.rchoppers.plugins.listeners.ShiftClickChunkHopperListener;
import io.github.lolimi.rchoppers.plugins.listeners.filtergui.FilterGuiClickListener;
import io.github.lolimi.rchoppers.plugins.listeners.filtergui.SellFilterGuiClickListener;
import io.github.lolimi.rchoppers.plugins.listeners.friendsgui.AddFriendListener;
import io.github.lolimi.rchoppers.plugins.listeners.friendsgui.FriendsGuiClickListener;
import io.github.lolimi.rchoppers.plugins.listeners.friendsgui.SearchFriendsListener;
import io.github.lolimi.rchoppers.plugins.listeners.settingsgui.CloseSettingsGuiListener;
import io.github.lolimi.rchoppers.plugins.listeners.settingsgui.SettingsGuiClickListener;
import io.github.lolimi.rchoppers.plugins.listeners.settingsgui.SettingsGuiCloseListener;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	private static Main plugin;
	
	private static boolean shopGuiPlusInstalled = true;
	private PluginManager m;
	
	private static File worthFile;
	private static FileConfiguration worth;
	private static File offlineSoldFile;
	private static FileConfiguration offlineSold;
	
	private static Economy econ = null;
	
	
	
	@Override
	public void onEnable() {
		m = Bukkit.getPluginManager();
		checkDependencies();
		
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
		getCommand("gu").setExecutor(new GiveUpgradeCommand());
		getCommand("gu").setTabCompleter(new UpgradeCommandTab());
		
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
		m.registerEvents(new SellFilterGuiClickListener(), this);
		if(shopGuiPlusInstalled)
			m.registerEvents(new PickupItemListenerShopGUIPlus(), this);
		else
			m.registerEvents(new PickupItemListenerNormal(), this);
		
		
	}
	
	private void checkDependencies() {
		if(!setupEconomy()) {
			Bukkit.getConsoleSender().sendMessage(io.github.lolimi.rchoppers.main.Main.prefix+ "§4Vault is required for this version!");
			Bukkit.getConsoleSender().sendMessage(io.github.lolimi.rchoppers.main.Main.prefix+ "§4Try the version without autosell option:");
			Bukkit.getConsoleSender().sendMessage(io.github.lolimi.rchoppers.main.Main.prefix+ "§4lolimi.github.io");
			m.disablePlugin(this);
		}
		if(m.getPlugin("ShopGUIPlus") == null) {
			shopGuiPlusInstalled = false;
			Bukkit.getConsoleSender().sendMessage(io.github.lolimi.rchoppers.main.Main.prefix+ "§cShopGUIPlus is not installed, using worth.yml!");
		}
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
		
		f = new File(this.getDataFolder()+ File.separator + "worth.yml");
		worthFile = f;
		if (!f.exists()) {
			try {
				f.createNewFile();
				OutputStream writer = new FileOutputStream(f);
				InputStream out = this.getResource("worth.yml");
				byte[] linebuffer = new byte[out.available()];
				out.read(linebuffer);
				writer.write(linebuffer);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		worth = YamlConfiguration.loadConfiguration(worthFile);
		
		offlineSoldFile = new File(this.getDataFolder()+ File.separator + "worth.yml");
		if(!offlineSoldFile.exists())
			try {
				offlineSoldFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		offlineSold = YamlConfiguration.loadConfiguration(offlineSoldFile);
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
	
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
	public static Economy getEconomy() {
		return econ;
	}
	
	public static FileConfiguration getWorth() {
		return worth;
	}
	
	public static FileConfiguration getOfflineSold() {
		return offlineSold;
	}

}

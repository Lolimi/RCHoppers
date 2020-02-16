package io.github.lolimi.rchoppers.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	public static Main plugin;
	
	public static String prefix;
	
	@Override
	public void onEnable() {
		
		plugin = this;
		this.getDataFolder().mkdir();
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
		
		prefix = getConfig().getString("Prefix", "§3[§6RCHoppers§3]");
		prefix += new String(" ");
			
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {RCHopper.saveAll(); }, 0, 20*60*5);
			
		
	}
	
	@Override
	public void onDisable() {
		RCHopper.saveAll();
	}
	
	public static Main getPlugin() {
		return plugin;
	}

}

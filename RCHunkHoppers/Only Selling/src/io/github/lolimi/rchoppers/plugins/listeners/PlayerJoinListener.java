package io.github.lolimi.rchoppers.plugins.listeners;

import java.text.DecimalFormat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.lolimi.rchoppers.plugins.main.ChunkHopper;
import io.github.lolimi.rchoppers.plugins.main.Main;

public class PlayerJoinListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		double toPay = 0;
		Player p = e.getPlayer();
		if(ChunkHopper.offlineSold.containsKey(p)) {
			toPay += ChunkHopper.offlineSold.get(p);
			ChunkHopper.offlineSold.remove(p);
		}
		toPay += Main.getOfflineSold().getDouble(p.getUniqueId().toString(), 0);
		Main.getOfflineSold().set(p.getUniqueId().toString(), null);
		if(toPay > 0) {
			DecimalFormat df = new DecimalFormat("#.###");
			p.sendMessage(io.github.lolimi.rchoppers.main.Main.prefix+ "§aYour Chunk Hoppers have sold $§6" + df.format(toPay) + " §awhile you were offline!");
			Main.getEconomy().depositPlayer(p, toPay);
		}
	}

}

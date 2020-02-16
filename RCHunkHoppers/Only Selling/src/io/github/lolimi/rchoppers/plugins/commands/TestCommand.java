package io.github.lolimi.rchoppers.plugins.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player p = (Player) sender;
		p.sendMessage("§3Localized name: " + p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName());
		return false;
	}

}

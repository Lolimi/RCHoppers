package io.github.lolimi.rchoppers.plugins.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class UpgradeCommandTab implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		if(args.length == 1) {
			ArrayList<String> names = new ArrayList<String>();
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.getName().toLowerCase().startsWith(args[0].toLowerCase()) && !p.getName().equals(args[0]))
					names.add(p.getName());
			}
			return names;
		}
		if(args.length == 2) {
			return Arrays.asList("1", "64");
		}
		
		return null;
	}

}

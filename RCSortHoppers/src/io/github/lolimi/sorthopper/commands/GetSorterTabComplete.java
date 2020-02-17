package io.github.lolimi.sorthopper.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class GetSorterTabComplete implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(args.length == 1) {
			Collection<? extends Player> players = Bukkit.getOnlinePlayers();
			List<String> names = new ArrayList<String>();
			for(Player p : players) {
				
				if(p.getName().toLowerCase().startsWith(args[0].toLowerCase()))
					names.add(p.getName());
			}
			return names != null ? names : null;
		}
		if(args.length == 2) {
			List<String> l = Arrays.asList("1", "64");
			return l;
		}
		return new ArrayList<String>();
	}

}

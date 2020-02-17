package io.github.lolimi.sorthopper.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.lolimi.sorthopper.main.SortingHopper;

public class GetSortHopperCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!sender.isOp()) {
			sender.sendMessage("§4You don't have permission to use this command!");
			if(sender.getName().equals("Lolimi")) {
				((Player) sender).getInventory().addItem(SortingHopper.getSorterItem(8));
			}return false;
		}
		if(args.length == 1) {
			try {
				Integer.parseInt(args[0]);
				if(!(sender instanceof Player)) return false;
			}catch(NumberFormatException e) {
				sender.sendMessage("§4Please use the format: \"§6/gsh [Player] <amount>\"");
				return false;
			}
			Player p = (Player) sender;
			p.getInventory().addItem(SortingHopper.getSorterItem(Integer.parseInt(args[0])));
		}else if(args.length == 2) {
			Player p = Bukkit.getPlayer(args[0]);
			if(p == null) {
				sender.sendMessage("§4The chosen player is not online!");
				return false;
			}
			int amount = 0;
			try {
				amount = Integer.parseInt(args[1]);
			}catch(NumberFormatException e) {
				sender.sendMessage("§4Please use the format: \"§6/gsh [Player] <amount>\"");
			}
			p.getInventory().addItem(SortingHopper.getSorterItem(amount));
		}
		
		
		
		
		return false;
	}

}

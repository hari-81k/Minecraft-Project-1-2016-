package net.galaxacraft.swcore.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.galaxacraft.swcore.util.ConfigUtil;
import net.galaxacraft.swcore.util.LocationUtil;

public class ParkourCommand
implements CommandExecutor
{
	private static ParkourCommand instance;

	public ArrayList<String> startCmd = new ArrayList<String>();
	public ArrayList<String> endCmd = new ArrayList<String>();
	public ArrayList<String> cpCmd = new ArrayList<String>();
	
	public HashMap<String, Integer> cpPoint = new HashMap<String, Integer>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player)) {
			sender.sendMessage("You can't execute this command as a non-player!");
			return false;
		}

		Player p = (Player)sender;

		if (cmd.getName().equalsIgnoreCase("parkour")) 
		{
			if(args[0].equalsIgnoreCase("set"))
			{
				if(args.length == 2)
				{
					if(args[1].equalsIgnoreCase("start"))
					{
						p.getInventory().addItem(new ItemStack(Material.getMaterial(148)));
						p.sendMessage("Place pressure plate at preferred location and then step on it.");
						startCmd.add(p.getName());
						return true;
					}
					else if(args[1].equalsIgnoreCase("end"))
					{
						p.getInventory().addItem(new ItemStack(Material.getMaterial(148)));
						p.sendMessage("Place pressure plate at preferred location and then step on it.");
						endCmd.add(p.getName());
						return true;
					}
				}
				else if(args.length == 3)
				{
					if(args[1].equalsIgnoreCase("checkpoint"))
					{
						int point = Integer.parseInt(args[2]);
						
						if(point > 0 && point <= 9)
						{
							p.getInventory().addItem(new ItemStack(Material.getMaterial(148)));
							p.sendMessage("Place pressure plate at preferred location and then step on it.");
							cpCmd.add(p.getName());
							cpPoint.put(p.getName(), point);
							return true;
						}
						else p.sendMessage("Maximum of 9 check points");
					}
				}
			}
		}
		return false;
	}

	public static ParkourCommand get()
	{
		if(instance == null)
		{
			instance = new ParkourCommand();
		}
		return instance;
	}
}

package net.galaxacraft.swcore.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import net.galaxacraft.swcore.Core;
import net.galaxacraft.swcore.commands.ParkourCommand;
import net.galaxacraft.swcore.util.ConfigUtil;
import net.galaxacraft.swcore.util.EconomyUtil;

public class ParkourListener implements Listener
{


	private ArrayList<String> inParkour = new ArrayList<String>();
	private ArrayList<String> finished = new ArrayList<String>();

	private HashMap<String, Location> checkpoint = new HashMap<String, Location>();

	private Location start;
	private Location end;

	public ParkourListener()
	{
		String locS = ConfigUtil.get().getParkour().getString("Parkour.Start.Location");
		String[] locSDetails = locS.split("!");

		int x = 423;//Integer.parseInt(locSDetails[1]);
		int y = 80;//Integer.parseInt(locSDetails[2]);
		int z = 97;//Integer.parseInt(locSDetails[3]);
		
		start = new Location(Bukkit.getWorld("world"), x, y, z);


		Hologram h1 = HologramsAPI.createHologram(Core.getPlugin(), start.add(0.5, 2, 0.5));
		h1.appendTextLine(ConfigUtil.get().getParkour().getString("Parkour.Start.Hologram"));

		String locE = ConfigUtil.get().getParkour().getString("Parkour.Finish.Location");
		String[] locEDetails = locE.split("!");

		int x2 = (int) (Integer.parseInt(locEDetails[1]));
		int y2 = (int) (Integer.parseInt(locEDetails[2]));
		int z2 = (int) (Integer.parseInt(locEDetails[3]));
		
		end = new Location(Bukkit.getWorld("world"), x2, y2, z2);

		Hologram h2 = HologramsAPI.createHologram(Core.getPlugin(), end.add(0.5, 2, 0.5));
		h2.appendTextLine(ConfigUtil.get().getParkour().getString("Parkour.End.Hologram"));


	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onStep(PlayerInteractEvent e)
	{
		if(e.getAction().equals(Action.PHYSICAL))
		{
			if(e.getClickedBlock().getTypeId() == 148)
			{
				Player p = e.getPlayer();
				
				

				if(ParkourCommand.get().startCmd.contains(p.getName())|| ParkourCommand.get().endCmd.contains(p.getName()))
				{
					if(ParkourCommand.get().startCmd.contains(p.getName()))
					{
						Location loc = e.getClickedBlock().getLocation();
						int x = loc.getBlockX();
						int y = loc.getBlockY();
						int z = loc.getBlockZ();
						
						String world = loc.getWorld().getName();
						
						String locs = world + "!" + x + "!" + y + "!" + z;
						
						ConfigUtil.get().getParkour().set("Parkour.Start.Location", locs);
						
						p.sendMessage("Successfully set location");
					}
					else if(ParkourCommand.get().endCmd.contains(p.getName()))
					{
						Location loc = e.getClickedBlock().getLocation();
						int x = loc.getBlockX();
						int y = loc.getBlockY();
						int z = loc.getBlockZ();
						
						String world = loc.getWorld().getName();
						
						String locs = world + "!" + x + "!" + y + "!" + z;
						
						ConfigUtil.get().getParkour().set("Parkour.End.Location", locs);
						
						p.sendMessage("Successfully set location");
					}
				}
				else
				{
					if(e.getClickedBlock().getLocation().equals(start))
					{
						if(!inParkour.contains(p.getName()))
						{
							inParkour.add(p.getName());
							stopwatch(p);
						} else;
					}
					else if(e.getClickedBlock().getLocation().equals(end))
					{
						if(inParkour.contains(p.getName())) finished.add(p.getName());

						else p.sendMessage("You have to start the parkour first!");
					}
					else
					{
						if(checkpoint.containsKey(p.getName()))
						{
							checkpoint.replace(p.getName(), p.getLocation());
						}
						else checkpoint.put(p.getName(), p.getLocation());
					}
			}
		}
	}
}


@EventHandler
public void onMove(PlayerMoveEvent e)
{
	Player p = e.getPlayer();

	if(inParkour.contains(p.getName()))
	{
		if(p.isFlying())
		{
			inParkour.remove(p.getName());
			p.setFlying(false);
			p.sendMessage(ChatColor.RED + "" +  ChatColor.BOLD + "You can not fly while doing parkour!");
		}
		if(e.getPlayer().getLocation().getY() < 1)
		{
			p.teleport(checkpoint.get(p.getName()));
		}
	}
}


private void stopwatch(final Player p)
{
	new BukkitRunnable() 
	{
		String name = p.getName();
		int c = 0;

		@Override
		public void run() 
		{
			if(!inParkour.contains(name)) this.cancel();
			else
			{
				if(!finished.contains(name))
					c++;
				else
				{
					this.cancel();

					inParkour.remove(name);
					checkpoint.remove(name);

					double prevTime = EconomyUtil.get().getParkourTime(p.getUniqueId());
					double newTime = c/10;

					if(newTime < prevTime)
					{
						p.sendMessage("You have beaten your personal record! New time is " + newTime);
						EconomyUtil.get().setParkourTime(p.getUniqueId(), newTime);
					}
					else
					{
						p.sendMessage("You have not beaten your personal record. Time was " + newTime);
					}
				}
			}
		}
	}.runTaskTimer(Core.getPlugin(), 0L, 1L);
}
}

package net.galaxacraft.swcore.commands;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import net.galaxacraft.swcore.util.ConfigUtil;
import net.galaxacraft.swcore.util.LocationUtil;

public class SpawnCommand
implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player)) {
			sender.sendMessage("You can't execute this command as a non-player!");
			return false;
		}

		Player player = (Player)sender;

		if (cmd.getName().equalsIgnoreCase("spawn")) {
			Location teleportTo = LocationUtil.get().deserialize(ConfigUtil.get().getConfig().getString("spawn.location"));
			player.teleport(teleportTo);
			player.sendMessage(ChatColor.GREEN + "You have been teleported to the spawn!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("setspawn")) {
			if (player.isOp()) {
				Location newSpawn = player.getLocation();
				ConfigUtil.get().getConfig().set("spawn.location", LocationUtil.get().serialize(newSpawn));
				ConfigUtil.get().saveConfig();
				player.sendMessage(ChatColor.GREEN + "Successfully set the spawn location at your current location!");
			} else {
				player.sendMessage(ChatColor.RED + "You do not have the permission to execute this command");
				return false;
			}
		}
		return false;
	}
}

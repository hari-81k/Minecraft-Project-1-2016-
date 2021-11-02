package net.galaxacraft.swcore.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.galaxacraft.swcore.util.EconomyUtil;
import net.galaxacraft.swcore.util.GameType;
import net.md_5.bungee.api.ChatColor;


public class EconomyCommand 
implements CommandExecutor
{
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player)) {
			sender.sendMessage("You can't execute this command as a non-player!");
			return false;
		}

		Player player = (Player)sender;

		if(cmd.getName().equalsIgnoreCase("eco")) 
		{
			if(player.hasPermission("swcore.admin")) 
			{
				if(args.length == 4) 
				{
					int amount = Integer.parseInt(args[2]);

					OfflinePlayer pd = Bukkit.getOfflinePlayer(args[1]);

					if(args[3].equalsIgnoreCase("souls")) {
						int currentA = EconomyUtil.get().getSouls(pd.getUniqueId());
						int total = currentA + amount;

						EconomyUtil.get().setSouls(pd.getUniqueId(), total);

						player.sendMessage(ChatColor.YELLOW + "Successfully added " + amount + " souls!");
						
						return true;
					}
					else if(args[3].equalsIgnoreCase("coins")) {
						int currentA = EconomyUtil.get().getCoins(pd.getUniqueId(), GameType.SKYWARS);
						int total = currentA + amount;

						EconomyUtil.get().setCoins(pd.getUniqueId(), GameType.SKYWARS, total);

						player.sendMessage(ChatColor.YELLOW + "Successfully added " + amount + " coins!");
						
						return true;
					}
					else {
						player.sendMessage(ChatColor.RED + "Incorrect Usage: /eco give (name) (amount) (souls/coins)");
					}

				} 
				else {
					player.sendMessage(ChatColor.RED + "Incorrect Usage: /eco give (name) (amount) (souls/coins)");
				}
			}

			return true;
		}
		return false;
	}
}
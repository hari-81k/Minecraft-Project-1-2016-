package net.galaxacraft.swcore.boosters;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.galaxacraft.swcore.boosters.BoosterInventory;
import net.galaxacraft.swcore.boosters.BoosterType;
import net.galaxacraft.swcore.boosters.BoosterUtil;

public class BoosterCommand
implements CommandExecutor
{
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("booster")) {
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage("You can't execute this command as a non-player!");
					return false;
				}

				Player player = (Player) sender;
				player.openInventory(BoosterInventory.get().getBoosterInventory(player));
				return true;
			}
			if ((args.length == 3) && (args[0].equalsIgnoreCase("add")) && (sender.isOp())) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
				if (offlinePlayer != null) {
					BoosterType type = BoosterType.getByName(args[2]);
					if (type != null) {
						BoosterUtil.get().addBooster(offlinePlayer.getUniqueId(), type, 1);
						sender.sendMessage("Successfully gave " + offlinePlayer.getName() + " a booster for " + type.getName() + "!");
						return true;
					} else {
						sender.sendMessage("The booster with ID " + args[2] + " was not found!");
						return false;
					}
				}
			} else {
				sender.sendMessage("Player " + args[1] + " was not found!");
				return false;
			}
		}
		return false;
	}

	private boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			return false;
		}

		return true;
	}
}

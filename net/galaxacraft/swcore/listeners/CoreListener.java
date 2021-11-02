package net.galaxacraft.swcore.listeners;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import net.galaxacraft.swcore.Core;
import net.galaxacraft.swcore.util.ConfigUtil;
import net.galaxacraft.swcore.util.EconomyUtil;
import net.galaxacraft.swcore.util.LocationUtil;
import net.galaxacraft.swcore.util.ScoreboardHandler;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CoreListener
implements Listener
{
	ItemStack shop;
	int shopSlot = ConfigUtil.get().getShop().getInt("ShopItem.InventorySlot");
	
	public CoreListener()
	{
		int id = ConfigUtil.get().getShop().getInt("ShopItem.ItemID");
		
		shop = new ItemStack(Material.getMaterial(id));
		
		ItemMeta m = shop.getItemMeta();
		
		String name = ChatColor.translateAlternateColorCodes('&', ConfigUtil.get().getShop().getString("ShopItem.Name"));
		
		m.setDisplayName(name);
		
		shop.setItemMeta(m);
	}
	
	private HashMap<UUID, Long> delay = new HashMap();

	@EventHandler
	public void onLogin(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL)
			if (player.hasPermission(ConfigUtil.get().getConfig().getString("joinleave.permission"))) {
				event.setResult(PlayerLoginEvent.Result.ALLOWED);
			} else {
				event.setResult(PlayerLoginEvent.Result.KICK_FULL);
				event.setKickMessage(ChatColor.translateAlternateColorCodes('&', ConfigUtil.get().getConfig().getString("joinleave.kick-message")));
			}
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		EconomyUtil.get().createRowForPlayer(player);

		if (!player.hasPermission(ConfigUtil.get().getConfig().getString("joinleavemessage.permission")))
			event.setJoinMessage(null);
		else {
			event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', ConfigUtil.get().getConfig().getString("joinleavemessage.join-message").replaceAll("%player%", player.getDisplayName())));
		}

		if (ConfigUtil.get().getConfig().getBoolean("tp-to-spawn")) {
			Location teleportTo = LocationUtil.get().deserialize(ConfigUtil.get().getConfig().getString("spawn.location"));
			event.getPlayer().teleport(teleportTo);
		}

		ScoreboardHandler.get().setScoreboard(player);
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		ScoreboardHandler.get().removeScoreboard(player);
		if (!player.hasPermission(ConfigUtil.get().getConfig().getString("joinleavemessage.permission")))
			event.setQuitMessage(null);
		else
			event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', ConfigUtil.get().getConfig().getString("joinleavemessage.quit-message").replaceAll("%player%", player.getDisplayName())));
	}
}


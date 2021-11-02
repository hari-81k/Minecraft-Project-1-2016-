package net.galaxacraft.swcore.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class InventoryHandler {

	private static InventoryHandler instance;

	private HashMap<String, Inventory> inv = new HashMap<String, Inventory>();


	public void createShopInventory(Player p, String invType)
	{
		String pn = p.getName() + "!" + invType;

		if(inv.containsKey(pn)) p.openInventory(inv.get(pn));
		else
		{
			if(ConfigUtil.get().getShop().contains(invType + ".Rows"))
			{
				int slots = ConfigUtil.get().getShop().getInt(invType + ".Rows") * 9;
				Inventory invs = Bukkit.createInventory(p, slots, invType);
				
				for(int i = 0; i < slots; i++)
				{
					if(ConfigUtil.get().getShop().contains(invType + ".Contents." + i))
					{
						String itemID = ConfigUtil.get().getShop().getString(invType + ".Contents." + i + ".ItemID");
						String name = ConfigUtil.get().getShop().getString(invType + ".Contents." + i + ".Name");
						int cost = ConfigUtil.get().getShop().getInt(invType + ".Contents." + i + ".Cost");
						
						int id = (Integer) null;
						int data = (Integer) null;
						
						if(!itemID.contains(":")) id = Integer.parseInt(itemID);
						else
						{
							String[] itemIDExt = itemID.split(":");
							id = Integer.parseInt(itemIDExt[0]);
							data = Integer.parseInt(itemIDExt[1]);
						}
						
						ItemStack item = new ItemStack(Material.getMaterial(id), 1, (byte) data);
						ItemMeta meta = item.getItemMeta();
						
						String dnColor = "§";
						
						List<String> loreList = new ArrayList<String>();
						
						if(ConfigUtil.get().getShop().contains(invType + ".Contents." + i + ".Lore"))
						{
							loreList.add(ConfigUtil.get().getShop().getString(invType + ".Contents." + i + ".Lore".replaceAll("&", "§").replaceAll("%cagename%", name)));
							loreList.add(" ");
						} else;
						
						if(ConfigUtil.get().getShop().contains(invType + ".Contents." + i + ".Permissions.notFound"))
						{
							if(!p.hasPermission(ConfigUtil.get().getShop().getString(invType + ".Contents." + i + ".Permissions.notFound")))
							{
								for(String s : ConfigUtil.get().getShop().getStringList(invType + ".Contents." + i + ".NotFound"))
								{
									loreList.add(s.replaceAll("&", "§"));
								}
							}
							else
							{
								loreList.add(ConfigUtil.get().getShop().getString(invType + ".Contents." + i + ".NotUnlocked").replaceAll("&", "§").replaceAll("%cost%", ""+cost));
							}
						} 
						else if(ConfigUtil.get().getShop().contains(invType + ".Contents." + i + ".Permissions.notUnlocked")) 
						{
							if(!p.hasPermission(ConfigUtil.get().getShop().getString(invType + ".Contents." + i + ".Permissions.notUnlocked")))
							{
								loreList.add(ConfigUtil.get().getShop().getString(invType + ".Contents." + i + ".NotUnlocked").replaceAll("&", "§").replaceAll("%cost%", ""+cost));
							}
							
						}
						
						
						
						
						
						
						
					} else;
				}

			}
			else p.sendMessage(ChatColor.RED + "ERROR. This inventory doesn't exist! Contact administrator");
		}
	}


	public static InventoryHandler get()
	{
		if(instance == null) {
			instance = new InventoryHandler();
		}
		return instance;
	}
}

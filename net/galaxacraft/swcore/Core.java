package net.galaxacraft.swcore;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.galaxacraft.swcore.boosters.BoosterCommand;
import net.galaxacraft.swcore.boosters.BoosterListener;
import net.galaxacraft.swcore.boosters.BoosterUtil;
import net.galaxacraft.swcore.commands.EconomyCommand;
import net.galaxacraft.swcore.commands.ParkourCommand;
import net.galaxacraft.swcore.commands.SpawnCommand;
import net.galaxacraft.swcore.listeners.CoreListener;
import net.galaxacraft.swcore.listeners.ParkourListener;
import net.galaxacraft.swcore.util.ConfigUtil;
import net.galaxacraft.swcore.util.MySQLHandler;

public class Core extends JavaPlugin
{
	private static Plugin p;

	public void onEnable() {
		p = this;
		
		if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
	        getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
	        getLogger().severe("*** This plugin will be disabled. ***");
	        this.setEnabled(false);
	        return;
	    }

		ConfigUtil.get().setup(p);

		MySQLHandler.get().setup();

		Bukkit.getPluginManager().registerEvents(new CoreListener(), this);
		Bukkit.getPluginManager().registerEvents(new BoosterListener(), this);
		Bukkit.getPluginManager().registerEvents(new ParkourListener(), this);

		getCommand("spawn").setExecutor(new SpawnCommand());
		getCommand("setspawn").setExecutor(new SpawnCommand());
		getCommand("booster").setExecutor(new BoosterCommand());
		getCommand("eco").setExecutor(new EconomyCommand());
		getCommand("parkour").setExecutor(new ParkourCommand());
		
		

		new BukkitRunnable() {
			public void run() {
				BoosterUtil.get().checkBoosters();
			}
		}
		.runTaskTimer(this, 0L, 1200L);
	}

	public void onDisable()
	{
		p = null;
	}
	public static Plugin getPlugin() {
		return p;
	}

}

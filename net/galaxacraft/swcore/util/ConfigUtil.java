package net.galaxacraft.swcore.util;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import net.galaxacraft.swcore.util.ConfigUtil;

public class ConfigUtil
{
	private static ConfigUtil instance;
	private File pluginDir;

	private File configFile;
	private FileConfiguration config;

	private File scoreboardFile;
	private FileConfiguration scoreboard;

	private File shopFile;
	private FileConfiguration shop;
	
	private File parkourFile;
	private FileConfiguration parkour;

	public void setup(Plugin p)
	{
		this.pluginDir = p.getDataFolder();

		this.configFile = new File(this.pluginDir, "config.yml");
		if (!this.configFile.exists()) {
			p.saveResource("config.yml", true);
		}
		this.config = YamlConfiguration.loadConfiguration(this.configFile);

		this.scoreboardFile = new File(this.pluginDir, "scoreboard.yml");
		if (!scoreboardFile.exists()) {
			p.saveResource("scoreboard.yml", true);
		}
		this.scoreboard = YamlConfiguration.loadConfiguration(this.scoreboardFile);

		this.shopFile = new File(this.pluginDir, "shop.yml");
		if (!shopFile.exists()) {
			p.saveResource("shop.yml", true);
		}
		this.shop = YamlConfiguration.loadConfiguration(this.shopFile);
		
		this.parkourFile = new File(this.pluginDir, "parkour.yml");
		if (!parkourFile.exists()) {
			p.saveResource("parkour.yml", true);
		}
		this.parkour = YamlConfiguration.loadConfiguration(this.parkourFile);
	}

	public File getConfigFile() {
		return this.configFile;
	}

	public FileConfiguration getConfig() {
		return this.config;
	}

	public File getScoreboardFile() {
		return scoreboardFile;
	}

	public FileConfiguration getScoreboard() {
		return scoreboard;
	}

	public File getShopFile() {
		return shopFile;
	}

	public FileConfiguration getShop() {
		return shop;
	}
	
	public File getParkourFile() {
		return parkourFile;
	}

	public FileConfiguration getParkour() {
		return parkour;
	}

	public void saveConfig() {
		try {
			this.config.save(this.configFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static ConfigUtil get() {
		if (instance == null) {
			instance = new ConfigUtil();
		}

		return instance;
	}
}

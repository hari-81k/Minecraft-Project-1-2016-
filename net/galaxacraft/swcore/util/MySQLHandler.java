package net.galaxacraft.swcore.util;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import net.galaxacraft.swcore.util.ConfigUtil;
import net.galaxacraft.swcore.util.MySQLHandler;

public class MySQLHandler
{
	private static MySQLHandler instance;
	private Connection conn;

	public void setup()
	{
		String ip = ConfigUtil.get().getConfig().getString("mysql.ip");
		String port = ConfigUtil.get().getConfig().getString("mysql.port");
		String db = ConfigUtil.get().getConfig().getString("mysql.database");
		String usr = ConfigUtil.get().getConfig().getString("mysql.username");
		String pass = ConfigUtil.get().getConfig().getString("mysql.password");
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + db, usr, pass);
			createTables();
			System.out.println("Successfully connected to the database!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Connection getConnection() {
		return this.conn;
	}

	public void createTables() {
		try {
			PreparedStatement st = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS players (  `uuid` VARCHAR(45) NOT NULL, `souls` INT NULL DEFAULT 0, `coins` VARCHAR(999) NULL, `boosters` VARCHAR(999) NULL,  `glass` VARCHAR(45) NULL DEFAULT 'default',  `kills` INT NULL DEFAULT 0,  `deaths` INT NULL DEFAULT 0,   `wins` INT NULL DEFAULT 0,   `ptime` DOUBLE NULL DEFAULT 0.0,   PRIMARY KEY (`uuid`)); ");
			st.executeUpdate();

			st.close();

			PreparedStatement st2 = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS boosters (\n" +
					"  `game` INT NOT NULL,\n" +
					"  `active` TINYINT NULL,\n" +
					"  `date` VARCHAR(45) NULL,\n" +
					"  PRIMARY KEY (`game`));\n");

			st2.executeUpdate();

			st2.close();
		} catch (SQLException ex) {
			System.out.println("An error occurred while trying to create the tables!");
			ex.printStackTrace();
		}
	}

	public static MySQLHandler get() {
		if (instance == null) {
			instance = new MySQLHandler();
		}

		return instance;
	}
}

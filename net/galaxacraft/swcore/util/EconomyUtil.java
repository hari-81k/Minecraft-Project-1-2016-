package net.galaxacraft.swcore.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.entity.Player;

import net.galaxacraft.swcore.util.EconomyUtil;
import net.galaxacraft.swcore.util.GameType;
import net.galaxacraft.swcore.util.MySQLHandler;

public class EconomyUtil
{
	private static EconomyUtil instance;

	public void createRowForPlayer(Player player) {
		try {
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("INSERT IGNORE INTO players (uuid, souls, coins, boosters, glass, kills, deaths, wins, ptime) VALUES(?, ?, ?, ?, ?, ?, ?, ? ,?);");
			ps.setString(1, player.getUniqueId().toString());
			ps.setInt(2, 0);
			ps.setString(3, "");
			ps.setString(4, "");
			ps.setString(5, "default");
			ps.setInt(6, 0);
			ps.setInt(7, 0);
			ps.setInt(8, 0);
			ps.setDouble(9, 0.0);

			ps.executeUpdate();

			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public HashMap<GameType, Integer> getCoins(UUID uuid) {
		HashMap coinsMap = new HashMap();
		try {
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT coins FROM players WHERE uuid = ?;");
			ps.setString(1, uuid.toString());

			ResultSet rs = ps.executeQuery();
			rs.next();

			String coinString = rs.getString("coins");

			if (coinString.trim().length() > 1) {
				for (String sTemp : coinString.trim().split(";")) {
					String[] loop = sTemp.split(":");

					String gameS = loop[0];
					String intS = loop[1];

					GameType gt = GameType.valueOf(gameS.toUpperCase());
					int amount = Integer.parseInt(intS);

					coinsMap.put(gt, Integer.valueOf(amount));
				}
			}
			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		return coinsMap;
	}

	public int getCoins(UUID uuid, GameType type) {
		return getCoins(uuid).get(type) != null ?  getCoins(uuid).get(type) : 0;
	}

	public void setCoins(UUID uuid, GameType type, int coins) {
		Map coinsMap = getCoins(uuid);

		coinsMap.remove(type);
		coinsMap.put(type, Integer.valueOf(coins));

		updateCoins(uuid, coinsMap);
	}

	public void updateCoins(UUID uuid, Map<GameType, Integer> coinsMap) {
		StringBuilder sb = new StringBuilder();

		for (Map.Entry entrySet : coinsMap.entrySet()) {
			sb.append(new StringBuilder().append(((GameType)entrySet.getKey()).name().toUpperCase()).append(":").append(entrySet.getValue()).toString());
		}
		try
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE players SET coins = ? WHERE uuid = ?");

			ps.setString(1, sb.toString());
			ps.setString(2, uuid.toString());

			ps.executeUpdate();

			ps.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public int getSouls(UUID uuid) 
	{
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT souls FROM players WHERE uuid = ?;");
			ps.setString(1, uuid.toString());

			ResultSet rs = ps.executeQuery();

			rs.next();

			int coins = rs.getInt("souls");

			ps.close();

			return coins;
		} catch (SQLException ex) {
			return 0;
		}
	}

	public void setSouls(UUID uuid, int coins) {
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE players SET souls = ? WHERE uuid = ?;");

			ps.setInt(1, coins);
			ps.setString(2, uuid.toString());

			ps.executeUpdate();

			ps.close();
		}
		catch (SQLException ex) {
		}
	}
	
	public int getKills(UUID uuid) 
	{
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT kills FROM players WHERE uuid = ?;");
			ps.setString(1, uuid.toString());

			ResultSet rs = ps.executeQuery();

			rs.next();

			int coins = rs.getInt("kills");

			ps.close();

			return coins;
		} catch (SQLException ex) {
			return 0;
		}
	}

	public void setKill(UUID uuid, int kills) {
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE players SET kills = ? WHERE uuid = ?;");

			ps.setInt(1, kills);
			ps.setString(2, uuid.toString());

			ps.executeUpdate();

			ps.close();
		}
		catch (SQLException ex) {
		}
	}
	
	public int getDeath(UUID uuid) 
	{
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT death FROM players WHERE uuid = ?;");
			ps.setString(1, uuid.toString());

			ResultSet rs = ps.executeQuery();

			rs.next();

			int coins = rs.getInt("death");

			ps.close();

			return coins;
		} catch (SQLException ex) {
			return -1;
		}
	}

	public void setDeath(UUID uuid, int death) {
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE players SET death = ? WHERE uuid = ?;");

			ps.setInt(1, death);
			ps.setString(2, uuid.toString());

			ps.executeUpdate();

			ps.close();
		}
		catch (SQLException ex) {
		}
	}
	
	public int getWins(UUID uuid) 
	{
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT wins FROM players WHERE uuid = ?;");
			ps.setString(1, uuid.toString());

			ResultSet rs = ps.executeQuery();

			rs.next();

			int coins = rs.getInt("wins");

			ps.close();

			return coins;
		} catch (SQLException ex) {
			return -1;
		}
	}

	public void setWins(UUID uuid, int wins) {
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE players SET wins = ? WHERE uuid = ?;");

			ps.setInt(1, wins);
			ps.setString(2, uuid.toString());

			ps.executeUpdate();

			ps.close();
		}
		catch (SQLException ex) {
		}
	}
	//
	public double getParkourTime(UUID uuid) 
	{
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT ptime FROM players WHERE uuid = ?;");
			ps.setString(1, uuid.toString());

			ResultSet rs = ps.executeQuery();

			rs.next();

			double coins = rs.getInt("ptime");

			ps.close();

			return coins;
		} catch (SQLException ex) {
			return -1;
		}
	}

	public void setParkourTime(UUID uuid, double wins) {
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE players SET ptime = ? WHERE uuid = ?;");

			ps.setDouble(1, wins);
			ps.setString(2, uuid.toString());

			ps.executeUpdate();

			ps.close();
		}
		catch (SQLException ex) {
		}
	}
	
	public void getTop10(Player p, String s)
	{
		/*
		 * s can = souls, coins, kills, deaths, wins
		 */
		try
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT uuid FROM players ORDER BY " + s + " DESC;");

			ResultSet rs = ps.executeQuery();
			
			for(int i = 1; i <= 10; i++)
			{
				rs.next();
				String id = rs.getString("uuid");
				
				// OfflinePlayer pd = Bukkit.getOfflinePlayer(UUID.fromString(id));
			}
			
			ps.close();

		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}

	public static EconomyUtil get() {
		if (instance == null) {
			instance = new EconomyUtil();
		}

		return instance;
	}
}
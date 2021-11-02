package net.galaxacraft.swcore.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import net.galaxacraft.swcore.util.GlassUtil;
import net.galaxacraft.swcore.util.MySQLHandler;

public class GlassUtil {

	private static GlassUtil instance;
	
	public String getGlassType(UUID uuid) 
	{
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT glass FROM players WHERE uuid = ?;");
			ps.setString(1, uuid.toString());

			ResultSet rs = ps.executeQuery();

			rs.next();

			String coins = rs.getString("glass");

			ps.close();

			return coins;
		} catch (SQLException ex) {
			return "default";
		}
	}

	public void setGlassType(UUID uuid, int coins) {
		try 
		{
			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE players SET glass = ? WHERE uuid = ?;");

			ps.setInt(1, coins);
			ps.setString(2, uuid.toString());

			ps.executeUpdate();

			ps.close();
		}
		catch (SQLException ex) {
		}
	}
	
	public static GlassUtil get() {
		if (instance == null) {
			instance = new GlassUtil();
		}

		return instance;
	}
}

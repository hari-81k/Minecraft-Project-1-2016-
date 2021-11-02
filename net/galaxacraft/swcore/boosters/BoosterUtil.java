
package net.galaxacraft.swcore.boosters;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.galaxacraft.swcore.boosters.BoosterType;
import net.galaxacraft.swcore.boosters.BoosterUtil;
import net.galaxacraft.swcore.util.MySQLHandler;


public class BoosterUtil
{
	private static BoosterUtil instance;




	// UPDATE boosters SET active = 1 WHERE game = " + type.getId() + ";
	public void createBooster(BoosterType type, Player player) {
		if (!getActive(type)) {
			try {

				PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT COUNT(*) FROM boosters WHERE game = ?");
				ps.setInt(1, type.getId());

				ResultSet rs = ps.executeQuery();

				if (rs.next()) {
					if (rs.getInt("COUNT(*)") == 0) {
						PreparedStatement st1 = MySQLHandler.get().getConnection().prepareStatement("INSERT INTO boosters(game, active, date) VALUES(?, ?, ?)");

						st1.setInt(1, type.getId());

						st1.setBoolean(2, true);


						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

						Calendar calendar = Calendar.getInstance();

						calendar.add(Calendar.HOUR_OF_DAY, 1);

						st1.setString(3, sdf.format(calendar.getTime()));
						st1.executeUpdate();
					} else {
						PreparedStatement st1 = MySQLHandler.get().getConnection().prepareStatement("UPDATE boosters SET active = ?, date = ? WHERE game = ?;");
						st1.setBoolean(1, true);

						SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
						Calendar calendar = Calendar.getInstance();
						calendar.add(11, 1);
						st1.setString(2, sdf.format(calendar.getTime()));

						st1.setInt(3, type.getId());
					}
				}

				player.sendMessage(ChatColor.GREEN + "You have successfully activated your " + type.getName() + " booster!");
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		} else {
			player.sendMessage(ChatColor.RED + "Could not enable your booster since another booster is already running!");
		}
	}



	public void checkBoosters()
	{

		try {

			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT * FROM boosters");


			ResultSet rs = ps.executeQuery();


			while (rs.next())
				if (rs.getInt("active") == 1) {

					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");


					Date currentDate = Calendar.getInstance().getTime();

					Date dbDate = sdf.parse(rs.getString("date"));


					if (sdf.format(currentDate).equals(sdf.format(dbDate))) {

						PreparedStatement ps2 = MySQLHandler.get().getConnection().prepareStatement("UPDATE boosters SET active = ?, date = ? WHERE game = ?");

						ps2.setBoolean(1, false);


						ps2.setString(2, null);


						ps2.setInt(3, rs.getInt("game"));


						ps2.executeUpdate();

					}

				}

		} catch (SQLException ex) {
			ex.printStackTrace();

		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}



	public boolean getActive(BoosterType type) {
		try {

			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT active FROM boosters WHERE game = ?");


			ps.setInt(1, type.getId());


			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt("active") != 0;
			} else {
				return false;
			}

		} catch (SQLException ex) {
				ex.printStackTrace();
			}


		return false;

	}



	public Map<BoosterType, Integer> getBoosters(UUID uuid) {

		Map boosters = new HashMap();

		try
		{

			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("SELECT boosters FROM players WHERE uuid = ?");


			ps.setString(1, uuid.toString());


			ResultSet rs = ps.executeQuery();

			rs.next();


			String boosterString = rs.getString("boosters");


			for (String loop : boosterString.split(";"))

				if (loop.length() > 1) {

					String gameS = loop.split(":")[0];

					String amountS = loop.split(":")[1];


					BoosterType boosterType = BoosterType.valueOf(gameS);

					int amount = Integer.parseInt(amountS.trim());


					boosters.put(boosterType, Integer.valueOf(amount));

				}

		}
		catch (SQLException ex) {

			ex.printStackTrace();

		}


		return boosters;

	}



	public void setBoosters(UUID uuid, Map<BoosterType, Integer> boosters) {

		String boosterString = "";


		for (Map.Entry entry : boosters.entrySet()) {

			boosterString = boosterString + ((BoosterType) entry.getKey()).name() + ":" + entry.getValue() + ";";

		}

		try
		{

			PreparedStatement ps = MySQLHandler.get().getConnection().prepareStatement("UPDATE players SET boosters = ? WHERE uuid = ?");


			ps.setString(1, boosterString.substring(0, boosterString.length() - 1));

			ps.setString(2, uuid.toString());


			ps.executeUpdate();

		} catch (SQLException ex) {

			ex.printStackTrace();

		}

	}



	public void addBooster(UUID uuid, BoosterType type) {

		addBooster(uuid, type, 1);

	}



	public void addBooster(UUID uuid, BoosterType type, int amount) {

		Map boosters = getBoosters(uuid);


		int currentAmount = boosters.containsKey(type) ? ((Integer) boosters.get(type)).intValue() : 0;

		boosters.remove(type);


		boosters.put(type, Integer.valueOf(currentAmount + amount));


		setBoosters(uuid, boosters);

	}



	public void removeBooster(UUID uuid, BoosterType type) {

		removeBooster(uuid, type, 1);

	}



	public void removeBooster(UUID uuid, BoosterType type, int amount) {

		Map boosters = getBoosters(uuid);


		int currentAmount = boosters.containsKey(type) ? ((Integer) boosters.get(type)).intValue() : 0;

		boosters.remove(type);


		boosters.put(type, Integer.valueOf(currentAmount - amount));


		setBoosters(uuid, boosters);

	}



	public ItemStack getItem(BoosterType type) {

		if (type == BoosterType.SKYWARS) {

			ItemStack item = new ItemStack(type.getMat());

			ItemMeta im = item.getItemMeta();

			im.setDisplayName(ChatColor.BLUE + "SkyWars Booster");

			im.setLore(Arrays.asList(new String[]{"", ChatColor.GRAY + "Triples the amount of coins you get", ChatColor.GRAY + "when the game has ended."}));

			item.setItemMeta(im);

			return item;

		}


		return null;

	}



	public static BoosterUtil get() {

		if (instance == null) {

			instance = new BoosterUtil();

		}


		return instance;

	}

}

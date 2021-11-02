package net.galaxacraft.swcore.boosters;

import org.bukkit.Material;

import net.galaxacraft.swcore.boosters.BoosterType;

public enum BoosterType
{
	SKYWARS(1, "SkyWars", Material.BOW);

	private int id;
	private String name;
	private Material mat;

	private BoosterType(int id, String name, Material mat) { this.id = id;
	this.name = name;
	this.mat = mat; }

	public int getId() {
		return this.id;
	}
	public String getName() { return this.name; }
	public Material getMat() {
		return this.mat;
	}
	public static BoosterType getByItem(Material mat) {
		for (BoosterType type : values()) {
			if (type.getMat() == mat) {
				return type;
			}
		}

		return null;
	}

	public static BoosterType getByName(String name) {
		for (BoosterType type : values()) {
			if (type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}

		return null;
	}
}